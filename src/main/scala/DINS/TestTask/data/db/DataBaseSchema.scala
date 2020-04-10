package DINS.TestTask.data.db

import DINS.TestTask.data.model.{Address, User}
import java.time.LocalDate

import slick.ast.BaseTypedType
import slick.jdbc.JdbcType
import slick.lifted.{ForeignKeyQuery, ProvenShape}

/**
 * Schemas definition for User and Address
 */
trait DataBaseSchema extends DB { this: DB =>

  import driver.api._

  class Addresses(tag: Tag) extends Table[Address](tag, "ADDRESSES") {
    def id:               Rep[Long]   = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def street:           Rep[String] = column[String]("STREET")
    def city:             Rep[String] = column[String]("CITY")
    def stateOrProvince:  Rep[String] = column[String]("STATE_OR_PROVINCE")
    def postalCode:       Rep[String] = column[String]("POSTAL_CODE")
    def country:          Rep[String] = column[String]("COUNTRY")

    override def * : ProvenShape[Address] =
      (id.?, street, city, stateOrProvince, postalCode, country) <> (Address.tupled, Address.unapply)
  }

  val addresses = TableQuery[Addresses]

  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    /**
     * LocalDate Converter for DOB column
     */
    implicit val LocalDateFormatter: JdbcType[LocalDate] with BaseTypedType[LocalDate] =
      MappedColumnType.base[LocalDate, String](
      localDate => localDate.toString,
      string    => {
        val arr = string.split("-").map(_.toInt)
        LocalDate.of(arr(0), arr(1), arr(2))
      }
    )

    def id:         Rep[Long]         = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def firstName:  Rep[String]       = column[String]("FIRST_NAME")
    def lastName:   Rep[String]       = column[String]("LAST_NAME")
    def DOB:        Rep[LocalDate]    = column[LocalDate]("DOB")
    def addressId:  Rep[Option[Long]] = column[Option[Long]]("ADDRESS_ID")

    override def * : ProvenShape[User] =
      (id.?, firstName, lastName, DOB, addressId) <> (User.tupled, User.unapply)

    def address: ForeignKeyQuery[Addresses, Address] =
      foreignKey("ADDRESS_FK", addressId, addresses)(_.id, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }

  val users = TableQuery[Users]

  val mainSchema: driver.DDL = addresses.schema ++ users.schema

}
