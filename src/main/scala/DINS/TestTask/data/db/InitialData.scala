package DINS.TestTask.data.db

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User}
import DINS.TestTask.data.persistance.DB

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

trait InitialData extends DataBaseSchema with DB{ this: DataBaseSchema =>

//  implicit def ex: ExecutionContext
  import driver.api._

  def insertInitialData(): Future[Unit] = {
    val setup = DBIO.seq(
      addresses += Address(Some(1), "5-avenu", "NY", "NY", "!@#!@#", "USA"),
      addresses += Address(Some(2), "test", "UK", "UK", "123", "UK"),

      users += User(Some(1), "test", "fromNY", LocalDate.of(1992, 11, 12), Some(1)),
      users += User(Some(4), "123", "UKASD", LocalDate.of(2002, 1, 3), Some(2))
    )
    db.run(setup).andThen {
      case Success(_) => println("Initial data inserted")
      case Failure(e) => println(s"Initial data not inserted: ${e.getMessage}")
    }
  }

}
