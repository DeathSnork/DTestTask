package DINS.TestTask.service

import DINS.TestTask.data.db.{DataBaseSchema, UserDao}
import DINS.TestTask.data.dto.{UserDto, UserDtoWithId}
import DINS.TestTask.data.model.UserWithAddress
import DINS.TestTask.data.persistance.DB
import DINS.TestTask.routes.UserRoutes
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

//import slick.jdbc.PostgresProfile.api._

class DBService(implicit ex: ExecutionContext) extends DataBaseSchema with DB { this: DB =>

  import driver.api._

  private val waitTime = Duration.Inf

  lazy val userDao: UserDao = new UserDao

  def initDB(): Unit = userDao.initDB()

  def dropDB(): Unit = userDao.dropDB()

  def allUsers: Seq[UserDtoWithId] = {
    val result = Await.result(userDao.getAllUsersWithAddress, waitTime)
    result.map(UserDtoWithId(_))
  }

  def insertUser(userDto: UserDto): UserDtoWithId = {
    val result = Await.result(userDao.addUserWithAddress2(userDto.toUserWithAddress), waitTime)
    UserDtoWithId(result)
  }

  def getUserById(id: Long): Option[UserDtoWithId] = {
    val result = Await.result(userDao.getUserWithAddressById(id), waitTime)
    result match {
      case Some(x) => Some(UserDtoWithId(x))
      case None => None
    }
  }

  def deleteUserById(id: Long): Boolean = {
    val result = Await.result(userDao.deleteUserById(id), waitTime)
    result
  }

  def updateUserById(id: Long, userDto: UserDto) : Option[UserWithAddress] = {
    None
  }
}
