package sonac.github.io.germanwords

package object model {
  case class Word(word: String)
  case class TotalWord(english: String, german: String, article: String)
}
