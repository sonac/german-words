package sonac.github.io.germanwords.services

import scala.util.Random
import scala.util.{Try, Failure, Success}

import cats.effect.IO
import org.http4s.client._
import org.http4s.client.dsl.io._
import org.http4s.headers._
import org.http4s.MediaType
import org.http4s.circe._
import org.http4s.Method._
import io.circe._
import io.circe.literal._
import io.circe.syntax._
import io.circe.parser._
import io.circe.generic.auto._
import org.http4s.Uri
import org.http4s.Credentials
import sonac.github.io.germanwords.common.Article
import sonac.github.io.germanwords.config.YandexConf
import sonac.github.io.germanwords.db.WordsRepository
import sonac.github.io.germanwords.model._

class GetterWorder[F[_]](
    config: YandexConf,
    client: Client[IO],
    repo: WordsRepository[IO]
) {

  val articleMap = Map("f" -> "die", "m" -> "der", "n" -> "das")

  val engWords: IO[Seq[Word]] = repo.getWords

  val key: String = config.key

  def getWord(word: String): IO[String] = {
    val url: String =
      "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" +
        key + "&lang=en-de&text=" + word
    println(url)
    client.expect[String](Uri.unsafeFromString(url))
  }

  def getRandomWord: IO[TotalWord] = {
    val resp = engWords.flatMap(s => getWord(Random.shuffle(s).head.word))
    resp.map { r =>
      val json: Json = parse(r).getOrElse(Json.Null)
      val cursor: HCursor = json.hcursor
      lazy val word = cursor
        .downField("def")
        .downArray
        .first
      TotalWord(
        word
          .downField("text")
          .as[String]
          .getOrElse(""),
        word
          .downField("tr")
          .downArray
          .first
          .downField("text")
          .as[String]
          .getOrElse(""),
        articleMap(
          word
            .downField("tr")
            .downArray
            .first
            .downField("gen")
            .as[String]
            .getOrElse("")
        )
      )
    }
  }

}
