package io.github.yuokada.simulations

case class SimulationConfig(
    schema: String = "sample_datasets",
    maxConcurrentClients: Int,
    testDurationSeconds: Int
) {
  require(schema != null && schema.nonEmpty, "Schema cannot be null or empty")
  require(maxConcurrentClients > 0, "max-concurrent-clients must be greater than 0")
  require(testDurationSeconds > 0, "test-duration-seconds must be greater than 0")
}
