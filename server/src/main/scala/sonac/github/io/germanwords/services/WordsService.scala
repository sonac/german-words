package sonac.github.io.germanwords.services

import scala.concurrent.ExecutionContext

import java.io.File
import java.util.concurrent._

import cats.data.Kleisli
import cats.effect._
import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.circe._
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.server.staticcontent._
import io.circe.generic.auto._
import io.circe.syntax._
import sonac.github.io.germanwords.config.YandexConf
import sonac.github.io.germanwords.db.WordsRepository
import sonac.github.io.germanwords.model._
import sonac.github.io.germanwords.common.ExternalServiceFailure
import org.http4s.StaticFile

class WordsService(
    config: YandexConf,
    client: Client[IO]
) extends Http4sDsl[IO] {
  import org.http4s.implicits._

  implicit val totalWordDecoder: EntityDecoder[IO, TotalWord] =
    jsonOf[IO, TotalWord]
  implicit val totalWordEncoder: EntityEncoder[IO, TotalWord] =
    jsonEncoderOf[IO, TotalWord]

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val blockingEc =
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  val service: Kleisli[IO, Request[IO], Response[IO]] = HttpRoutes
    .of[IO] {
      case request @ GET -> Root =>
        StaticFile
          .fromFile[IO](
            new File("../public/index.html"),
            blockingEc,
            Some(request)
          )
          .map(_.putHeaders())
          .getOrElseF(Ok(System.getProperty("user.dir")))
      case request @ GET -> Root / "js" / jsFileName =>
        StaticFile
          .fromFile[IO](
            new File("../public/js/" + jsFileName),
            blockingEc,
            Some(request)
          )
          .map(_.putHeaders())
          .getOrElseF(Ok(System.getProperty("user.dir")))
      case request @ GET -> Root / "fonts" / fontFileName =>
        StaticFile
          .fromFile[IO](
            new File("../public/fonts/" + fontFileName),
            blockingEc,
            Some(request)
          )
          .map(_.putHeaders())
          .getOrElseF(Ok(System.getProperty("user.dir")))
      case GET -> Root / "healthcheck" =>
        Ok("oh hi mark")
      case GET -> Root / "word" =>
        new GetterWorder(config, client).getRandomWord().flatMap {
          case Right(word) => Ok(word)
          case Left(err)   => InternalServerError(err.message)
        }
    }
    .orNotFound

}
