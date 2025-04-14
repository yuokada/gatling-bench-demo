package io.github.yuokada.simulations;

public record SimulationConfig(
    String schema,
    Integer maxConcurrentClients,
    Integer testDurationSeconds
) {
    public SimulationConfig {
        if (schema == null) {
            schema = "sample_datasets";
        }
        if (schema.isEmpty()) {
            throw new IllegalArgumentException("Schema cannot be null or empty");
        }
        if (maxConcurrentClients == null || maxConcurrentClients <= 0) {
            throw new IllegalArgumentException("max-concurrent-clients must be greater than 0");
        }
        if (testDurationSeconds == null || testDurationSeconds <= 0) {
            throw new IllegalArgumentException("test-duration-seconds must be greater than 0");
        }
    }
}
