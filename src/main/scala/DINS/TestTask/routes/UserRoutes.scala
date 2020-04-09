package DINS.TestTask.routes

import DINS.TestTask.data.db.UserDao
import DINS.TestTask.data.dto.UserDto
import DINS.TestTask.service.DBService
import DINS.TestTask.util.JsonProtocol
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

class UserRoutes(service: DBService)(implicit ex: ExecutionContext)
  extends JsonProtocol {

  val routes: Route = pathPrefix("users") {
    pathEndOrSingleSlash {
      get { // GET ALL
        complete(ToResponseMarshallable(service.allUsers))
      } ~
        post { // CREATE NEW
          entity(as[UserDto]) { userDto =>
            complete {
              (StatusCodes.Created, service.insertUser(userDto))
            }
          }
        }
    } ~
      path(Segment) { id =>
        get { // GET BY ID
          complete {
            val result = service.getUserById(id.toLong)
            result match {
              case Some(user) => (StatusCodes.OK, user)
              case None => HttpResponse(StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
            }
          }
        } ~
          delete { // DELETE BY ID
            complete {
              val result = service.deleteUserById(id.toLong)
              if (result) StatusCodes.OK
              else HttpResponse(StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
            }
          } ~
          put { // UPDATE BY ID
            entity(as[UserDto]) { userDto: UserDto =>
              complete {
                val result = service.updateUserById(id.toLong, userDto)
                result match {
                  case Some(user) => (StatusCodes.OK, user)
                  case None =>HttpResponse (StatusCodes.NotFound, entity = s"does not exist users with the id: $id")
                }
              }
            }
          }
      }
  }
}