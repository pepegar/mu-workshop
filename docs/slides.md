# mu-workshop

## Who are we?

@adrianrafo Senior Software Engineer @ 47Degrees
@pepegar Tech Lead @ 47Degrees

#

## Brief introduction to RPC

RPC, stands for remote procedure call, and it's a way of communicating
services.

#

## What's an IDL?

#

## What is Avro?

Avro is an Interface Definition Language widely used in the data
engineering world.

#

## What is mu?

mu is an opensource library by your friendly folks @ 47Degrees.  It's
located in github and opensource, check it out https://github.com/higherkindness/mu-scala

## How does mu work?

mu provides the ability to combine RPC protocols, services, and clients in your Scala program, thanks to gRPC. Although it’s fully integrated with gRPC, there are some important differences when defining the protocols, as we’ll see later on.

## What does mu provide?

#

## Configure SBT

- Show a graph with module dependencies
- add settings for source generation
- show specific dependencies for each module

## Defining the protocol

```scala
libraryDependencies += "io.higherkindness" %% "mu-rpc-channel" % "@MU_VERSION@"
```

#

## Creating the server from the protocol

## Creating the client from the protocol

## Connect to each other

## Other IDLs

## Protobuf
