package com.adrianrafo.seed.client
package process
package runtime

import cats.syntax.either._
import com.adrianrafo.seed.client.common.models.PeopleError
import com.adrianrafo.seed.server.protocol._
import shapeless.Poly1

object handlers {

  object PeopleResponseLogger extends Poly1 {
    implicit val peh1 = at[NotFoundError](_.message)
    implicit val peh2 = at[DuplicatedPersonError](_.message)
    implicit val peh3 = at[Person](_.toString)
  }

  object PeopleResponseHandler extends Poly1 {
    implicit val peh1 = at[NotFoundError](e => PeopleError(e.message).asLeft[Person])
    implicit val peh2 = at[DuplicatedPersonError](e => PeopleError(e.message).asLeft[Person])
    implicit val peh3 = at[Person](_.asRight[PeopleError])
  }
}
