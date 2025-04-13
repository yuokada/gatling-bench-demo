package io.github.yuokada.simulations;

public record SimulationConfig(
    String schema,
    Integer numberOfSimulationClients,
    Integer numberOfRequests
) {
    public SimulationConfig {
        if (schema == null || schema.isEmpty()) {
            throw new IllegalArgumentException("Schema cannot be null or empty");
        }
        if (numberOfSimulationClients == null || numberOfSimulationClients <= 0) {
            throw new IllegalArgumentException("Number of simulation clients must be greater than 0");
        }
        if (numberOfRequests == null || numberOfRequests <= 0) {
            throw new IllegalArgumentException("Number of requests must be greater than 0");
        }
    }
}
