package com.adrianrafo.seed.client.process

import java.net.InetAddress

import cats.effect.{Effect, Timer}
import cats.syntax.flatMap._
import cats.syntax.functor._
import freestyle.rpc.ChannelForAddress
import freestyle.rpc.client.{ManagedChannelInterpreter, UsePlaintext}
import freestyle.rpc.client.cache.ClientCache
import freestyle.rpc.client.cache.ClientCache.HostPort
import io.grpc.ManagedChannel
import monix.execution.Scheduler

import scala.concurrent.duration.FiniteDuration

object ClientRPC {

  def clientCache[F[_], Client[_[_]]](
      hostAndPort: F[HostPort],
      sslEnabled: Boolean,
      tryToRemoveUnusedEvery: FiniteDuration,
      removeUnusedAfter: FiniteDuration,
      fromChannel: ManagedChannel => Client[F])(
      implicit F: Effect[F],
      TM: Timer[F],
      S: Scheduler): fs2.Stream[F, ClientCache[Client, F]] = {

    def serviceClient(hostname: String, port: Int): F[(Client[F], F[Unit])] = {

      def service(ip: String): F[(Client[F], F[Unit])] = {
        val channelFor    = ChannelForAddress(ip, port)
        val channelConfig = if (!sslEnabled) List(UsePlaintext()) else Nil
        F.delay(
            new ManagedChannelInterpreter[F](channelFor, channelConfig)
              .build(channelFor, channelConfig))
          .map(channel => (fromChannel(channel), F.delay(channel.shutdown).void))
      }

      F.delay(InetAddress.getByName(hostname).getHostAddress).flatMap(service)
    }

    ClientCache
      .impl[Client, F](
        hostAndPort,
        Function.tupled(serviceClient),
        tryToRemoveUnusedEvery,
        removeUnusedAfter
      )
  }

}
