package DINS.TestTask

import DINS.TestTask.data.db.{DB, DataBaseSchema, InitialData}
import DINS.TestTask.server.HttpServer
import DINS.TestTask.service.{DBService, HttpService}

import scala.concurrent.duration.Duration
import scala.concurrent.Await

object Main extends App with  HttpService with HttpServer with DataBaseSchema with InitialData with DB {

  implicit lazy val dbService: DBService = new DBService

  dbService.dropDB()
  dbService.initDB()

  Await.result(insertInitialData(), Duration.Inf)
}
