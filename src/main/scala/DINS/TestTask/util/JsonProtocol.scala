package DINS.TestTask.util

import java.time.LocalDate
import DINS.TestTask.data.dto.{AddressFromHttp, UserFromHttp, UserToHttp}
import DINS.TestTask.data.model.{Address, User, UserWithAddress}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

import scala.util.{Failure, Success, Try}

trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  /**
   * Json Converter for LocalDate
   */
  implicit object LocalDateJsonFormat extends RootJsonFormat[LocalDate] {
    def write(localDate: LocalDate): JsString = JsString(localDate.toString)

    def read(value: JsValue): LocalDate = value match {
      case JsString(string: String) =>
        Try {
          val arr = string.split("-").map(_.toInt)
          LocalDate.of(arr(0), arr(1), arr(2))
        } match {
          case Success(value) => value
          case Failure(_) => throw DeserializationException("Date expected in format: yyyy-mm-dd")
        }
      case _ => throw DeserializationException("Date expected in format: yyyy-mm-dd")
    }
  }



  implicit val addressFormat: RootJsonFormat[Address] = jsonFormat6(Address)
  implicit val addressDtoFormat: RootJsonFormat[AddressFromHttp] = jsonFormat5(AddressFromHttp)


  implicit val userFormat: RootJsonFormat[User] = jsonFormat5(User)
  implicit val userDtoFormat: RootJsonFormat[UserFromHttp] = jsonFormat4(UserFromHttp)

  implicit val userDtoWithIdFormat: RootJsonFormat[UserToHttp] = jsonFormat5(UserToHttp.tupled)

  implicit val userWithAddressFormat: RootJsonFormat[UserWithAddress] = jsonFormat2(UserWithAddress)
}
