package sonac.github.io.germanwords.db

import scala.io.Source
import cats.effect.IO
import sonac.github.io.germanwords.model.Word
import io.circe._
import io.circe.generic.semiauto._
import io.circe.parser._

object WordsRepository {

  implicit val decodeWord: Decoder[Word] = deriveDecoder[Word]

  def getWords: IO[Option[Seq[Word]]] = {
    val json = Source.fromResource("words.json").getLines().mkString
    IO {
      parse(json)
        .getOrElse(Json.Null)
        .hcursor
        .downField("words")
        .as[Seq[String]]
        .toOption
        .map(s => s.map(Word(_)))
    }
  }

}
