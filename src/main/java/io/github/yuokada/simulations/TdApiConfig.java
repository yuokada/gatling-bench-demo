package io.github.yuokada.simulations;

public record TdApiConfig(String endpoint, String token) {

    public TdApiConfig {
        if (endpoint == null || endpoint.isEmpty()) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty");
        }
        // endpoint should be a valid URL
        if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
            throw new IllegalArgumentException("Endpoint must start with http:// or https://");
        }
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
    }
}

