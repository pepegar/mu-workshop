package com.adrianrafo.seed.client
package process

import java.net.InetAddress

import cats.effect._
import cats.syntax.flatMap._
import higherkindness.mu.rpc.ChannelForAddress
import higherkindness.mu.rpc.channel.cache.ClientCache
import higherkindness.mu.rpc.channel.cache.ClientCache.HostPort
import higherkindness.mu.rpc.channel.{ManagedChannelInterpreter, UsePlaintext}
import io.grpc.ManagedChannel

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.FiniteDuration

object ClientRPC {

  def clientCache[F[_], Client[_[_]]](
      hostAndPort: F[HostPort],
      sslEnabled: Boolean,
      tryToRemoveUnusedEvery: FiniteDuration,
      removeUnusedAfter: FiniteDuration,
      fromChannel: F[ManagedChannel] => Resource[F, Client[F]])(
      implicit F: ConcurrentEffect[F],
      TM: Timer[F],
      EC: ExecutionContext): fs2.Stream[F, ClientCache[Client, F]] = {

    def serviceClient(hostname: String, port: Int): Resource[F, Client[F]] = {

      val channel: F[ManagedChannel] =
        F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap { ip =>
          val channelFor    = ChannelForAddress(ip, port)
          val channelConfig = if (!sslEnabled) List(UsePlaintext()) else Nil
          new ManagedChannelInterpreter[F](channelFor, channelConfig).build
        }

      fromChannel(channel)
    }

    ClientCache
      .fromResource[Client, F](
        hostAndPort,
        Function.tupled(serviceClient),
        tryToRemoveUnusedEvery,
        removeUnusedAfter
      )
  }

}
