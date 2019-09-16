# Mu Workshop

---

## Who are we?

@adrianrafo Senior Engineer @47Degrees

@pepegar Tech Lead @47Degrees

---

## Brief introduction to RPC

RPC, stands for remote procedure call, and it's a way of communicating
services.

---

## What's an IDL?

IDL stands for **Interface Definition Language**.  IDLs are used to
declare communication protocols, and they commonly allow users to
declare complex types and messages.

---

## What is Avro?

Avro is an **IDL** widely used in the data engineering world created by the Apache Software Foundation.

---

## What is Mu?

**Mu** is an open source library by your friendly folks @47Degrees.
It's located in Github as open source, check it out
https://github.com/higherkindness/mu

It's a Scala library (in other languages soon) to do RPC in a purely
functional fashion style.

---

## How does Mu work?

**Mu** abstracts over **gRPC** framework to create purely functional
clients and servers on a more simple way.

---

## What does Mu provide?

**Mu** allows you to:

- Create generated clients from the server specification
- Create servers with a minimal amount of code
- Generate Scala protocol code given an IDL (Avro|OpenApi|Protobuf)

---

## Creating the protocol

The first thing we will need to do is to create the protocol. Today
we will use **Avro** as our IDL, but we can use **Protobuf** or **Openapi** as
well.

---

## Avro protocol

---

## Configure SBT

- Show a graph with module dependencies
- add settings for source generation
- show specific dependencies for each module

---

## Defining the protocol

```scala
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "0.18.4"
```

---

## Creating the server from the protocol

---

## Creating the client from the protocol

---

## Connect to each other

---

## Other IDLs

---

## Protobuf

---
