package com.adrianrafo.seed.client.process.runtime

import cats.effect.IO
import com.adrianrafo.seed.client.common.models.Person
import com.adrianrafo.seed.client.process.PeopleServiceClient
import com.adrianrafo.seed.client.process.PeopleServiceClient.serviceName
import com.adrianrafo.seed.server.protocol._
import io.chrisdavenport.log4cats.Logger

class PeopleServiceClientImpl(client: PeopleService[IO])(implicit L: Logger[IO])
    extends PeopleServiceClient[IO] {

  private def personFromRPC(rpcPerson: PersonRPC): Person = Person(rpcPerson.name, rpcPerson.age)

  def getPerson(name: Option[String]): IO[Option[Person]] = ???

}

object PeopleServiceClientImpl {
  def apply(client: PeopleService[IO])(implicit L: Logger[IO]): PeopleServiceClientImpl =
    new PeopleServiceClientImpl(client)
}
