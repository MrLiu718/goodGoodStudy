package com.lhh.TableSql.demo01

import org.apache.flink.api.scala._
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/8 16:43
  * @version 0.0.1
  */
object FlinkBatchSQL {

  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val batchSQL: BatchTableEnvironment = BatchTableEnvironment.create(environment)

    val sourceSet: DataSet[String] = environment.readTextFile("D:\\开课吧课程资料\\Flink实时数仓\\datas\\dataSet.csv")

    val userSet: DataSet[User2] = sourceSet.map(x => {
      println(x)
      val line: Array[String] = x.split(",")
      User2(line(0).toInt, line(1), line(2).toInt)
    })

    import org.apache.flink.table.api._

    batchSQL.registerDataSet("user",userSet)
    //val table: Table = batchSQL.scan("user").filter("age > 18")
    //注意：user关键字是flink当中的保留字段，如果用到了这些保留字段，需要转译
    val table: Table = batchSQL.sqlQuery("select id,name,age from `user` ")
    val sink = new CsvTableSink("D:\\开课吧课程资料\\Flink实时数仓\\datas\\batchSink.csv","===",1,WriteMode.OVERWRITE)
    table.writeToSink(sink)


    //将Table转换成为DataSet
    val tableSet: DataSet[User2] = batchSQL.toDataSet[User2](table)

    tableSet.map(x =>x.age).print()

    environment.execute()
  }
}
case class User2(id:Int,name:String,age:Int)
