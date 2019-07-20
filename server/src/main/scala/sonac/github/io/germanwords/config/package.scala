package sonac.github.io.germanwords

import cats.effect.IO
import com.typesafe.config.ConfigFactory
import pureconfig.error.ConfigReaderException

package object config {
  case class ServerConfig(host: String, port: Int)

  case class YandexConf(key: String)

  case class Config(
      server: ServerConfig,
      yandex: YandexConf
  )

  object Config {
    import pureconfig._
    import pureconfig.generic.auto._

    def load(configFile: String = "application.conf"): IO[Config] = {
      IO {
        loadConfig[Config](ConfigFactory.load(configFile))
      }.flatMap {
        case Left(e) =>
          IO.raiseError[Config](new ConfigReaderException[Config](e))
        case Right(config) => IO.pure(config)
      }
    }
  }
}
