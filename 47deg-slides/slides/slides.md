# Mu workshop

<section data-background="rgba(255,255,255,0.40)">
<!-- TODO: Esto queda más feo que el copón... hay que buscar otro color :) -->
<!-- TODO: Ok, con el fondo clarito va mejor... pero hay que verlo -->
<img src="https://higherkindness.io/img/mu-masthead-image.svg"/>
</section>

---

## Who are we?

<!-- TODO: Buscar las caricaturas, 47deg-mkt está chapao ahora... -->

**@adrianrafo** Senior Software Engineer @ 47Degrees

**@pepegar** Tech Lead @ 47Degrees

---

## Brief introduction to RPC

RPC, stands for **R**emote **P**rocedure **C**all, and it's a way of communicating
services.


## What's an IDL?

IDL stands for **I**nterface **D**efinition **L**anguage.  IDLs are used to
declare communication protocols, and they commonly allow users to
declare complex types and messages.


## What is Avro?

Avro is an **Interface Definition Language** widely used in the data
engineering world.


## What is Mu?

**Mu** is an RPC library by your friendly folks @ 47Degrees.

![Mu documentation](./img/qr-mu.png)


## What is Mu for?

It is a Scala library (in other languages soon) to do **RPC** in a purely
functional fashion.


## How does Mu work?

**Mu** abstracts over **gRPC** framework to create purely functional
clients and servers.


## What does Mu provide?

**Mu** allows you to:

- Create clients
- Create servers
- Generate Scala code given an **IDL** (Avro|OpenApi|Protobuf)

---

## Configuring SBT

We will need some sbt configuration in order to make this codegen
work.  Let's start checking out the correct tag:

```sh
sbt "groll initial"
```

And making sure everything works with

```sh
sbt clean compile
```


## Configuring SBT

we start by adding the needed import to our **SBT** config file.

TODO: dependencia en el plugin.sbt

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

![](./img/qr-mu-idlgen-docs.png)


## Mu modules

<!-- digraph G { --><!--     subgraph cluster_0 { --><!--         mu_common -> mu_rpc_internal_core; --><!--         label = "common"; --><!--     } --><!--     subgraph cluster_1 { --><!--         mu_rpc_channel; --><!--         mu_rpc_channel -> mu_common; --><!--         label = "transport"; --><!--     } --><!--     subgraph cluster_2 { --><!--         mu_rpc_netty -> mu_rpc_channel; --><!--         mu_rpc_okhttp -> mu_rpc_channel; --><!--         label = "client"; --><!--     } --><!--     subgraph cluster_3 { --><!--         mu_rpc_server -> { mu_common mu_rpc_channel }; --><!--         label = "server"; --><!--     } --><!--     subgraph cluster_4 { --><!--         mu_rpc_prometheus -> mu_rpc_internal_core; --><!--         mu_rpc_dropwizard -> mu_rpc_internal_core; --><!--         label = "other integrations"; --><!--     } --><!-- } -->

![modules graph](./img/modules-graph.png)

---

## Recommended way

In **Mu**, we advise users to go _**IDL** first_.  This means to
declare the **IDLs** by hand first and use them as the source of
truth.


## Creating the protocol

The first thing we will need to do is to create the protocol.  Today
we will use **Avro** as our **IDL**, but we can use **Protobuf** or
**Openapi** as well.


## Defining the protocol

First of all we will need to have the following dependencies in the
`build.sbt` file.

```scala
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "0.18.0"
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

Avro records represent product types, **like case classes**, and we
declare them with the `record` keyword:

```java
record Person {
  string name;
  int age;
}
```


## Defining unions

Unions in Avro represent sum types, **like Either**:

```java
record PeopleResponse {
  union{ Person, NotFoundError, DuplicatedPersonError } result;
}
```


## Defining RPC messages

We use java-like syntax for defining **RPC messages**.

```java
int sum(int a, int b = 0);
```


## AVDL documentation

https://avro.apache.org/docs/current/idl.html

![AVDL Documentation](./img/qr-avdl.png)

---

## Today's exercise

in today's exercise we will create a simple distributed application
that will allow us to get information from **Persons** in our system.
Think of it as a person directory.


## The models

<!-- Todo: too much info! -->

Create:

- a record for a **Person**, with name and age.
- a record for an error with persons.  It will need a message.
- a record for a **PersonRequest**.  We will need to ask for a
  particular person by name, or just get the first one.
- a record for a **PersonResponse** that will contain either a
  **Person** or a **PersonError**.


## the service protocol

Now that we have the models, we need to define the communication.

create a new protocol named **PeopleService** that imports
**People.avdl** and declares a message **getPerson** that:

- receives a **PeopleRequest** as request.
- returns a **PeopleResponse**.


## Dependencies

In order to generate scala  sources from IDLs, we will use `sbt-mu-idlgen`.

```scala
addSbtPlugin("io.higherkindness" %% "sbt-mu-idlgen" % "0.18.0")
```


## Executing the generation

Now that SBT is configured we can use `sbt idlGen` () task to make it
generate our Scala sources from the IDL.


---

## Reviewing the generated code

- In the **People.scala** we'll have all the models.
- protocol **PeopleService.scala** we'll have a tagless final algebra
  defining our interface.


## Creating the server from the protocol

there will be four main parts to the server module.

- **ServerProcess**
- **ServerBoot**
- **ServerProgram**
- **ServerApp**


## ServerProcess

In this class we will implement the service generated by
**`sbt-idlgen`**

```scala:mdoc
class PeopleServiceServer extends PeopleService[IO] {
  // our implementation will come here
}
```


## ServerBoot

To load dependencies and services required to start the server. Here
we'll place the server initialization.


## ServerProgram

We use this file to configure the **Mu** server.


## ServerApp

Just with the **IOApp** and the main method with only one line running
the **ServerProgram**.

---

## Creating the client from the protocol

- **ClientProcess**
- **ClientBoot**
- **ClientProgram**
- **ClientApp**


## ClientProcess

The implementation of the tagless algebra for the client.

```scala:mdoc
class PeopleServiceClient extends PeopleService[IO] {
  // our implementation will come here
}
```


## ClientBoot

To load dependencies and clients required to start the program.


## ClientProgram

With our client logic. In this file we'll use the clients implemented
on client-process.


## ClientApp

Just with the IOApp and the main method with only one line running the
ClientProgram.

---

## Connect to each other

Point your client to `http://172.168.1.222:8000` and send messages!

---

## Thanks!

TODO: add bibliografia
