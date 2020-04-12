package DINS.TestTask.data.db

import java.time.LocalDate

import DINS.TestTask.data.model.{Address, User}

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

trait InitialData extends DataBaseSchema with DB{ this: DataBaseSchema =>

  import driver.api._

  def insertInitialData(): Future[Unit] = {
    val setup = DBIO.seq(
      addresses += Address(Some(1), "25 Guild Street Glen Aubrey", "NY", "NY", "NY 13777", "USA"),
      addresses += Address(Some(2), "30 Commercial Road", "PORTSMOUTH", "Fratton", "PO1 1AA", "Hampshire"),

      users += User(Some(1), "John", "Smith", LocalDate.of(1992, 11, 12), Some(1)),
      users += User(Some(4), "James", "Fitton", LocalDate.of(2002, 1, 3), Some(2))
    )
    db.run(setup).andThen {
      case Success(_) => println("Initial data inserted")
      case Failure(e) => println(s"Initial data not inserted: ${e.getMessage}")
    }
  }

}
