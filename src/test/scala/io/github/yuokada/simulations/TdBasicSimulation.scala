package io.github.yuokada.simulations

import com.treasuredata.client.TDClient
import io.gatling.core.Predef._
import io.gatling.core.session.ExpressionSuccessWrapper

import scala.concurrent.duration.DurationInt

class TdBasicSimulation extends Simulation {
  private val apiConfig        = SimulationConfigLoader.loadApiConfig()
  private val simulationConfig = SimulationConfigLoader.loadSimulationConfig()
  private val tdClient = TDClient
    .newBuilder()
    .setEndpoint(apiConfig.endpoint)
    .setApiKey(apiConfig.token)
    .build()

  private val scn = scenario("TD Query Scenario")
    .feed(TpchQueryFeeder)
    .exec(
      new TDQueryActionBuilder(
        requestName = "Tpch Benchmark Query".expressionSuccess,
        query = session => session("query").as[String],
        database = simulationConfig.schema,
        client = tdClient
      )
    )

  setUp(
    scn.inject(
      constantUsersPerSec(5.0) // ① 5 requests/sec
        .during(1.hour)        // ② 1 hour total
    )
  ).throttle(
    reachRps(5).in(10.seconds), // Gradually reach 5 RPS
    holdFor(1.hour)             // Sustain for 1 hour
  ).maxDuration(1.hour)
   .assertions(
     global.requests.count.lte(5000),       // ② max 5000 requests
     global.activeUsers.count.lte(10)       // ③ concurrency <= 10
   )
}
