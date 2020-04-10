package DINS.TestTask.server

import DINS.TestTask.config.Config
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer

import scala.concurrent.ExecutionContext

trait HttpServer extends Config{
  implicit def system: ActorSystem

  implicit def materializer: Materializer

  implicit def ec: ExecutionContext

  def routes: Route

  Http().bindAndHandle(routes, httpHost, httpPort)
}
