package DINS.TestTask.data.persistance

import slick.jdbc.JdbcProfile

/**
 * The final Postgres DB implementation which we can mixin
 */
trait DB {
  val driver: JdbcProfile = slick.jdbc.PostgresProfile

  import driver.api._

  lazy val db: Database = Database.forConfig("database")
}