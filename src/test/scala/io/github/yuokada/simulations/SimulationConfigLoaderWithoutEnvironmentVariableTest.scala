package io.github.yuokada.simulations

import org.junit.jupiter.api.Test
import org.scalatestplus.junit5.AssertionsForJUnit

class SimulationConfigLoaderWithoutEnvironmentVariableTest extends AssertionsForJUnit {

  @Test
  def throwException(): Unit = {
    val exception = intercept[IllegalArgumentException] {
      SimulationConfigLoader.loadApiConfig()
    }
    assert(exception.getMessage.contains("The TD_API_KEY environment variable must be set"))
  }
}
