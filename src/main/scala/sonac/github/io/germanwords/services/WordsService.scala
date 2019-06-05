package sonac.github.io.germanwords.services

import cats.data.Kleisli
import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}
import org.http4s.circe._
import org.http4s.client.Client
import io.circe.generic.auto._
import io.circe.syntax._
import doobie.util.transactor.Transactor
import sonac.github.io.germanwords.config.YandexConf
import sonac.github.io.germanwords.db.WordsRepository

class WordsService(
    config: YandexConf,
    client: Client[IO],
    repo: WordsRepository[IO]
) extends Http4sDsl[IO] {
  import org.http4s.implicits._

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case GET -> Root / "healthcheck" =>
        Ok("oh hi mark")
      case GET -> Root / "word" =>
        Ok(
          new GetterWorder(config, client, repo).getRandomWord
        )
    }
    .orNotFound

}
