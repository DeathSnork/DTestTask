package DINS.TestTask.data.dto

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User, UserWithAddress}

case class UserFromHttp(firstName: String,
                        lastName: String,
                        DOB: LocalDate,
                        address: Address) {

  def toUser = User(None, firstName, lastName, DOB, None)

  def toUser(addressId: Long) = User(None, firstName, lastName, DOB, Some(addressId))

  def toUserWithAddress = UserWithAddress(this.toUser, address)

}
