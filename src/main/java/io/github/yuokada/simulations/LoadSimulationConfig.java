package io.github.yuokada.simulations;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static io.github.yuokada.simulations.SimulationConstants.DEFAULT_API_ENDPOINT;
import static io.github.yuokada.simulations.SimulationConstants.SIMULATION_PREFIX;

public class LoadSimulationConfig {

    public static TdApiConfig loadApiConfig() {
        Properties props = getProperties();

        String endpoint = props.getProperty(SIMULATION_PREFIX + "endpoint", DEFAULT_API_ENDPOINT);
        String token = props.getProperty(SIMULATION_PREFIX + "api-token", "");
        if (System.getenv("TD_API_ENDPOINT") != null) {
            endpoint = System.getenv("TD_API_ENDPOINT");
        }
        if (System.getenv("TD_API_KEY") != null) {
            token = System.getenv("TD_API_KEY");
        }
        return new TdApiConfig(endpoint, token);
    }

    public static SimulationConfig loadSimulationConfig() {
        Properties props = getProperties();
        String schema = props.getProperty(SIMULATION_PREFIX + "schema", null);
        String maxClients = props.getProperty(SIMULATION_PREFIX + "max-concurrent-clients", String.valueOf(3));
        String testDuration = props.getProperty(SIMULATION_PREFIX + "test-duration-seconds", String.valueOf(60));
        return new SimulationConfig(schema, Integer.valueOf(maxClients), Integer.valueOf(testDuration));
    }

    private static Properties getProperties() {
        // Load configuration from src/main/resources/application.properties
        Properties props = new Properties();
        try (InputStream input = LoadSimulationConfig.class.getClassLoader()
            .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("Unable to find application.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
        return props;
    }
}
