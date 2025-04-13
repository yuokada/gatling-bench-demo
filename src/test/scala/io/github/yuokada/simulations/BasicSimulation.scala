package io.github.yuokada.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class BasicSimulation extends Simulation {
//  private val apiConfig: TdApiConfig = LoadSimulationConfig.loadApiConfig()
//  private val httpProtocol = http.baseUrl(apiConfig.endpoint())
   private val httpProtocol = http.baseUrl("https://example.com")

//  private val scn = scenario("Basic Test")
//    .exec(http("Request_1").get("/"))
//    .exec(http("Request_2").get("/"))
//    .exec(http("Request_3").get("/"))

  private val scn = scenario("Basic Test")
    .repeat(3, "loop") {
      exec(http("Request_1").get("/")).pause(50.millis)
    }
    .pause(150.millis)

  setUp(
    //scn.inject(atOnceUsers(50) // 50 users)
    // scn.inject(rampUsers(10).during(10.seconds)) // 50 users over 10 seconds
    scn.inject(constantUsersPerSec(5).during(10.seconds)) // 5 users per second for 10 seconds
      // scn.inject(nothingFor(4.seconds), atOnceUsers(50), rampUsers(100).during(10.seconds))
  ).protocols(httpProtocol)
}
