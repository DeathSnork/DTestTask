package DINS.TestTask.data.dto

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User, UserWithAddress}

case class UserDto(firstName: String,
                   lastName: String,
                   DOB: LocalDate,
                   address: Address) {

  def toUser = User(None, firstName, lastName, DOB, None)

  def toUserWithAddress = UserWithAddress(this.toUser, address)

}
