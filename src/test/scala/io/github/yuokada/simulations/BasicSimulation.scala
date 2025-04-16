package io.github.yuokada.simulations

import com.treasuredata.client.model.TDJobRequest
import com.treasuredata.client.{ExponentialBackOff, TDClient}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

@deprecated
class BasicSimulation extends Simulation {
//  private val apiConfig: TdApiConfig = LoadSimulationConfig.loadApiConfig()
//  private val httpProtocol = http.baseUrl(apiConfig.endpoint())
  private val httpProtocol = http.baseUrl("https://example.com")

  private val feeder = TpchQueryFeeder

  private val apiConfig: TdApiConfig = LoadSimulationConfig.loadApiConfig()
  private val endpoint               = apiConfig.endpoint
  private val token                  = apiConfig.token
  private val defaultSchema          = "sample_datasets"
  private val client = TDClient
    .newBuilder()
    .setEndpoint(endpoint)
    .setApiKey(token)
    .build();

  private val scn = scenario("Basic Test")
    .exec(http("Request_1").get("/").check(status.is(200)))
    .pause(150.millis)
    .exec(http("Request_2").get("/?foo=bar").check(status.is(200)))
    .pause(150.millis)
    .exec(http("Request_3").get("/?foo=bar&baz=qux").check(status.is(200)))
    .pause(150.millis)

  private val scn1 = scenario("Treasure Data Query Scenario")
    .exec { session =>
      val jobId = client.submit(TDJobRequest.newPrestoQuery(defaultSchema, feeder.generate()))

      // Wait until the query finishes
      val backoff = new ExponentialBackOff()
      var job     = client.jobStatus(jobId)
      while (!job.getStatus.isFinished) {
        Thread.sleep(backoff.nextWaitTimeMillis())
        job = client.jobStatus(jobId)
      }
      val detail = client.jobInfo(jobId)
      println(detail.getStatus)
      session
    }.pause(150.millis)

//  setUp(
//    //scn.inject(atOnceUsers(50) // 50 users)
//    // scn.inject(rampUsers(10).during(10.seconds)) // 50 users over 10 seconds
//    // scn.inject(constantUsersPerSec(5).during(30.seconds)) // 5 users per second for 10 seconds
//    // scn.inject(nothingFor(4.seconds), atOnceUsers(50), rampUsers(100).during(10.seconds))
//    scn.inject(constantUsersPerSec(5)
//      .during(30.seconds))
//      .throttle(jumpToRps(10), holdFor(10.minutes))
//  ).protocols(httpProtocol)
  setUp(scn.inject(constantUsersPerSec(3).during(30.seconds))).protocols(httpProtocol)
}
