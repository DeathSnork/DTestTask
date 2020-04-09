package DINS.TestTask.data.dto

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User, UserWithAddress}

case class UserDtoWithId(id: Option[Long],
                         firstName: String,
                         lastName: String,
                         DOB: LocalDate,
                         address: Address)

object UserDtoWithId {
  def apply(id: Option[Long],
            firstName: String,
            lastName: String,
            DOB: LocalDate,
            address: Address): UserDtoWithId =
    new UserDtoWithId(id, firstName, lastName, DOB, address)

  def apply(user: User, address: Address): UserDtoWithId =
    apply(user.id, user.firstName, user.lastName, user.DOB, address)

  def apply(userWithAddress: UserWithAddress): UserDtoWithId =
    apply(userWithAddress.user, userWithAddress.address)

  def tupled: (Option[Long], String, String, LocalDate, Address) => UserDtoWithId = {
    case Tuple5(x1, x2, x3, x4, x5) => apply(x1, x2, x3, x4, x5)
  }
}
