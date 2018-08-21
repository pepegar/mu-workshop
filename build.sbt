import ProjectPlugin._

//////////////////////////
//// Protocol Modules ////
//////////////////////////

lazy val common = project in file("protocol/modules/common")

lazy val config = project in file("protocol/modules/config") settings configSettings

lazy val protocol_rpc = project in file("protocol/modules/rpc") settings rpcProtocolSettings

//////////////////////////
////     Protocol     ////
//////////////////////////

lazy val allProtocolModules: Seq[ProjectReference] = Seq(
  common,
  config,
  protocol_rpc
)

lazy val allProtocolModulesDeps: Seq[ClasspathDependency] =
  allProtocolModules.map(ClasspathDependency(_, None))

lazy val protocol = project in file("protocol") aggregate (allProtocolModules: _*) dependsOn (allProtocolModulesDeps: _*)

//////////////////////////
////  Client Modules  ////
//////////////////////////

lazy val client_process = project in file("client/modules/process") settings clientRPCSettings dependsOn (common, protocol_rpc)

lazy val client_app = project in file("client/modules/app") settings clientAppSettings dependsOn (client_process, config)

//////////////////////////
////      Client      ////
//////////////////////////

lazy val allClientModules: Seq[ProjectReference] = Seq(
  client_process,
  client_app
)

lazy val allClientModulesDeps: Seq[ClasspathDependency] =
  allClientModules.map(ClasspathDependency(_, None))

lazy val client = project in file("client") aggregate (allClientModules: _*) dependsOn (allClientModulesDeps: _*)
addCommandAlias("runClient", "client_app/runMain com.adrianrafo.seed.client.app.ClientApp")

//////////////////////////
////  Server Modules  ////
//////////////////////////

lazy val server_process = project in file("server/modules/process") settings serverSettings dependsOn (common, protocol_rpc)

lazy val server_app = project in file("server/modules/app") settings serverAppSettings dependsOn (server_process, config)

//////////////////////////
////      Server      ////
//////////////////////////

lazy val allServerModules: Seq[ProjectReference] = Seq(
  server_process,
  server_app
)

lazy val allServerModulesDeps: Seq[ClasspathDependency] =
  allServerModules.map(ClasspathDependency(_, None))

lazy val server = project in file("server") aggregate (allServerModules: _*) dependsOn (allServerModulesDeps: _*)
addCommandAlias("runServer", "server_app/runMain com.adrianrafo.seed.server.app.ServerApp")

/////////////////////////
////       Root       ////
/////////////////////////

lazy val allRootModules: Seq[ProjectReference] = Seq(
  protocol,
  client,
  server,
)

lazy val allRootModulesDeps: Seq[ClasspathDependency] =
  allRootModules.map(ClasspathDependency(_, None))

lazy val root = project in file(".") settings (name := "Avro-Seed") aggregate (allRootModules: _*) dependsOn (allRootModulesDeps: _*)
