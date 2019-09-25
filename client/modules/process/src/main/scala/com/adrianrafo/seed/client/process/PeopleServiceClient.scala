package com.adrianrafo.seed.client
package process

import java.net.InetAddress

import cats.effect._
import cats.implicits._
import com.adrianrafo.seed.client.common.models.Person
import com.adrianrafo.seed.client.process.runtime.PeopleServiceClientHandler
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

  def createClient[F[_]: ContextShift: Logger](
      hostname: String,
      port: Int
  )(implicit F: ConcurrentEffect[F]): Resource[F, PeopleServiceClient[F]] = {

    val channel: F[ManagedChannel] =
      F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
        val channelFor    = ChannelForAddress(ip, port)
        val channelConfig = List(UsePlaintext()) //no SSL
        new ManagedChannelInterpreter[F](channelFor, channelConfig).build
      }

    def clientFromChannel: Resource[F, PeopleService[F]] =
      PeopleService.clientFromChannel(channel, CallOptions.DEFAULT)

    clientFromChannel.map(PeopleServiceClientHandler(_))
  }

}
