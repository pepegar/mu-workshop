# Mu workshop

## Step 1 - Project structure

Let's start moving the project to its initial state using:
 
```bash
 sbt "groll initial"
```

and make sure everything works with:

```bash
 sbt clean compile
```

The initial state just contains the project structure with all the modules, 
dependencies and settings we are going to need along the project.

As you can see in the `build.sbt`, the project is divided into `client` and `server`, 
both of them composed by smaller modules with specific purposes.

The modules list contains:
 - **ClientCommon**: A place for shared models across the client.
 - **ClientProcess**: The module for the client logic. Whatever the client will do should be placed here. It also contains the code to create the Mu client.
 - **ClientApp**: The client initialization. Here we just initiate the application and its dependencies (logger, clients, config, ....).
 - **ServerCommon**: A place for shared models across the server.
 - **ServerProcess**: The module for the server logic. Whatever the server will do should be placed here.
 - **ServerApp**: The server initialization. Here we just initiate the application and its dependencies (logger, clients, config, ....).

In the `project/ProjectPlugin.scala` we can find the settings per each of our modules.
The most important piece here is the **protocol settings** specification,
where we configure the `source generation` on compile time and specify the `IDL` we are going to use.

The complete list of settings is:
 - **logSettings**: Settings required for logging.
 - **serverProtocolSettings**: Settings for source generation on compile time from the Avro protocol.
 - **serverProcessSettings**: The server process module settings. It just need the logging to work.
 - **serverAppSettings**: The settings for the server app with the **Mu** server dependency.
 - **clientProcessSettings**: The client process module settings. The **Mu** client dependency is needed here.
 - **clientAppSettings**: Here we just add the dependencies required to start the client application.
 - **docsSettings**: Settings for slides. Unrelated with the project itself.

The `projectSettings` contains a mix of things:

- The `macro-paradise` compiler plugin to activate macros,
- Some `scalacOptions` to make the compiler more strict
- Adds the `silencer` plugin, to make the compiler not warn about unused imports on generated code. The `"-P:silencer:pathFilters=target"` in `scalacOptions` is in charge of this.

## Step 2 - Defining protocol

As we already told we are going to use `Avro` for our `IDL` protocol definition.

First we are going to create our protocol models on a `People.avdl` file.

Once we have the models, we can use them into out protocol service `PeopleService.avdl`.

Note that the protocol belongs to the `server` side but it will be shared with the `client` modules for the `RPC` client generation.

We need to share the `protocol` between the `server` and the `client` because the generated code by **Mu** 
will contains all the utilities for both the `RPC client` and the `RPC server` creation.

We share the protocol from the `server` with the `client` using the `IDL` files
to avoid **binary issues** caused for different versions between the `server` and the `client`.

If you use `sbt "groll next"` you can see how the **protocol** should look like.

## Step 3 - Creating a server

We are going to start on the `server-process` module (realize that `server-common` just contains some common utils and models).
As we explained before, we are going to implement the service generated from the protocol in this module .

Once we have implemented our service logic, it's time to create the `server` app itself.

To do that, we need to move to the `server-app` module where we are going to create 3 files:

 - **ServerBoot**: To load dependencies and services required to start the `server`. Here we'll place the server **initialization**.
 - **ServerProgram**: With our server program. In this file we'll configure the **Mu** server.
 - **ServerApp**: Just with the `IOApp` and the `main` method with only one line running the `ServerProgram`.

If you use `sbt "groll next"` you can see how the **server** should looks like.

## Step 4 - Generating a client

The client follows a similar structure, so, 
we'll start with the `client-process` module as well (the `client-common` has the same purpose as `server-common`).
In this case, the `client-process` module will contain all the stuff related with our **Mu** client.

We have there the **Mu** client configuration and creation, also, 
we'll have the `client` usages on a `tagless final algebra/handler` (easily to mock for testing).

***Note***: As a recommended pattern we usually create an `internal` model
 that will be the one we return instead of the `protocol` model in order to reduce the changes in case of protocol modifications.

Now it's time to implement the `client` app.

To do that, we need to move to the `client-app` module where we are going to create 3 files:

 - **ClientBoot**: To load dependencies and clients required to start the program.
 - **ClientProgram**: With our client logic. In this file we'll use the clients implemented on `client-process`.
 - **ClientApp**: Just with the `IOApp` and the `main` method with only one line running the `ClientProgram`.

If you use `sbt "groll next"` you can see how the **client** should look like.

## Final - Live demo

Let's try all this hell.

The `speakers` are going to run the **server** and 
the `atendees` (on the same local network) will use their **clients** to send requests to the **speaker's server**.


## Compiling the slides

There is a two step process to compile the slides.
- from the root of the repository, run `sbt slides/mdoc` (or `sbt "slides/mdoc --watch"` if you want to compile slides on save)
- from the `47deg-slides` folder, run `npm install && npm start`
