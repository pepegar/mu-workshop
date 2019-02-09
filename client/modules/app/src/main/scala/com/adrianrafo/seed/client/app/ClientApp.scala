package com.adrianrafo.seed.client
package app

import cats.effect._
import com.adrianrafo.seed.client.common.models._
import com.adrianrafo.seed.client.process.runtime.PeopleServiceClient
import fs2.Stream
import io.chrisdavenport.log4cats.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class ClientProgram[F[_]: Effect] extends ClientBoot[F] {

  def peopleServiceClient(host: String, port: Int)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, PeopleServiceClient[F]] =
    PeopleServiceClient.createClient(
      host,
      port,
      sslEnabled = false,
      tryToRemoveUnusedEvery = 30 minutes,
      removeUnusedAfter = 1 hour)

  def clientProgram(config: SeedClientConfig)(
      implicit L: Logger[F],
      TM: Timer[F],
      F: ConcurrentEffect[F]): Stream[F, ExitCode] = {
    for {
      peopleClient <- peopleServiceClient(config.client.host, config.client.port)
      result       <- Stream.eval(peopleClient.getPerson(config.params.request))
    } yield result.fold(_ => ExitCode.Error, _ => ExitCode.Success)
  }
}

object ClientApp extends ClientProgram[IO] with IOApp {
  implicit val ce: ConcurrentEffect[IO] = IO.ioConcurrentEffect
  def run(args: List[String]): IO[ExitCode] =
    program(args).compile.toList.map(_.headOption.getOrElse(ExitCode.Error))
}
