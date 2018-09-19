package com.adrianrafo.seed.server
package common

object models {

  case class ServerConfig(name: String, host: String, port: Int)

  case class SeedServerConfig(server: ServerConfig)
}
