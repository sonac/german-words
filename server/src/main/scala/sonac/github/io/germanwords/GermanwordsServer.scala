package sonac.github.io.germanwords

import cats.effect._
import cats.implicits._
import org.http4s.server.blaze._
import fs2.Stream
import config.Config
import org.http4s.client.blaze.BlazeClientBuilder

import sonac.github.io.germanwords.services.WordsService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object GermanwordsServer extends IOApp {

  def stream: Stream[IO, ExitCode] = {

    for {
      client <- BlazeClientBuilder[IO](global)
        .withResponseHeaderTimeout(Duration(100000, SECONDS))
        .stream
      config <- Stream.eval(Config.load())
      exitCode <- BlazeServerBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(
          new WordsService(
            config.yandex,
            client
          ).service
        )
        .serve
    } yield exitCode

  }

  def run(args: List[String]): IO[ExitCode] = {
    stream.compile.drain.as(ExitCode.Success)
  }

}
