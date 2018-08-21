package com.adrianrafo.seed.server.process

import cats.effect.Sync
import cats.syntax.functor._
import com.adrianrafo.seed.protocol._
import io.chrisdavenport.log4cats.Logger

class PeopleServiceHandler[F[_]: Sync](implicit L: Logger[F]) extends PeopleService[F] {

  val serviceName = "PeopleService"

  override def getPerson(request: PeopleRequest): F[PeopleResponse] =
    L.info(s"$serviceName - Request: $request").as(PeopleResponse(Person(request.name, 10)))

}
