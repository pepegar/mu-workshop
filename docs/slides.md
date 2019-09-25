# mu-workshop

---

## Who are we?

@adrianrafo Senior Software Engineer @ 47Degrees
@pepegar Tech Lead @ 47Degrees

---

## Brief introduction to RPC

RPC, stands for remote procedure call, and it's a way of communicating
services.


## What's an IDL?

IDL stands for **Interface Definition Language**.  IDLs are used to
declare communication protocols, and they commonly allow users to
declare complex types and messages.


## What is Avro?

Avro is an **Interface Definition Language** widely used in the data
engineering world.


## What is mu?

**mu** is an opensource library by your friendly folks @ 47Degrees.
It's located in github and opensource, check it out
https://github.com/higherkindness/mu

It is a scala library (in other languages soon) to do RPC in a purely
functional fashion.


## How does mu work?

**mu** abstracts over **gRPC** framework to create purely functional
clients and servers.


## What does mu provide?

mu allows you to:

- Create clients
- Create servers
- Generate Scala code given an IDL (Avro|OpenApi|Protobuf)

---

## Creating the protocol

The first thing we will need to do is to create the protocol.  Today
we will use Avro as our IDL, but we can use Protobuf or Openapi as
well.

## Avro protocol


## Configure SBT

- Show a graph with module dependencies
- add settings for source generation
- show specific dependencies for each module


## Defining the protocol

First of all we will need to have the following dependencies in the
`build.sbt` file.

```scala
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "@MU_VERSION@"
```


## the protocol

For this workshop we will use Avro with the AVDL language.  All AVDL
files should have one protocol definition.

```avdl
protocol ProtocolName {
  // all our declarations
}
```


## Defining records

Avro records represent product types, like case classes, and we
declare them with the `record` keyword:

```avdl
record Person {
  string name;
  int age;
}
```

## Defining unions

Unions in Avro represent sum types, like eithers:

```avdl
record PeopleResponse {
  union{ Person, NotFoundError, DuplicatedPersonError } result;
}
```


## AVDL documentation

TODO: add link to docu (and QR)


## The models protocol: TODO

use `sbt groll next` to go to the first exercise, and put this
protocol in the file
`server/modules/protocol/src/main/resources/People.avdl`.

```avdl
@namespace("com.adrianrafo.seed.server.protocol")
protocol People {

  record Person {
    string name;
    int age;
  }

  record NotFoundError{
    string message;
  }

  record DuplicatedPersonError{
    string message;
  }

  record PeopleRequest {
    string name;
  }

  record PeopleResponse {
    union{ Person, NotFoundError, DuplicatedPersonError } result;
  }

}
```


## the service protocol : TODO


## Creating the server from the protocol

serverprocess (servicio tagless) & serverapp (mu service)

TODO: recommended IDL -> scala,


In order to generate scal  sources from IDLs, we will use `sbt-mu-idlgen`.

```scala
addSbtPlugin("io.higherkindness" %% "sbt-mu-idlgen" % "@MU_VERSION@")
```


## Configuring SBT

We will need some sbt configuration in order to make this codegen
work.  Let's start checking out the correct tag:

```
sbt groll TODO
```


## Configuring SBT

we start by adding the needed import to our `build.sbt` file.

```scala
import higherkindness.mu.rpc.idlgen.IdlGenPlugin.autoImport._
```


## Configuring SBT

then, we need to tell `idlGen` the IDL language we're using and also
telling it we will be generating sources when compiling (as scalaxb
does, if you've used it...).

```scala
idlType := avro
srcGenSerializationType := Avro
srcGen on compile ..... TODO
```

(those are just the mandatory settings, but you can find a lot more
here: https://higherkindness.io/mu/generate-sources-from-idl)


## Executing the generation

Now that SBT is configured we can use `sbt idlGen` () task to make it
generate our scala sources from the IDL.


## Creating the client from the protocol

clientprocess (tagless) & clientapp

## Connect to each other


## Other IDLs


## Protobuf
