package DINS.TestTask.data.model

/**
 * Address Entity for DB
 */
case class Address(id: Option[Long] = None,
                   street: String,
                   city: String,
                   stateOrProvince: String,
                   postalCode: String,
                   country: String)
