package DINS.TestTask

import DINS.TestTask.data.db.{DataBaseSchema, InitialData}
import DINS.TestTask.data.persistance.DB
import DINS.TestTask.server.HttpServer
import DINS.TestTask.service.{DBService, HttpService}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object Main extends App with  HttpService with HttpServer with DataBaseSchema with InitialData with DB {
//  implicit lazy val system: ActorSystem = ActorSystem()
//  implicit lazy val materializer: Materializer = ActorMaterializer()
//  implicit lazy val ec: ExecutionContext = system.dispatcher


  implicit lazy val dbService = new DBService

  dbService.dropDB()
  dbService.initDB()

  Await.result(insertInitialData(), Duration.Inf)

}
