package com.adrianrafo.seed.client
package process

import java.net.InetAddress

import cats.effect._
import com.adrianrafo.seed.client.common.models.Person
import com.adrianrafo.seed.client.process.runtime.PeopleServiceClientImpl
import com.adrianrafo.seed.server.protocol._
import higherkindness.mu.rpc.ChannelForAddress
import higherkindness.mu.rpc.channel._
import io.chrisdavenport.log4cats.Logger
import io.grpc.{CallOptions, ManagedChannel}

trait PeopleServiceClient[F[_]] {

  def getPerson(name: Option[String]): F[Option[Person]]

}
object PeopleServiceClient {

  val serviceName = "PeopleClient"

  def createClient(
      hostname: String,
      port: Int
  )(implicit CE: ConcurrentEffect[IO], CS: ContextShift[IO], L: Logger[IO]): Resource[IO, PeopleServiceClient[IO]] = {

    val channel: IO[ManagedChannel] =
      IO(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
        val channelFor    = ChannelForAddress(ip, port)
        val channelConfig = List(UsePlaintext()) //no SSL
        new ManagedChannelInterpreter[IO](channelFor, channelConfig).build
      }

    def clientFromChannel: Resource[IO, PeopleService[IO]] =
      PeopleService.clientFromChannel(channel, CallOptions.DEFAULT)

    clientFromChannel.map(PeopleServiceClientImpl(_))
  }

}
