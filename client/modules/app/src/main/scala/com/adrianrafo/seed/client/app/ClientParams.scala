package com.adrianrafo.seed.client.app

import com.adrianrafo.seed.client.common.models.ParamsConfig
import scopt.OptionParser

object ClientParams {

  val default = ParamsConfig("Foo")

  def paramsConfig(name: String): OptionParser[ParamsConfig] =
    new scopt.OptionParser[ParamsConfig](name) {

      opt[String]("name")
        .required()
        .action((value, config) => config.copy(request = value))
        .text("The name for the request")

    }

  def loadParams(name: String, args: List[String]): ParamsConfig =
    paramsConfig(name).parse(args, default).getOrElse(default)

}
