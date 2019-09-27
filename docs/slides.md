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

https://github.com/higherkindness/Mu

<!-- ![](TODO: arreglar URL)-->


#  What is Mu for?

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

TODO: hacer QR aqui

(those are just the mandatory settings, but you can find a lot more
here: https://higherkindness.io/Mu/generate-sources-from-idl)


## Mu modules


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
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "@MU_VERSION@"
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


## AVDL documentation

https://avro.apache.org/docs/current/idl.html

![AVDL Documentation](./img/qr-avdl.png)


## The models protocol

Let's define the model of our protocol.

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


## the service protocol

And let's define the service protocol now.

TODO: describe the instructions

```java
@namespace("com.adrianrafo.seed.server.protocol")
protocol PeopleService {
  import idl "People.avdl";

  com.adrianrafo.seed.server.protocol.PeopleResponse getPerson(com.adrianrafo.seed.server.protocol.PeopleRequest request);

}
```


## Dependencies

In order to generate scala  sources from IDLs, we will use `sbt-mu-idlgen`.

```scala
addSbtPlugin("io.higherkindness" %% "sbt-mu-idlgen" % "@MU_VERSION@")
```


## Executing the generation

Now that SBT is configured we can use `sbt idlGen` () task to make it
generate our Scala sources from the IDL.


---

## Reviewing the generated code

TODO: go through all the generated code and comment it


## Creating the server from the protocol

there will be four main parts to the server module.

- **ServerProcess**
- **ServerBoot**
- **ServerProgram**
- **ServerApp**


## ServerProcess

TODO: Add some code sample here?

In this class we will implement the service generated by
**`sbt-idlgen`**


## ServerBoot

To load dependencies and services required to start the server. Here
we'll place the server initialization.


## ServerProgram

We use this file to configure the **Mu** server.


## ServerApp

Just with the IOApp and the main method with only one line running the
ServerProgram.

---

## Creating the client from the protocol

- **ClientProcess**
- **ClientBoot**
- **ClientProgram**
- **ClientApp**


## ClientProcess

TODO: add proper description


## ClientBoot

To load dependencies and clients required to start the program.


## ClientProgram

With our client logic. In this file we'll use the clients implemented on client-process.


## ClientApp

Just with the IOApp and the main method with only one line running the ClientProgram.

---

## Connect to each other

Point your client to `http://172.168.1.222:8000` and send messages!

---

## Thanks!

TODO: add bibliografia
