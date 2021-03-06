package DINS.TestTask.data.dao

import DINS.TestTask.data.db.DataBaseSchema
import DINS.TestTask.data.model.{Address, User, UserWithAddress}
import slick.sql.FixedSqlAction

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class UserDao(implicit ex: ExecutionContext) extends DataBaseSchema {

  import driver.api._

  def AddressSchemaCreation(): FixedSqlAction[Unit, NoStream, Effect.Schema] = addresses.schema.create
  def AddressSchemaDeletion(): FixedSqlAction[Unit, NoStream, Effect.Schema] = addresses.schema.drop
  def UserSchemaCreation():    FixedSqlAction[Unit, NoStream, Effect.Schema] = users.schema.create
  def UserSchemaDeletion():    FixedSqlAction[Unit, NoStream, Effect.Schema] = users.schema.drop

  def initUsers: Future[Unit]     = db.run(UserSchemaCreation())
  def dropUsers: Future[Unit]     = db.run(UserSchemaDeletion())
  def initAddresses: Future[Unit] = db.run(AddressSchemaCreation())
  def dropAddresses: Future[Unit] = db.run(AddressSchemaDeletion())

  def initDB(): Unit = {
    Await.result(initAddresses, Duration.Inf)
    Await.result(initUsers, Duration.Inf)
  }

  def dropDB(): Unit = {
    Await.result(dropUsers, Duration.Inf)
    Await.result(dropAddresses, Duration.Inf)
  }

  def updateAddressByIdAction(id: Long, address: Address): DBIO[Int] = {
    addresses.filter(_.id === id).update(address)
  }

  def updateUserByIdAction(id: Long, user: User): DBIO[Int] = {
    users.filter(_.id === id).update(user)
  }

  def getAllUsers: Future[Seq[User]] = {
    val query = users.to[Seq].result
    db.run(query)
  }

  def getUserById(id: Long): Future[Option[User]] = {
    val query = users.filter(_.id === id).result.headOption
    db.run(query)
  }

  def deleteUserByIdAction(id: Long): DBIO[Boolean] = {
    users.filter(_.id === id).delete.map(_ > 0)
  }

  def deleteAddressByIdAction(addressId: Long): DBIO[Boolean] = {
    addresses.filter(_.id === addressId).delete.map(_ > 0)
  }

  def addUserWithAddress(userWithAddress: UserWithAddress): Future[UserWithAddress] = {
    val user = userWithAddress.user
    val address = userWithAddress.address

    val query = for {
        addressId <- addresses returning addresses.map(_.id) += address
        userId    <- users     returning users.map(_.id)     += user.copy(addressId = Some(addressId))
    } yield UserWithAddress(user.copy(id = Some(userId)), address.copy(id = Some(addressId)))
    query.transactionally
    db.run(query)
  }

  def getAllUsersWithAddress: Future[Seq[UserWithAddress]] = {
    val query = for {
      (user, address) <- users join addresses on (_.addressId === _.id)
    } yield (user, address)
    db.run(query.result).map(_.map(UserWithAddress.tupled))
  }

  def getUserWithAddressById(id: Long): Future[Option[UserWithAddress]] = {
    val query = for {
      user <- users if user.id === id
      address <- user.address
    } yield (user, address)
    db.run(query.result.headOption).map(_.map(UserWithAddress.tupled))
  }

}
