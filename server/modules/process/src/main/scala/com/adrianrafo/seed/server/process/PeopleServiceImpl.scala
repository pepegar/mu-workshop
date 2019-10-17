package com.adrianrafo.seed.server
package process

import cats.effect.IO
import cats.implicits._
import com.adrianrafo.seed.server.protocol._
import io.chrisdavenport.log4cats.Logger

class PeopleServiceImpl(implicit L: Logger[IO]) extends PeopleService[IO] {

  val serviceName = "PeopleService"

  val people: List[PersonRPC] = List(
    PersonRPC("Foo", 10),
    PersonRPC("Bar", 20),
    PersonRPC("Bar", 10)
  )

  def getPerson(request: PeopleRequestRPC): IO[PeopleResponseRPC] = {
    def findPerson(request: Option[String]): Either[PeopleErrorRPC, PersonRPC] = ???

    val response = PeopleResponseRPC(findPerson(request.name))
    L.info(s"$serviceName - Request: $request - Response: $response").as(response)
  }

}
