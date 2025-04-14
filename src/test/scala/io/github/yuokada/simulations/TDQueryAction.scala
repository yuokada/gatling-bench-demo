package io.github.yuokada.simulations

import com.treasuredata.client.model.TDJobRequest
import com.treasuredata.client.{ExponentialBackOff, TDClient}
import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.Clock
import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.structure.ScenarioContext

class TDQueryAction(
    requestName: Expression[String],
    query: Expression[String],
    database: String,
    client: TDClient,
    statsEngine: StatsEngine,
    clock: Clock,
    next: Action
) extends Action {

  override def name: String = "TDQueryAction"

  override def execute(session: Session): Unit = {
    val start = clock.nowMillis

    // TODO: Fix the following two lines to use the correct session values
    val resolvedName  = requestName(session).toOption.get
    val resolvedQuery = query(session).toOption.get

    try {
      val jobId = client.submit(TDJobRequest.newPrestoQuery(database, resolvedQuery))

      val backoff = new ExponentialBackOff()
      var job     = client.jobStatus(jobId)
      while (!job.getStatus.isFinished) {
        Thread.sleep(backoff.nextWaitTimeMillis())
        job = client.jobStatus(jobId)
      }

      val status = job.getStatus
      val end    = clock.nowMillis

      if (status.isFinished) {
        statsEngine.logResponse(
          session.scenario,
          session.groups,
          resolvedName,
          start,
          end,
          OK,
          None,
          None
        )
        next ! session
      } else {
        statsEngine.logResponse(
          session.scenario,
          session.groups,
          resolvedName,
          start,
          end,
          KO,
          Some("TD job failed"),
          None
        )
        next ! session.markAsFailed
      }

    } catch {
      case e: Exception =>
        val end = clock.nowMillis
        statsEngine.logResponse(
          session.scenario,
          session.groups,
          resolvedName,
          start,
          end,
          KO,
          Some(e.getMessage),
          None
        )
        next ! session.markAsFailed
    }
  }
}

class TDQueryActionBuilder(
    requestName: Expression[String],
    query: Expression[String],
    database: String,
    client: TDClient
) extends ActionBuilder {

  override def build(ctx: ScenarioContext, next: Action): Action = {
    new TDQueryAction(
      requestName,
      query,
      database,
      client,
      ctx.coreComponents.statsEngine,
      ctx.coreComponents.clock,
      next
    )
  }
}
