package DINS.TestTask.config

import com.typesafe.config.ConfigFactory


trait Config {
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")

  lazy val httpHost: String = httpConfig.getString("interface")
  lazy val httpPort: Int = httpConfig.getInt("port")
}
