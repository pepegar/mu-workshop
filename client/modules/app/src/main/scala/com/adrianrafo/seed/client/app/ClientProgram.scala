package com.adrianrafo.seed.client.app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import io.chrisdavenport.log4cats.Logger

class ClientProgram(implicit CE: ConcurrentEffect[IO], CS: ContextShift[IO]) extends ClientBoot {

  def clientProgram(config: SeedClientConfig)(implicit L: Logger[IO]): Resource[IO, ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.params.host, config.client.port)
      result       <- Resource.liftF(peopleClient.getPerson(config.params.request))
    } yield result.fold(ExitCode.Error)(_ => ExitCode.Success)
  }

}
