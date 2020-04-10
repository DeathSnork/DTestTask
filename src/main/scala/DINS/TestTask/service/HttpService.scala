package DINS.TestTask.service

import DINS.TestTask.routes.UserRoutes
import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.{ActorMaterializer, Materializer}

import scala.concurrent.ExecutionContext


trait HttpService {
  implicit lazy val system: ActorSystem = ActorSystem()
  implicit lazy val materializer: Materializer = ActorMaterializer()
  implicit lazy val ec: ExecutionContext = system.dispatcher

  implicit def dbService: DBService

  lazy val routes: Route = new UserRoutes(dbService).routes
}
