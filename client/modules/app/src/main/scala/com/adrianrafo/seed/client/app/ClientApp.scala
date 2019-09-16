package com.adrianrafo.seed.client.app

import cats.effect._
import cats.syntax.applicative._

object ClientApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    new ClientProgram[IO].runProgram(args).use(_.pure[IO])
}
