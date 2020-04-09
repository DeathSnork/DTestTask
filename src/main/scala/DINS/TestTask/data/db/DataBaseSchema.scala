package DINS.TestTask.data.db


import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User}
import DINS.TestTask.data.persistance.DB


//import slick.jdbc.PostgresProfile.api._

trait DataBaseSchema extends DB { this: DB =>

  import driver.api._

  class Addresses(tag: Tag) extends Table[Address](tag, "ADDRESSES") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def stateOrProvince = column[String]("STATE_OR_PROVINCE")
    def postalCode = column[String]("POSTAL_CODE")
    def country = column[String]("COUNTRY")

    def * =
      (id.?, street, city, stateOrProvince, postalCode, country) <> (Address.tupled, Address.unapply)
  }

  val addresses = TableQuery[Addresses]

  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    implicit val LocalDateFormatter = MappedColumnType.base[LocalDate, String](
      localDate => localDate.toString,
      string    => {
        val arr = string.split("-").map(_.toInt)
        LocalDate.of(arr(0), arr(1), arr(2))
      }
    )

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def firstName = column[String]("FIRST_NAME")
    def lastName = column[String]("LAST_NAME")
    def DOB = column[LocalDate]("DOB")
    def addressId = column[Option[Long]]("ADDRESS_ID")

    def * =
      (id.?, firstName, lastName, DOB, addressId) <> (User.tupled, User.unapply)

    def address =
      foreignKey("ADDRESS_FK", addressId, addresses)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  val users = TableQuery[Users]

}
