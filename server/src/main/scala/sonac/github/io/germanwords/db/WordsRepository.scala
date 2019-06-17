package sonac.github.io.germanwords.db

import cats.effect.Sync
import doobie.util.transactor.Transactor
import doobie._
import doobie.implicits._
import doobie.postgres.sqlstate
import sonac.github.io.germanwords.model.Word

class WordsRepository[F[_]: Sync](transactor: Transactor[F]) {

  def getWords: F[Seq[Word]] = {
    sql"select word from words.english_words"
      .query[Word]
      .to[Seq]
      .transact(transactor)
  }

}
