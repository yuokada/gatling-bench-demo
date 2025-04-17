package io.github.yuokada.simulations

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.{DisabledIfEnvironmentVariable, EnabledIfEnvironmentVariable}
import org.scalatestplus.junit5.AssertionsForJUnit

class SimulationConfigLoaderTest extends AssertionsForJUnit {
  @Test
  @DisabledIfEnvironmentVariable(named = "TD_API_KEY", matches = ".*")
  def loadApiConfigTestWhenEnvironmentVariableNotExist(): Unit = {
    intercept[IllegalArgumentException] {
      SimulationConfigLoader.loadApiConfig()
    }
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "TD_API_KEY", matches = ".*")
  def loadApiConfigTestWhenEnvironmentVariableExist(): Unit = {
    val config = SimulationConfigLoader.loadApiConfig()
    assert(config.endpoint == "api.tdameritrade.com")
    assert(config.token == "sample_token")
  }

  @Test
  def loadSimulationConfigTest(): Unit = {
    val config = SimulationConfigLoader.loadSimulationConfig()
    assertEquals(config.schema, "sample_datasets")
    assertEquals(config.maxConcurrentClients, 3)
    assertEquals(config.testDurationSeconds.toSeconds, 60)
  }
}
