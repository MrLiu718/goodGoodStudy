package com.lhh.TableSql.demo01

import org.apache.flink.api.common.typeinfo.Types
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.table.api.Table
import org.apache.flink.table.api.java.StreamTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.apache.flink.table.sources.CsvTableSource

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/8 12:04
  * @version 0.0.1
  *          使用sql去查询数据
  */
object TableSql {
  def main(args: Array[String]): Unit = {
    //流式sql 程序入口
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //流式sql 运行环境
    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(environment)

    //注册tableSource
    val tableSource: CsvTableSource = CsvTableSource.builder()
      .field("id", Types.INT)
      .field("name", Types.STRING)
      .field("age", Types.INT)
      .fieldDelimiter(",") //分割符
      .ignoreFirstLine()
      .ignoreParseErrors()
      .lineDelimiter("\r\n") //行号切割符
      .path("F:\\开课吧 大数据\\flink\\Flink实时数仓课件\\数据\\datas\\flinksql.csv")
      .build()


    //注册table表
    tableEnvironment.registerTableSource("user_table",tableSource)

    //查询年龄大于18的人
    val resultTable1: Table = tableEnvironment.scan("user_table").filter("age >18")

    val resultTable2: Table = tableEnvironment.sqlQuery("select * from user_table where age > 18")

    val sink = new CsvTableSink("F:\\开课吧 大数据\\flink\\Flink实时数仓课件\\数据\\datas\\out\\sink.csv",
      "====", 1, WriteMode.NO_OVERWRITE)
    resultTable2.writeToSink(sink)
    environment.execute()
  }

}
