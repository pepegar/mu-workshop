# Mu workshop

<section data-background="rgba(255,255,255,0.40)">
<!-- TODO: Esto queda más feo que el copón... hay que buscar otro color :) -->
<!-- TODO: Ok, con el fondo clarito va mejor... pero hay que verlo -->
<img src="https://higherkindness.io/img/mu-masthead-image.svg"/>
</section>

---

## Who are we?

<!-- TODO: Buscar las caricaturas, 47deg-mkt está chapao ahora... -->

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


## What is Mu?

**Mu** is an RPC library by your friendly folks @ 47Degrees.

https://github.com/higherkindness/Mu

![Mu home](./img/qr-mu.png)


# What is Mu for?

It is a Scala library (in other languages soon) to do RPC in a purely
functional fashion.


## How does Mu work?

**Mu** abstracts over **gRPC** framework to create purely functional
clients and servers.


## What does Mu provide?

Mu allows you to:

- Create clients
- Create servers
- Generate Scala code given an IDL (Avro|OpenApi|Protobuf)

---

## Configuring SBT

We will need some sbt configuration in order to make this codegen
work.  Let's start checking out the correct tag:

```sh
sbt groll init
```


## Configuring SBT

we start by adding the needed import to our **`build.sbt`** file.

```scala
import higherkindness.Mu.rpc.idlgen.IdlGenPlugin.autoImport._
```


## Configuring SBT

We need to configure the `idlGen` plugin now.

```scala
idlType := avro
srcGenSerializationType := Avro
sourceGenerators in Compile += (srcGen in Compile).taskValue
```

(those are just the mandatory settings, but you can find a lot more
here: https://higherkindness.io/Mu/generate-sources-from-idl)


## Mu modules

<!-- digraph G { --><!--     subgraph cluster_1 { --><!--       mu_rpc_netty -> mu_rpc_channel; --><!--       mu_rpc_netty_ssl -> mu_rpc_netty; --><!--       label = "client"; --><!--     } -->

<!--     subgraph cluster_0 { --><!--       node [style=filled]; --><!--       mu_rpc_channel -> mu_config; --><!--       mu_rpc_channel -> mu_rpc_internal_core; --><!--       mu_rpc_monix -> mu_rpc_channel; --><!--       mu_rpc_fs2 -> mu_rpc_channel; --><!--       label = "transport"; --><!--       color = blue; --><!--     } -->

<!--     subgraph cluster_2 { --><!--       mu_rpc_server -> mu_rpc_internal_core; --><!--       mu_rpc_server -> mu_rpc_channel; --><!--       mu_rpc_server -> mu_rpc_monix; --><!--       mu_rpc_server -> mu_rpc_fs2; --><!--       label = "server"; --><!--     } --><!-- } -->

![modules graph](./img/modules-graph.png)

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
libraryDependencies += "io.higherkindness" %% "Mu-rpc-channel" % "0.18.0"
```


## the protocol

For this workshop we will use Avro with the AVDL language.  All AVDL
files should have one protocol definition.

```java
protocol ProtocolName {
  // all our declarations
}
```


## Defining records

Avro records represent product types, like case classes, and we
declare them with the `record` keyword:

```java
record Person {
  string name;
  int age;
}
```


## Defining unions

Unions in Avro represent sum types, like eithers:

```java
record PeopleResponse {
  union{ Person, NotFoundError, DuplicatedPersonError } result;
}
```


## AVDL documentation

https://avro.apache.org/docs/current/idl.html

![AVDL Documentation](./img/qr-avdl.png)


## The models protocol

use `sbt groll next` to go to the first exercise, and put this
protocol in the file
`server/modules/protocol/src/main/resources/People.avdl`.

```java
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

```java
@namespace("com.adrianrafo.seed.server.protocol")
protocol PeopleService {
  import idl "People.avdl";

  com.adrianrafo.seed.server.protocol.PeopleResponse getPerson(com.adrianrafo.seed.server.protocol.PeopleRequest request);

}
```

---

## Creating the server from the protocol

TODO: serverprocess (servicio tagless) & serverapp (Mu service)

In order to generate scala  sources from IDLs, we will use `sbt-Mu-idlgen`.

```scala
addSbtPlugin("io.higherkindness" %% "sbt-Mu-idlgen" % "0.18.0")
```


## Recommended way

in Mu, we advise users to go _IDL first_.  What we mean by that is to
declare their IDLs by hand first and use them as their source of
truth.


## Executing the generation

Now that SBT is configured we can use `sbt idlGen` () task to make it
generate our Scala sources from the IDL.

---

## Creating the client from the protocol

TODO: clientprocess (tagless) & clientapp

## Connect to each other


## Other IDLs


## Protobuf

