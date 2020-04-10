package DINS.TestTask.data.dto

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User, UserWithAddress}

case class UserToHttp(id: Option[Long],
                      firstName: String,
                      lastName: String,
                      DOB: LocalDate,
                      address: Address)

object UserToHttp {
  def apply(id: Option[Long],
            firstName: String,
            lastName: String,
            DOB: LocalDate,
            address: Address): UserToHttp =
    new UserToHttp(id, firstName, lastName, DOB, address)

  def apply(user: User, address: Address): UserToHttp =
    apply(user.id, user.firstName, user.lastName, user.DOB, address)

  def apply(userWithAddress: UserWithAddress): UserToHttp =
    apply(userWithAddress.user, userWithAddress.address)

  def tupled: (Option[Long], String, String, LocalDate, Address) => UserToHttp = {
    case Tuple5(x1, x2, x3, x4, x5) => apply(x1, x2, x3, x4, x5)
  }
}
