package io.github.yuokada.simulations

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.junit.jupiter.api.{MethodOrderer, Test, TestMethodOrder}
import org.junitpioneer.jupiter.SetEnvironmentVariable
import org.scalatestplus.junit5.AssertionsForJUnit

@SetEnvironmentVariable(key = "TD_API_KEY", value = "sample_token")
@TestMethodOrder(classOf[MethodOrderer.Random])
class SimulationConfigLoaderTest extends AssertionsForJUnit {

  @Test
  @EnabledIfEnvironmentVariable(named = "TD_API_KEY", matches = ".*")
  def loadApiConfigTestWhenEnvironmentVariableExist(): Unit = {
    val config = SimulationConfigLoader.loadApiConfig()
    assert(config.endpoint == "api.treasuredata.com")
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
