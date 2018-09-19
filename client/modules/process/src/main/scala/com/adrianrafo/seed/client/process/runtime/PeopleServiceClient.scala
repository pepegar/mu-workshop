package com.adrianrafo.seed.client
package process
package runtime

import cats.effect._
import cats.syntax.applicative._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.adrianrafo.seed.client.common.models.PeopleError
import com.adrianrafo.seed.client.process.runtime.handlers._
import com.adrianrafo.seed.server.protocol._
import io.grpc.{CallOptions, ManagedChannel}
import monix.execution.Scheduler
import io.chrisdavenport.log4cats.Logger

import scala.concurrent.duration.FiniteDuration

trait PeopleServiceClient[F[_]] {

  def getPerson(name: String): F[Either[PeopleError, Person]]

}
object PeopleServiceClient {

  val serviceName = "PeopleClient"

  def apply[F[_]: Effect](clientF: F[PeopleService.Client[F]])(
      implicit L: Logger[F]): PeopleServiceClient[F] =
    new PeopleServiceClient[F] {

      def getPerson(name: String): F[Either[PeopleError, Person]] =
        for {
          client   <- clientF
          _        <- L.info(s"$serviceName - Request: $name")
          response <- client.getPerson(PeopleRequest(name))
          _        <- L.info(s"$serviceName - Result: ${response.result.map(PeopleResponseLogger).unify}")
        } yield response.result.map(PeopleResponseHandler).unify

    }

  def createClient[F[_]](
      hostname: String,
      port: Int,
      sslEnabled: Boolean = true,
      tryToRemoveUnusedEvery: FiniteDuration,
      removeUnusedAfter: FiniteDuration)(
      implicit F: Effect[F],
      L: Logger[F],
      TM: Timer[F],
      S: Scheduler): fs2.Stream[F, PeopleServiceClient[F]] = {

    def fromChannel(channel: ManagedChannel): PeopleService.Client[F] =
      PeopleService.clientFromChannel(channel, CallOptions.DEFAULT)

    ClientRPC
      .clientCache(
        (hostname, port).pure[F],
        sslEnabled,
        tryToRemoveUnusedEvery,
        removeUnusedAfter,
        fromChannel)
      .map(cache => PeopleServiceClient(cache.getClient))
  }

}
