package io.github.yuokada.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

@deprecated
class BasicSimulation extends Simulation {
  private val httpProtocol = http.baseUrl("https://example.com")

  private val scn = scenario("Basic Test")
    .exec(http("Request_1").get("/").check(status.is(200)))
    .pause(150.millis)
    .exec(http("Request_2").get("/?foo=bar").check(status.is(200)))
    .pause(150.millis)
    .exec(http("Request_3").get("/?foo=bar&baz=qux").check(status.is(200)))
    .pause(150.millis)

//  setUp(
//    //scn.inject(atOnceUsers(50) // 50 users)
//    // scn.inject(rampUsers(10).during(10.seconds)) // 50 users over 10 seconds
//    // scn.inject(constantUsersPerSec(5).during(30.seconds)) // 5 users per second for 10 seconds
//    // scn.inject(nothingFor(4.seconds), atOnceUsers(50), rampUsers(100).during(10.seconds))
//    scn.inject(constantUsersPerSec(5)
//      .during(30.seconds))
//      .throttle(jumpToRps(10), holdFor(10.minutes))
//  ).protocols(httpProtocol)
  private val simulationConfig = SimulationConfigLoader.loadSimulationConfig()
  setUp(
    scn.inject(
      constantUsersPerSec(simulationConfig.maxConcurrentClients.toFloat)
        .during(simulationConfig.testDurationSeconds)
    )
  ).protocols(httpProtocol)
}
