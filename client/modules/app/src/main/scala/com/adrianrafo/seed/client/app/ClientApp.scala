package com.adrianrafo.seed.client.app

import cats.effect._

object ClientApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = new ClientProgram().runProgram(args).use(IO.pure)
}
