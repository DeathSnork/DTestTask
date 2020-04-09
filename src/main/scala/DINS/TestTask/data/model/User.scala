package DINS.TestTask.data.model

import java.time.LocalDate

case class User(id: Option[Long],
                firstName: String,
                lastName: String,
                DOB: LocalDate,
                addressId: Option[Long])
