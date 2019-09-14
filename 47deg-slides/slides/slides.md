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

```scala
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "0.18.0"
```


## Creating the server from the protocol


## Creating the client from the protocol


## Connect to each other


## Other IDLs


## Protobuf

