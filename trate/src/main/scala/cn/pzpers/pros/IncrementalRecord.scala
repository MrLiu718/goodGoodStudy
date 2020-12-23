package cn.pzpers.pros

import scala.beans.BeanProperty



/*val CURRENT_TS = "current_ts"// : "2018-06-19T08:52:55.161000",
val OP_TS = "op_ts"// : "2018-06-19 00:52:47.078555",
val OP_TYPE = "op_type"// : "I",
val POS = "pos"// : "00000000050003417038",
val TABLE = "table"// : "RZQKGDB.ORDERS"*/
trait IncrementalRecord {
}

case class IncrementalInsertRecord(@BeanProperty var table: String, // table name
                                   @BeanProperty var currentTs: String, // current ts toString
                                   @BeanProperty var opTs: String, // operation ts toString
                                   @BeanProperty var opType: DataOpType.ops,
                                   @BeanProperty var after: String,
                                   @BeanProperty var offset: Long,
                                   @BeanProperty var original: String
                                  ) extends IncrementalRecord

case class IncrementalUpdateRecord(@BeanProperty var table: String, // table name
                                   @BeanProperty var currentTs: String, // current ts toString
                                   @BeanProperty var opTs: String, // operation ts toString
                                   @BeanProperty var opType: DataOpType.ops,
                                   @BeanProperty var before: String,
                                   @BeanProperty var after: String,
                                   @BeanProperty var offset: Long,
                                   @BeanProperty var original: String) extends IncrementalRecord

case class IncrementalDeleteRecord(@BeanProperty var table: String, // table name
                                   @BeanProperty var currentTs: String, // current ts toString
                                   @BeanProperty var opTs: String, // operation ts toString
                                   @BeanProperty var opType: DataOpType.ops,
                                   @BeanProperty var before: String,
                                   @BeanProperty var offset: Long,
                                   @BeanProperty var original: String) extends IncrementalRecord

case class ProcessedIncrementalRecord(@BeanProperty var table: String, // table name
                                      @BeanProperty var currentTs: String, // current ts toString
                                      @BeanProperty var opTs: String, // operation ts toString
                                      @BeanProperty var opType: DataOpType.ops,
                                      @BeanProperty var snapshot: String,
                                      @BeanProperty var offset: Long,
                                      @BeanProperty var original: String) extends IncrementalRecord