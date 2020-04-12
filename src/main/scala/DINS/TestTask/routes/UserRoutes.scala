package DINS.TestTask.routes

import DINS.TestTask.data.dto.UserFromHttp
import DINS.TestTask.service.DBService
import DINS.TestTask.util.JsonProtocol
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{entity, _}
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

class UserRoutes(service: DBService)(implicit ex: ExecutionContext)
  extends JsonProtocol {

  private val cors = new CORSHandler {}

  val routes: Route = cors.corsHandler(pathPrefix("users") {
    pathEndOrSingleSlash {
      getAllUsers ~ createUser()
    } ~
    path(Segment) { idString =>
      Try(idString.toLong) match {
        case Success(id) => getUser(id) ~ deleteUser(id) ~ putUser(id)
        case Failure(_) => complete(HttpResponse(StatusCodes.BadRequest, entity = s"id expected: $idString"))
      }
    }
  })

  // GET ALL
  def getAllUsers: Route = get {
    complete(ToResponseMarshallable(service.allUsers))
  }

  // CREATE NEW
  def createUser(): Route = cors.corsHandler(post {
    entity(as[UserFromHttp]) { userFromHttp =>
      complete {
        (StatusCodes.Created, service.insertUser(userFromHttp))
      }
    }
  })

  // GET BY ID
  def getUser(id: Long): Route = get {
    complete {
      val result = service.getUserById(id.toLong)
      result match {
        case Some(user) => (StatusCodes.OK, user)
        case None       => HttpResponse(StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
      }
    }
  }

  // DELETE BY ID
  def deleteUser(id: Long): Route = delete {
    complete {
      val result = service.deleteUserById(id.toLong)
      if (result) StatusCodes.OK
      else HttpResponse(StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
    }
  }
  // UPDATE BY ID
  def putUser(id: Long): Route = put {
    entity(as[UserFromHttp]) { userFromHttp: UserFromHttp =>
      complete {
        val result = service.updateUserById(id.toLong, userFromHttp)
        result match {
          case Some(user) => (StatusCodes.OK, user)
          case None       => HttpResponse (StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
        }
      }
    }
  }
}