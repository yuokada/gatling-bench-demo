package io.github.yuokada.simulations

import com.typesafe.config.ConfigFactory
import io.github.yuokada.simulations.SimulationConstants.DEFAULT_SCHEMA

import java.time.Duration
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object SimulationConstants {
  val DEFAULT_SCHEMA = "sample_datasets"
}

object SimulationConfigLoader {

  def loadApiConfig(): TdApiConfig = {
    val config = ConfigFactory.load("simulation.conf")
    var endpoint = if (config.hasPath("apiEndpoint")) {
      config.getString("apiEndpoint")
    } else {
      throw new IllegalArgumentException("API endpoint not found in configuration")
    }
    var token = if (config.hasPath("apiKey")) {
      config.getString("apiKey")
    } else {
      throw new IllegalArgumentException("API key not found in configuration")
    }
    if (sys.env.contains("TD_API_ENDPOINT")) {
      endpoint = sys.env("TD_API_ENDPOINT")
    }
    if (sys.env.contains("TD_API_KEY")) {
      token = sys.env("TD_API_KEY")
    }

    TdApiConfig(endpoint, token)
  }

  def loadSimulationConfig(): SimulationConfig = {
    val config = ConfigFactory.load("simulation.conf")
    val schema = if (config.hasPath("schema")) {
      config.getString("schema")
    } else {
      DEFAULT_SCHEMA
    }
    val maxClients = Option(config.getInt("active-user")).getOrElse(1) // default to 1 active user
    val testDuration = Option(config.getDuration("test-duration")).getOrElse(Duration.parse("60S"))

    SimulationConfig(schema, maxClients, FiniteDuration.apply(testDuration.toSeconds, TimeUnit.SECONDS))
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
    testDurationSeconds: FiniteDuration
) {
  require(schema != null && schema.nonEmpty, "Schema cannot be null or empty")
  require(maxConcurrentClients > 0, "max-concurrent-clients must be greater than 0")
  require(testDurationSeconds.isFinite, "test-duration-seconds must be greater than 0")
}
