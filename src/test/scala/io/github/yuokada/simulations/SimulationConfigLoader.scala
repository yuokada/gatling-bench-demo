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
  private val SIMULATION_CONFIG_FILE = "simulation.conf"
  private val fallbackConfig = ConfigFactory.parseString(
    s"""
      |schema: ${DEFAULT_SCHEMA}
      |active-user: 1
      |test-duration: 60S
      |""".stripMargin
  )

  def loadApiConfig(): TdApiConfig = {
    val config = ConfigFactory.load(SIMULATION_CONFIG_FILE).withFallback(fallbackConfig).resolve()
    val endpoint = if (config.hasPath("api-endpoint")) {
      config.getString("api-endpoint")
    } else {
      throw new IllegalArgumentException("API endpoint not found in configuration")
    }
    val token = if (sys.env.contains("TD_API_KEY")) {
      sys.env("TD_API_KEY")
    } else if (config.hasPath("api-token")) {
      config.getString("api-token")
    } else {
      throw new IllegalArgumentException("API token not found in configuration")
    }
    TdApiConfig(endpoint, token)
  }

  def loadSimulationConfig(): SimulationConfig = {
    val config       = ConfigFactory.load(SIMULATION_CONFIG_FILE).withFallback(fallbackConfig)
    val schema       = config.getString("schema")
    val maxClients   = config.getInt("active-user") // default to 1 active user
    val testDuration = config.getDuration("test-duration")

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
