package io.github.yuokada.simulations

object TpchQueryFeeder {
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

  // FeederBuilderBase
  def generate(): Iterator[Map[String, String]]= {
    tables.map { table =>
      Map("table" -> table)
    }.toIterator
  }

}
