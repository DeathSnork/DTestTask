package DINS.TestTask

import DINS.TestTask.data.db.{DB, DataBaseSchema, InitialData}
import DINS.TestTask.server.HttpServer
import DINS.TestTask.service.{DBService, HttpService}

import scala.concurrent.duration.Duration
import scala.concurrent.Await


/**
 * The main application to run
 */
object Main extends App with  HttpService with HttpServer with DataBaseSchema with InitialData with DB {

  implicit lazy val dbService: DBService = new DBService

  /**
   * Drop and create DB Scheme
   */
  dbService.dropDB()
  dbService.initDB()


  /**
   * Inserting initial data to table
   */
  Await.result(insertInitialData(), Duration.Inf)
}
