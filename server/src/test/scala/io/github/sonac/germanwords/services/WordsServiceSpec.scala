package io.github.sonac.germanwords.services

import cats.effect._
import cats.implicits._
import java.io.IOException
import org.http4s.dsl.Http4sDsl
import org.http4s.client.Client
import org.scalatest.WordSpec
import org.scalatest.mockito.MockitoSugar.mock
import org.http4s.HttpApp
import org.http4s.Response
import org.http4s.Request
import sonac.github.io.germanwords.services.WordsService
import sonac.github.io.germanwords.config.YandexConf
import org.http4s.Method
import org.http4s.Uri
import cats.implicits._
// import cats.implicits._

import org.http4s.server.middleware.VirtualHost
import org.http4s.server.middleware.VirtualHost.exact

import io.circe._
// import io.circe._

import io.circe.syntax._
// import io.circe.syntax._

import io.circe.generic.semiauto._
// import io.circe.generic.semiauto._

import cats.effect._, org.http4s._, org.http4s.dsl.io._
// import cats.effect._
// import org.http4s._
// import org.http4s.dsl.io._

import org.http4s.circe._
// import org.http4s.circe._

import org.http4s.dsl.io._
import org.http4s.dsl.impl.Path
import org.scalatest.AsyncWordSpec
import org.http4s.blaze.http.HttpClient
import org.http4s.headers.Host

class WordsServiceSpec extends WordSpec with Http4sDsl[IO] {

  def check[A](
      actual: IO[Response[IO]],
      expectedStatus: Status,
      expectedBody: Option[A]
  )(
      implicit ev: EntityDecoder[IO, A]
  ): Boolean = {
    val actualResp = actual.unsafeRunSync
    val statusCheck = actualResp.status == expectedStatus
    val bodyCheck = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty
    )( // Verify Response's body is empty.
      expected => actualResp.as[A].unsafeRunSync == expected
    )
    statusCheck && bodyCheck
  }

  val responseJson = Json.obj(
    ("head", Json.Null),
    (
      "def",
      Json.arr(
        Json.obj(
          ("text", Json.fromString("account")),
          ("pos", Json.fromString("noun")),
          (
            "tr",
            Json.arr(
              Json.obj(
                ("text", Json.fromString("Konto")),
                ("gen", Json.fromString("n"))
              )
            )
          )
        )
      )
    )
  )
  val app: HttpApp[IO] = HttpApp[IO] {
    case req @ GET -> _ / "api" / "v1" / "dicservice.json" / "lookup" =>
      Response[IO](Ok)
        .withEntity(
          responseJson
        )
        .pure[IO]
    case r => {
      Response[IO](Ok).withEntity(r.body).pure[IO]
    }
  }
  val client = Client.fromHttpApp(app)
  val config = YandexConf("some_conf")

  "mock client" should {
    "do smth" in {
      assert(
        client
          .expect[String](Request[IO](POST).withEntity("foo"))
          .unsafeRunSync == "foo"
      )
    }
  }

  val wordService = new WordsService(config, client)

  "service" should {
    "return proper response for word endpoint" in {
      val response = wordService.service.run(
        Request(method = Method.GET, uri = Uri.uri("/word"))
      )
      val expectedJson = Json.obj(
        ("english", Json.fromString("account")),
        ("german", Json.fromString("Konto")),
        ("article", Json.fromString("das"))
      )
      assert(check[Json](response, Status.Ok, Some(expectedJson)))
    }
    "return proper response fpr healthcheck endpoint" in {
      val response = wordService.service.run(
        Request(method = Method.GET, uri = Uri.uri("/healthcheck"))
      )
      val expectedBody = "oh hi mark"
      assert(check[String](response, Status.Ok, Some(expectedBody)))
    }
    "fails on wrong method to healthcheck endpoint" in {
      val response = wordService.service.run(
        Request(method = Method.POST, uri = Uri.uri("/healthcheck"))
      )
      assert(check(response, Status.NotFound, Some("Not found")))
    }
  }
}
