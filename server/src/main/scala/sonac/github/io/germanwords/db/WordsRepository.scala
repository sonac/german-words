package sonac.github.io.germanwords.db

import scala.io.Source
import scala.util.Try
import java.io._
import cats.effect.IO
import sonac.github.io.germanwords.model.Word
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import io.circe.generic.extras.Configuration
import io.circe.parser._
import sonac.github.io.germanwords.model.`package`.User

object WordsRepository {

  implicit val customConfig: Configuration =
    Configuration.default.withSnakeCaseMemberNames

  lazy val words = Source.fromResource("words.json").getLines().mkString
  lazy val users = Source.fromResource("users.json").getLines().mkString

  def getWords: IO[Seq[Word]] = {
    IO {
      parse(words)
        .getOrElse(Json.Null)
        .hcursor
        .downField("words")
        .as[Seq[String]]
        .toOption
        .get
        .map(Word(_))
    }
  }

  def getUsers: IO[Seq[User]] = {
    IO {
      parse(users)
        .getOrElse(Json.Null)
        .as[Seq[User]]
        .toOption
        .get
    }
  }

  def getTopTenUsers: IO[Seq[User]] = {
    getUsers.map(_.sortBy(-_.bestResult).take(10))
  }

  def getUserByName(username: String): IO[Option[User]] = {
    getUsers.map(_.filter(_.username == username).headOption)
  }

  def addUserRecord(user: User): IO[Try[User]] = {
    getUsers.map { s =>
      Try {
        val writerUsers =
          new PrintWriter(new File("src/main/resources/users.json"))
        writerUsers.write(
          (user :: s.filter(_.username != user.username).toList).asJson.toString
        )
        writerUsers.close()
        getUserByName(user.username).map(_.get).unsafeRunSync()
      }
    }
  }

}
