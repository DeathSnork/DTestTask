package DINS.TestTask

import DINS.TestTask.data.db.{DB, DataBaseSchema, InitialData}
import DINS.TestTask.server.HttpServer
import DINS.TestTask.service.{DBService, HttpService}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object Main extends App with  HttpService with HttpServer with DataBaseSchema with InitialData with DB {

  implicit lazy val dbService = new DBService

  dbService.dropDB()
  dbService.initDB()

//  dbService.createSchemaIfNotExists()

  Await.result(insertInitialData(), Duration.Inf)

}
