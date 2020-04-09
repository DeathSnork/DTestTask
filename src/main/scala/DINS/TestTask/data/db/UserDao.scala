package DINS.TestTask.data.db

import DINS.TestTask.data.model.{Address, User, UserWithAddress}
import slick.jdbc.meta.MTable
import slick.sql.FixedSqlAction

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Success

import scala.concurrent.duration.Duration

class UserDao(implicit ex: ExecutionContext) extends DataBaseSchema {

  import driver.api._

  def createSchemaIfNotExists(): Future[Unit] = {
    db.run(MTable.getTables("ADDRESSES")).flatMap {
      case tables  =>
        println("Schema already exists" + tables)
        db.run((addresses.schema.create)).andThen {
          case Success(_) => println("addresses Schema created")
        }

      case tables if tables.nonEmpty =>
        println("Schema already exists" + tables)
        Future.successful()
    }
  }

  private val mainSchema = addresses.schema ++ users.schema

  def AddressSchemaCreation(): FixedSqlAction[Unit, NoStream, Effect.Schema] = addresses.schema.create
  def AddressSchemaDeletion(): FixedSqlAction[Unit, NoStream, Effect.Schema] = addresses.schema.drop
  def UserSchemaCreation(): FixedSqlAction[Unit, NoStream, Effect.Schema] = users.schema.create
  def UserSchemaDeletion(): FixedSqlAction[Unit, NoStream, Effect.Schema] = users.schema.drop

  def initUsers: Future[Unit] = db.run(users.schema.create)
  def dropUsers: Future[Unit] = db.run(users.schema.drop)
  def initAddresses: Future[Unit] = db.run(addresses.schema.create)
  def dropAddresses: Future[Unit] = db.run(addresses.schema.drop)

  def initDB(): Unit = {
    Await.result(db.run(addresses.schema.create), Duration.Inf)
    Await.result(db.run(users.schema.create), Duration.Inf)
  }

  def dropDB(): Unit = {
    Await.result(db.run(users.schema.drop), Duration.Inf)
    Await.result(db.run(addresses.schema.drop), Duration.Inf)
  }

  def addAddressAction(address: Address): DBIO[Address] = {
    addresses returning addresses.map(_.id) into ((address, id) => address.copy(id = Some(id))) += address
  }

  def updateAddressByIdAction(id: Long, address: Address): DBIO[Int] = {
    addresses.filter(_.id === id).update(address)
  }

  def addUserAction(user: User): DBIO[User] = {
    users returning users.map(_.id) into ((user, id) => user.copy(id = Some(id))) += user
  }

  def getAllUsers: Future[Seq[User]] = {
    val query = users.to[Seq].result
    db.run(query)
  }

  def getUserById(id: Long): Future[Option[User]] = {
    val query = users.filter(_.id === id).result.headOption
    db.run(query)
  }

  def updateUserByIdAction(id: Long, user: User): DBIO[Int] = {
    users.filter(_.id === id).update(user)
  }

  def deleteUserById(id: Long): Future[Boolean] = {
    val query = users.filter(_.id === id).delete.map(_ > 0)
    db.run(query)
  }

  def addUserWithAddress(userWithAddress: UserWithAddress): Future[UserWithAddress] = {
    val addressWithId = addAddressAction(userWithAddress.address)
    val userWithId = addUserAction(userWithAddress.user)
    val action = (addressWithId.zip(userWithId)).transactionally
    db.run(action).map(_.swap).map(UserWithAddress.tupled)
  }

  def addUserWithAddress2(userWithAddress: UserWithAddress): Future[UserWithAddress] = {
    val user = userWithAddress.user
    val address = userWithAddress.address

    val insertAddress = addresses returning addresses.map(_.id)
    val query = for {
        addressId <- insertAddress += address
        userId <- users returning users.map(_.id) += user.copy(addressId = Some(addressId))
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
