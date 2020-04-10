package DINS.TestTask.service

import DINS.TestTask.data.dao.UserDao
import DINS.TestTask.data.db.{DB, DataBaseSchema}
import DINS.TestTask.data.dto.{UserFromHttp, UserToHttp}
import DINS.TestTask.data.model.UserWithAddress
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Success

class DBService(implicit ex: ExecutionContext) extends DataBaseSchema with DB { this: DB =>

  import driver.api._

  private val waitTime = Duration.Inf

  lazy val userDao: UserDao = new UserDao

  def createSchemaIfNotExists(): Future[Unit] = {
    db.run(MTable.getTables("ADDRESSES")).flatMap {
      case tables  =>
        println("Schema already exists" + tables)
        db.run(mainSchema.create).andThen {
          case Success(_) => println("addresses Schema created")
        }

      case tables if tables.nonEmpty =>
        println("Schema already exists" + tables)
        Future.successful()
    }
  }

  def initDB(): Unit = userDao.initDB()

  def dropDB(): Unit = userDao.dropDB()

  def allUsers: Seq[UserToHttp] = {
    val result = Await.result(userDao.getAllUsersWithAddress, waitTime)
    result.map(UserToHttp(_))
  }

  def insertUser(userDto: UserFromHttp): UserToHttp = {
    val result = Await.result(userDao.addUserWithAddress(userDto.toUserWithAddress), waitTime)
    UserToHttp(result)
  }

  def getUserById(id: Long): Option[UserToHttp] = {
    val result = Await.result(userDao.getUserWithAddressById(id), waitTime)
    result match {
      case Some(x) => Some(UserToHttp(x))
      case None => None
    }
  }

  def updateUserById(id: Long, userFromHttp: UserFromHttp): Option[UserToHttp] = {
    val addressQuery = Await.result(db.run(users.filter(_.id === id).map(_.addressId).result.headOption), Duration.Inf).flatten
    addressQuery match {
      case Some(addressId) =>
        val newAddress = userFromHttp.address.copy(id = Some(addressId))
        val newUser = userFromHttp.toUser.copy(id = Some(id), addressId = Some(addressId))
        val newUserWithAddress = UserWithAddress(newUser, newAddress)

        val updateAddressAction = userDao.updateAddressByIdAction(addressId, newAddress)
        val updateUserAction = userDao.updateUserByIdAction(id, newUser)
        val zipAction = updateAddressAction.zip(updateUserAction)

        val result = Await.result(db.run(zipAction.map(res => res._1 > 0 && res._2 > 0).transactionally), waitTime)

        if (result) Some(UserToHttp(newUserWithAddress))
        else None
      case None => None
    }
  }

  def deleteUserById(id: Long): Boolean = {
    val addressQuery = Await.result(db.run(users.filter(_.id === id).map(_.addressId).result.headOption), Duration.Inf).flatten
    addressQuery match {
      case Some(addressId) =>
        val deleteAddressAction = userDao.deleteAddressByIdAction(addressId)
        val deleteUserAction = userDao.deleteUserByIdAction(id)
        Await.result(db.run(deleteAddressAction.andThen(deleteUserAction)), waitTime)
        true
      case None => false
    }
  }
}
