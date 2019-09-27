package com.adrianrafo.seed.server.app

import cats.effect._

object ServerApp extends IOApp {
  def run(args: List[String]): IO[ExitCode] = new ServerProgram().runProgram(args)
}