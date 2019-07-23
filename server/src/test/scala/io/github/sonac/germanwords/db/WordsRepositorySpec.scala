package io.github.sonac.germanwords.db

import org.scalatest.WordSpec
import sonac.github.io.germanwords.db.WordsRepository

class WordsRepositorySpec extends WordSpec {

  "words repo" should {
    "return word from json" in {
      val words = WordsRepository.getWords

      // There is only one word in test words.json
      assert(words.unsafeRunSync().get.forall(_.word == "account"))
    }
  }

}
