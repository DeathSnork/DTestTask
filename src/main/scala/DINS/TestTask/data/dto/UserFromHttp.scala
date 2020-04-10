package DINS.TestTask.data.dto

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User, UserWithAddress}

/**
 * User DTO from Http requests
 */
case class UserFromHttp(firstName: String,
                        lastName: String,
                        DOB: LocalDate,
                        address: Address) {

  /**
   * Convert this DTO to User entity without addressId
   */
  def toUser: User = User(None, firstName, lastName, DOB, None)

  /**
   * Convert this DTO to User entity with addressId
   */
  def toUser(addressId: Long): User = User(None, firstName, lastName, DOB, Some(addressId))

  /**
   * Convert this DTO to UserWithAddress entity
   */
  def toUserWithAddress: UserWithAddress = UserWithAddress(this.toUser, address)
}
