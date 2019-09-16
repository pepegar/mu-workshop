package com.adrianrafo.seed.client.app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import io.chrisdavenport.log4cats.Logger

class ClientProgram[F[_]: ConcurrentEffect: ContextShift] extends ClientBoot[F] {

  def clientProgram(config: SeedClientConfig)(implicit L: Logger[F]): Resource[F, ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.client.host, config.client.port)
      result       <- Resource.liftF(peopleClient.getPerson(config.params.request))
    } yield result.fold(_ => ExitCode.Error, _ => ExitCode.Success)
  }

}


