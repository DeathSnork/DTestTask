package DINS.TestTask.util

import java.time.{LocalDate, LocalDateTime}
import java.time.format.DateTimeFormatter

import DINS.TestTask.data.dto.{AddressFromHttp, UserFromHttp, UserToHttp}
import DINS.TestTask.data.model.{Address, User, UserWithAddress}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.{Marshaller, Marshalling}
import akka.http.scaladsl.unmarshalling.Unmarshaller
import spray.json.{DefaultJsonProtocol, DeserializationException, JsNumber, JsString, JsValue, JsonFormat, RootJsonFormat}

import scala.util.{Failure, Success, Try}
import scala.util.Try._

trait JsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit object LocalDateJsonFormat extends RootJsonFormat[LocalDate] {
    def write(localDate: LocalDate) = JsString(localDate.toString)

    def read(value: JsValue): LocalDate = value match {
      case JsString(string: String) => {
        Try {
          val arr = string.split("-").map(_.toInt)
          LocalDate.of(arr(0), arr(1), arr(2))
        } match {
          case Success(value) => value
          case Failure(e) => throw DeserializationException("Date expected in format: yyyy-mm-dd")
        }
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
