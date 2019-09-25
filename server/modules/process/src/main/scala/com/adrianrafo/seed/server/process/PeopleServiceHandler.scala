package com.adrianrafo.seed.server
package process

import cats.effect.Sync
import cats.implicits._
import com.adrianrafo.seed.server.protocol._
import io.chrisdavenport.log4cats.Logger

class PeopleServiceHandler[F[_]: Sync](implicit L: Logger[F]) extends PeopleService[F] {

  val serviceName = "PeopleService"

  val people: List[PersonRPC] = List(
    PersonRPC("Foo", 10),
    PersonRPC("Bar", 20),
    PersonRPC("Bar", 10)
  )

  def getPerson(request: PeopleRequestRPC): F[PeopleResponseRPC] = {
    def findPerson(nameRequest: String): Either[PeopleErrorRPC, PersonRPC] =
      people.count(_.name == nameRequest) match {
        case x if x < 2 =>
          people
            .find(_.name == nameRequest)
            .fold(PeopleErrorRPC(s"Person ${request.name} not found").asLeft[PersonRPC])(
              _.asRight[PeopleErrorRPC]
            )
        case _ =>
          PeopleErrorRPC(s"Person ${request.name} duplicated").asLeft[PersonRPC]
      }

    request.name.fold(PeopleResponseRPC(PersonRPC("", 0).asRight[PeopleErrorRPC]).pure[F]) { name =>
      val response = PeopleResponseRPC(findPerson(name))
      L.info(s"$serviceName - Request: $request - Response: $response").as(response)
    }
  }

}
