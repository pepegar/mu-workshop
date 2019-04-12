package com.adrianrafo.seed.server
package process

import cats.effect.Sync
import cats.syntax.functor._
import com.adrianrafo.seed.server.protocol._
import io.chrisdavenport.log4cats.Logger
import shapeless.{:+:, CNil, Coproduct}

class PeopleServiceHandler[F[_]: Sync](implicit L: Logger[F]) extends PeopleService[F] {

  val serviceName = "PeopleService"

  type PersonResult = Person :+: NotFoundError :+: DuplicatedPersonError :+: CNil

  val people: List[Person] = List(
    Person("Foo", 10),
    Person("Bar", 20),
    Person("Bar", 10)
  )

  def getPerson(request: PeopleRequest): F[PeopleResponse] = {
    def findPerson: PersonResult =
      people.count(_.name == request.name) match {
        case x if x < 2 =>
          people
            .find(_.name == request.name)
            .map(Coproduct[PersonResult](_))
            .getOrElse(Coproduct[PersonResult](NotFoundError(s"Person ${request.name} not found")))
        case _ =>
          Coproduct[PersonResult](DuplicatedPersonError(s"Person ${request.name} duplicated"))
      }

    L.info(s"$serviceName - Request: $request").as(PeopleResponse(findPerson))
  }

}
