package io.github.yuokada.simulations

//final class TdApiConfig  (: String, : String) { if (endpoint == null || endpoint.isEmpty) throw new IllegalArgumentException("Endpoint cannot be null or empty")
//if (token == null || token.isEmpty) throw new IllegalArgumentException("Token cannot be null or empty")
//final private val endpoint: String = null
//final private val token: String = null
//}
case class TdApiConfig(
    endpoint: String,
    token: String
) {
  require(endpoint != null && endpoint.nonEmpty, "Endpoint cannot be null or empty")
  require(token != null && token.nonEmpty, "Token cannot be null or empty")
}
