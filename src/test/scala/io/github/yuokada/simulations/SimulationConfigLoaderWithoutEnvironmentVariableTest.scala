package io.github.yuokada.simulations

import org.junit.jupiter.api.Test
import org.scalatestplus.junit5.AssertionsForJUnit

class SimulationConfigLoaderWithoutEnvironmentVariableTest extends AssertionsForJUnit {

  @Test
  def throwException(): Unit = {
    intercept[IllegalArgumentException] {
      SimulationConfigLoader.loadApiConfig()
    }
  }
}
