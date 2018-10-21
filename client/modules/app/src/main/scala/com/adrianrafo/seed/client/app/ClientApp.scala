package com.adrianrafo.seed.client
package app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import com.adrianrafo.seed.client.process.runtime.PeopleServiceClient
import fs2.{Stream, StreamApp}
import io.chrisdavenport.log4cats.Logger

import scala.language.postfixOps
import scala.concurrent.duration._

class ClientProgram[F[_]: Effect] extends ClientBoot[F] {

  def peopleServiceClient(host: String, port: Int)(
      implicit L: Logger[F]): Stream[F, PeopleServiceClient[F]] =
    PeopleServiceClient.createClient(
      host,
      port,
      sslEnabled = false,
      tryToRemoveUnusedEvery = 30 minutes,
      removeUnusedAfter = 1 hour)

  override def serverStream(config: SeedClientConfig)(
      implicit L: Logger[F]): Stream[F, StreamApp.ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.client.host, config.client.port)
      result       <- Stream.eval(peopleClient.getPerson(config.params.request))
    } yield result.fold(_ => StreamApp.ExitCode.Error, _ => StreamApp.ExitCode.Success)
  }
}

object ClientApp extends ClientProgram[IO]
