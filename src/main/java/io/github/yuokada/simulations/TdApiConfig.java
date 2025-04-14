package io.github.yuokada.simulations;

public record TdApiConfig(String endpoint, String token) {

    public TdApiConfig {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
    }
}

