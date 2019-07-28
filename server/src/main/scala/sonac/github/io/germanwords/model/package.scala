package sonac.github.io.germanwords

import io.circe.generic.extras._

package object model {
  
  case class Word(word: String)
  case class TotalWord(english: String, german: String, article: String)
  @ConfiguredJsonCodec case class User(username: String, bestResult: Int)
  object User {
    implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
  }
}
