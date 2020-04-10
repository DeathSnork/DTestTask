package DINS.TestTask.data.dto

/**
 * Address DTO from Http requests
 */
case class AddressFromHttp(street: String,
                           city: String,
                           stateOrProvince: String,
                           postalCode: String,
                           country: String)
