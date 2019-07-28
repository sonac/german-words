package io.github.sonac.germanwords.db

import org.scalatest.WordSpec
import sonac.github.io.germanwords.db.WordsRepository
import sonac.github.io.germanwords.model.`package`.User

class WordsRepositorySpec extends WordSpec {

  "words repo" should {
    "return word from json" in {
      val words = WordsRepository.getWords

      // There is only one word in test words.json
      assert(words.unsafeRunSync().forall(_.word == "account"))
    }
    "return all users from json" in {
      val users: Seq[User] = WordsRepository.getUsers.unsafeRunSync()

      assert(users.forall(_.username.startsWith("sonac")))
      assert(users.length == 11)
    }
    "return top ten users from json" in {
      val users: Seq[User] = WordsRepository.getTopTenUsers.unsafeRunSync()

      assert(users.forall(_.username.startsWith("sonac")))
      assert(users.head.username == "sonac5")
      assert(users.last.bestResult == -1)
    }
    "return expected user from json" in {
      val user: Option[User] =
        WordsRepository.getUserByName("sonac10").unsafeRunSync()

      assert(user.get.username == "sonac10")
      assert(user.get.bestResult == -10)
    }
  }

}
