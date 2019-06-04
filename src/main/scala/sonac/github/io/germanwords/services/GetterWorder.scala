package sonac.github.io.germanwords.services

import scala.util.Random
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
import sonac.github.io.germanwords.common.Article
import sonac.github.io.germanwords.config.YandexConf
import org.http4s.Uri
import org.http4s.Credentials

class GetterWorder[F[_]](config: YandexConf, client: Client[IO]) {

  val engWords: List[String] = List("i", "will", "remove", "this", "later")

  case class TotalWord(english: String, german: String, article: String)

  implicit val decoder = jsonOf[IO, TotalWord]

  val key: String = config.key

  def getWord(word: String): IO[String] = {
    val url: String =
      "https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" +
        key + "&lang=en-de&text=" + word
    client.expect[String](Uri.unsafeFromString(url))
  }

  def getRandomWord: IO[String] = {
    val resp = getWord(Random.shuffle(engWords).head)
    resp.map { r =>
      val json: Json = parse(r).getOrElse(Json.Null)
      val cursor: HCursor = json.hcursor
      cursor
        .downField("def")
        .downArray
        .first
        .downField("tr")
        .downArray
        .first
        .downField("text")
        .as[String]
        .getOrElse("none")
    }
  }

}
