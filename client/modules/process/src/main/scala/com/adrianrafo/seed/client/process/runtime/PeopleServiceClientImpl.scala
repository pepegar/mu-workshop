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

  def getPerson(name: Option[String]): IO[Option[Person]] =
    for {
      response <- client.getPerson(PeopleRequestRPC(name))
      _ <- response.result.fold(
        err => L.error(s"$serviceName - Request: $name - Error: $err"),
        res => L.info(s"$serviceName - Request: $name - Result: $res")
      )
    } yield response.result.toOption.map(personFromRPC)

}

object PeopleServiceClientImpl {
  def apply(client: PeopleService[IO])(implicit L: Logger[IO]): PeopleServiceClientImpl =
    new PeopleServiceClientImpl(client)
}
