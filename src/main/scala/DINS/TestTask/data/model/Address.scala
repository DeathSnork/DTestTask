package DINS.TestTask.data.model

case class Address(id: Option[Long] = None,
                   street: String,
                   city: String,
                   stateOrProvince: String,
                   postalCode: String,
                   country: String)
