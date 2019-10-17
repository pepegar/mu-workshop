package com.adrianrafo.seed.client
package common

object models {

  case class ClientConfig(name: String, port: Int)

  case class ParamsConfig(host:String, request: Option[String])

  case class SeedClientConfig(client: ClientConfig, params: ParamsConfig)

  case class Person(name: String, age: Int)

}
