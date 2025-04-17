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
      constantUsersPerSec(simulationConfig.maxConcurrentClients.doubleValue())
        .during(simulationConfig.testDurationSeconds)
    )
  )
}
