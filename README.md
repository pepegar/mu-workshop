# Mu workshop

## Step 1 - Project structure

Let's start moving the project to its initial state using:
 
```bash
 sbt "groll initial"
```

and get sure everything works with:

```bash
 sbt clean compile
```

The initial state just contains the project structure with all the modules, 
dependencies and settings we are going to need along the project.

As you can see on the `build.sbt` the project is divided into `client` and `server`, 
both of them composed by smaller modules with specific purposes.

//TODO explain modules

On the `ProjectPlugin` we can find the settings per each of our modules.
The most important piece here is the **protocol settings** ,
where we configure the `source generation` on compile time and specify the `IDL` we are going to use.

//TODO explain settings

## Step 2 - Defining protocol

As we already told we are going to use `Avro` for our `IDL` protocol definition.

First we are going to create our protocol models on a `People.avdl` file.

Once we have the models we can use them into out protocol service `PeopleService.avdl`.

Note that the protocol belongs to the server file but it will be shared with the client for its generation.

If you use `sbt "groll next"` you can see how the **protocol** should looks like.

## Step 3 - Creating a server

We are going to start on the `server-process` module (realize that `server-common` just contains some common utils and models).
As we explained before, on this module we are going to implement the service generated from the protocol.

Once we have implemented our service logic, it's time to create the `server` app itself.

To do that we need to move to the `server-app` module where we are going to create 3 files:

 - **ServerBoot**: To load dependencies and services required to start the `server`. Here we'll place the server **initialization**.
 - **ServerProgram**: With our server program. In this file we'll configure the `Mu` server.
 - **ServerApp**: Just with the `IOApp` and the `main` method with only one line running the `ServerProgram`.

If you use `sbt "groll next"` you can see how the **server** should looks like.

## Step 4 - Generating a client

The client follows a similar structure, so, 
we'll start as well on the `client-process` module (the `client-common` has the same purpose as `server-common`).
In this case, the `client-process` module will contain all the stuff related with our `Mu` client.

We have there the `Mu` client configuration and creation, also, 
we'll have the client usages on a `tagless final algebra/handler` (easily to mock for testing).

***Note***: As a recommended pattern we usually create a internal model
 which we'll return instead of the protocol one in order to reduce the changes in case of protocol modifications.

Once we have implemented our service logic, it's time to create the `server` app itself.

To do that we need to move to the `server-app` module where we are going to create 3 files:

 - **ClientBoot**: To load dependencies and clients required to start the program.
 - **ClientProgram**: With our client logic. In this file we'll use the clients implemented on `client-process`.
 - **ClientApp**: Just with the `IOApp` and the `main` method with only one line running the `ClientProgram`.

If you use `sbt "groll next"` you can see how the **client** should looks like.

## Final - Live demo

Let's try all this hell.

The `speakers` are going to run the **server** and 
the `atendees` (on the same local network) will use their **clients** to send requests to the **speaker's server**.


## Compiling the slides

There is a two step process to compile the slides.
- from the root of the repository, run `sbt slides/mdoc` (or `sbt "slides/mdoc --watch"` if you want to compile slides on save)
- from the `47deg-slides` folder, run `npm install && npm start`
