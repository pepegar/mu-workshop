import ProjectPlugin._

////////////////////////
//// Shared Modules ////
////////////////////////

lazy val config = project in file("shared/modules/config") settings configSettings

////////////////////////
////     Shared     ////
////////////////////////

lazy val allSharedModules: Seq[ProjectReference] = Seq(
  config
)

lazy val allSharedModulesDeps: Seq[ClasspathDependency] =
  allSharedModules.map(ClasspathDependency(_, None))

lazy val shared = project in file("shared") aggregate (allSharedModules: _*) dependsOn (allSharedModulesDeps: _*)

//////////////////////////
////  Server Modules  ////
//////////////////////////

lazy val server_common = project in file("server/modules/common")

lazy val server_protocol = project in file("server/modules/protocol") settings serverProtocolSettings

lazy val server_process = project in file("server/modules/process") settings serverProcessSettings dependsOn (server_common, server_protocol)

lazy val server_app = project in file("server/modules/app") settings serverAppSettings dependsOn (server_process, config)

//////////////////////////
////      Server      ////
//////////////////////////

lazy val allServerModules: Seq[ProjectReference] = Seq(
  server_common,
  server_protocol,
  server_process,
  server_app
)

lazy val allServerModulesDeps: Seq[ClasspathDependency] =
  allServerModules.map(ClasspathDependency(_, None))

lazy val server = project in file("server") aggregate (allServerModules: _*) dependsOn (allServerModulesDeps: _*)
addCommandAlias("runServer", "server_app/runMain com.adrianrafo.seed.server.app.ServerApp")

//////////////////////////
////  Client Modules  ////
//////////////////////////

lazy val client_common = project in file("client/modules/common")

lazy val client_process = project in file("client/modules/process") settings clientRPCSettings dependsOn (client_common, server_protocol)

lazy val client_app = project in file("client/modules/app") settings clientAppSettings dependsOn (client_process, config)

//////////////////////////
////      Client      ////
//////////////////////////

lazy val allClientModules: Seq[ProjectReference] = Seq(
  client_common,
  client_process,
  client_app
)

lazy val allClientModulesDeps: Seq[ClasspathDependency] =
  allClientModules.map(ClasspathDependency(_, None))

lazy val client = project in file("client") aggregate (allClientModules: _*) dependsOn (allClientModulesDeps: _*)
addCommandAlias("runClient", "client_app/runMain com.adrianrafo.seed.client.app.ClientApp")

/////////////////////////
////       Root       ////
/////////////////////////

lazy val allRootModules: Seq[ProjectReference] = Seq(
  shared,
  client,
  server
)

lazy val allRootModulesDeps: Seq[ClasspathDependency] =
  allRootModules.map(ClasspathDependency(_, None))

lazy val root = project in file(".") settings (name := "Avro-Seed") aggregate (allRootModules: _*) dependsOn (allRootModulesDeps: _*)
