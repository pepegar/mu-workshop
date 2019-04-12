package com.adrianrafo.seed.client
package process

import java.net.InetAddress

import cats.effect._
import cats.syntax.flatMap._
import cats.syntax.functor._
import com.adrianrafo.seed.client.common.models.PeopleError
import com.adrianrafo.seed.client.process.runtime.handlers._
import com.adrianrafo.seed.server.protocol._
import higherkindness.mu.rpc.ChannelForAddress
import higherkindness.mu.rpc.channel.{ManagedChannelInterpreter, UsePlaintext}
import io.chrisdavenport.log4cats.Logger
import io.grpc.{CallOptions, ManagedChannel}

trait PeopleServiceClient[F[_]] {

  def getPerson(name: String): F[Either[PeopleError, Person]]

}
object PeopleServiceClient {

  val serviceName = "PeopleClient"

  def apply[F[_]: Effect](client: PeopleService[F])(implicit L: Logger[F]): PeopleServiceClient[F] =
    new PeopleServiceClient[F] {

      def getPerson(name: String): F[Either[PeopleError, Person]] =
        for {
          response <- client.getPerson(PeopleRequest(name))
          _ <- L.info(
            s"$serviceName - Request: $name - Result: ${response.result.map(PeopleResponseLogger).unify}")
        } yield response.result.map(PeopleResponseHandler).unify

    }

  def createClient[F[_]: ContextShift: Logger](
      hostname: String,
      port: Int,
      sslEnabled: Boolean = true)(
      implicit F: ConcurrentEffect[F]): fs2.Stream[F, PeopleServiceClient[F]] = {

    val channel: F[ManagedChannel] =
      F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
        val channelFor    = ChannelForAddress(ip, port)
        val channelConfig = if (!sslEnabled) List(UsePlaintext()) else Nil
        new ManagedChannelInterpreter[F](channelFor, channelConfig).build
      }

    def clientFromChannel: Resource[F, PeopleService[F]] =
      PeopleService.clientFromChannel(channel, CallOptions.DEFAULT)

    fs2.Stream.resource(clientFromChannel).map(PeopleServiceClient(_))
  }

}
