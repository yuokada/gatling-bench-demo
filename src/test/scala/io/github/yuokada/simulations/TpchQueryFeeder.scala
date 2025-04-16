package io.github.yuokada.simulations

import io.gatling.core.feeder.{Feeder, Record}

import scala.util.Random

object TpchQueryFeeder extends Feeder[String] {
  // Only small schemas are used
  private val schemas = Seq("tiny", "sf1")

  private val tables = Seq(
    "customer",
    "lineitem",
    "nation",
    "orders",
    "part",
    "partsupp",
    "region",
    "supplier"
  )

  override def hasNext: Boolean = {
    // Always return true to keep the feeder alive
    true
  }
  override def next(): Record[String] = Map("query" -> generate())

  def generate(): String = {
    val (schema, table) = choiceSchemaAndTable()

    val operation = Random.nextInt(10)
    val query = operation match {
      case 0 | 1     => generateShowTables(table, schema)
      case 2 | 3 | 4 => generateCountQuery(table, schema)
      case _         => generateSimpleSelectQuery(table, schema)
    }
    query
  }

  private def choiceSchemaAndTable(): (String, String) = {
    val schema = schemas(Random.nextInt(schemas.length))
    val table  = tables(Random.nextInt(tables.length))
    (schema, table)
  }

  def generateShowTables(table: String, schema: String = "sf1"): String = {
    s"DESCRIBE tpch.$schema.$table"
  }

  def generateCountQuery(table: String, schema: String = "sf1"): String = {
    s"SELECT COUNT(*) FROM tpch.$schema.$table"
  }

  def generateSimpleSelectQuery(table: String, schema: String = "sf1"): String = {
    val limit = Random.nextInt(31) + 1
    s"SELECT * FROM tpch.$schema.$table LIMIT $limit"
  }
}
