package sonac.github.io.germanwords.db

import cats.effect.IO
import doobie.hikari.HikariTransactor
import org.flywaydb.core.Flyway

import sonac.github.io.germanwords.config.DatabaseConfig

object Database {

  def transactor(config: DatabaseConfig): IO[HikariTransactor[IO]] = {
    HikariTransactor.newHikariTransactor[IO](
      config.driver,
      config.url,
      config.user,
      config.password
    )
  }

  def initialize(transactor: HikariTransactor[IO]): IO[Unit] = {
    transactor.configure { datasource =>
      IO {
        val flyway = Flyway.configure().dataSource(datasource).load().baseline()
        ()
      }
    }
  }

}
