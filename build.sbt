
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / version := "0.1"
ThisBuild / organization := "DINS"

lazy val akkaHttpVersion  = "10.1.11"
lazy val akkaVersion      = "2.6.4"
lazy val slickVersion     = "3.2.3"

lazy val root = (project in file("."))
  .enablePlugins(AssemblyPlugin)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "TestTask",
    libraryDependencies ++= Seq(
      //      AkkaHttp
      "com.typesafe.akka"             %% "akka-stream"              % akkaVersion,
      "com.typesafe.akka"             %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka"             %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka"             %% "akka-http-spray-json"     % akkaHttpVersion,
      //      Logging
      "ch.qos.logback"                % "logback-classic"           % "1.2.3",

      //      Slick
      "com.typesafe.slick"            %% "slick"                    % slickVersion,
      "com.typesafe.slick"            %% "slick-hikaricp"           % slickVersion,

      //      Postgres JDBC Driver
      "org.postgresql"                % "postgresql"                % "9.4-1206-jdbc42"
  ),
    mainClass in (Compile, run) := Some("DINS.TestTask.Main"),
    mainClass in (assembly) := Some("DINS.TestTask.Main"),
    assemblyJarName in assembly := "DINSTestTask.jar"
  )