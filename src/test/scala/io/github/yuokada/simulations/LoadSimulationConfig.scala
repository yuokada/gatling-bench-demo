package io.github.yuokada.simulations

import io.github.yuokada.simulations.SimulationConstants.DEFAULT_SCHEMA

import java.io.{IOException, InputStream}
import java.util.Properties

object SimulationConstants {
  val SIMULATION_PREFIX    = "abc.simulation."
  val DEFAULT_API_ENDPOINT = "api-development.treasuredata.com"
  val DEFAULT_SCHEMA       = "sample_datasets"
}

object LoadSimulationConfig {

  def loadApiConfig(): TdApiConfig = {
    val props = getProperties

    var endpoint =
      props.getProperty(SimulationConstants.SIMULATION_PREFIX + "endpoint", SimulationConstants.DEFAULT_API_ENDPOINT)
    var token = props.getProperty(SimulationConstants.SIMULATION_PREFIX + "api-token", "")

    if (sys.env.contains("TD_API_ENDPOINT")) {
      endpoint = sys.env("TD_API_ENDPOINT")
    }
    if (sys.env.contains("TD_API_KEY")) {
      token = sys.env("TD_API_KEY")
    }

    TdApiConfig(endpoint, token)
  }

  def loadSimulationConfig(): SimulationConfig = {
    val props = getProperties

    val schema = Option(props.getProperty(SimulationConstants.SIMULATION_PREFIX + "schema")).getOrElse(DEFAULT_SCHEMA)
    val maxClients   = props.getProperty(SimulationConstants.SIMULATION_PREFIX + "max-concurrent-clients", "3").toInt
    val testDuration = props.getProperty(SimulationConstants.SIMULATION_PREFIX + "test-duration-seconds", "60").toInt

    SimulationConfig(schema, maxClients, testDuration)
  }

  private def getProperties: Properties = {
    val props        = new Properties()
    val resourcePath = "application.properties"

    val input: InputStream = Option(getClass.getClassLoader.getResourceAsStream(resourcePath))
      .getOrElse(throw new IllegalStateException(s"Unable to find $resourcePath"))

    try {
      props.load(input)
    } catch {
      case e: IOException => throw new RuntimeException("Failed to load configuration", e)
    } finally {
      input.close()
    }

    props
  }
}

case class TdApiConfig(
    endpoint: String,
    token: String
) {
  require(endpoint != null && endpoint.nonEmpty, "Endpoint cannot be null or empty")
  require(token != null && token.nonEmpty, "Token cannot be null or empty")
}

case class SimulationConfig(
    schema: String = "sample_datasets",
    maxConcurrentClients: Int,
    testDurationSeconds: Int
) {
  require(schema != null && schema.nonEmpty, "Schema cannot be null or empty")
  require(maxConcurrentClients > 0, "max-concurrent-clients must be greater than 0")
  require(testDurationSeconds > 0, "test-duration-seconds must be greater than 0")
}
