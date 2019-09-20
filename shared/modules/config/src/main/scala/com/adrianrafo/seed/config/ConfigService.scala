package com.adrianrafo.seed
package config

import cats.effect.Effect
import pureconfig.{ConfigReader, Derivation}

trait ConfigService[F[_]] {

  def serviceConfig[Config](implicit reader: Derivation[ConfigReader[Config]]): F[Config]

}

object ConfigService {
  def apply[F[_]: Effect]: ConfigService[F] = new ConfigService[F] {

    override def serviceConfig[Config](
        implicit reader: Derivation[ConfigReader[Config]]
    ): F[Config] = Effect[F].delay(pureconfig.loadConfigOrThrow[Config])

  }
}
