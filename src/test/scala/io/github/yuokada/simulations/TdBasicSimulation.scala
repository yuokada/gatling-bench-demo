package io.github.yuokada.simulations

import com.treasuredata.client.TDClient
import io.gatling.core.Predef._
import io.gatling.core.session.ExpressionSuccessWrapper

import scala.concurrent.duration.DurationInt

class TdBasicSimulation extends Simulation {
  private val apiConfig = LoadSimulationConfig.loadApiConfig()
  private val endpoint  = apiConfig.endpoint
  private val token     = apiConfig.token
  private val tdClient = TDClient
    .newBuilder()
    .setEndpoint(endpoint)
    .setApiKey(token)
    .build()

  private val simulationConfig = LoadSimulationConfig.loadSimulationConfig()
  private val defaultSchema    = simulationConfig.schema

  private val scn = scenario("TD Query Scenario")
    .feed(TpchQueryFeeder)
    .exec(
      new TDQueryActionBuilder(
        requestName = "Tpch Benchmark Query".expressionSuccess,
        query = session => session("query").as[String],
        database = defaultSchema,
        client = tdClient
      )
    )

  setUp(
    scn.inject(
      constantUsersPerSec(simulationConfig.maxConcurrentClients.doubleValue())
        .during(simulationConfig.testDurationSeconds.seconds)
    )
  )
}
