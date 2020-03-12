package com.lhh.TableSql.demo01
import org.apache.flink.table.api._
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/8 16:13
  * @version 0.0.1
  */
object DStream2Table {
  def main(args: Array[String]): Unit = {

    val executionEnvironment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnvironment: StreamTableEnvironment = StreamTableEnvironment.create(executionEnvironment)

    val socketData: DataStream[String] = executionEnvironment.socketTextStream("hadoop100",8888)

    val userDataStream: DataStream[User] = socketData.map(line => {
      val strings: Array[String] = line.split(",")
      User(strings(0).toInt, strings(1), strings(2).toInt)
    })

    tableEnvironment.registerDataStream("user1",userDataStream)

    //得到一个符合条件的表
    val userTable: Table = tableEnvironment.sqlQuery("select id, name, age from user1")

    val sink1 = new CsvTableSink("F:\\开课吧 大数据\\flink\\Flink实时数仓课件\\数据\\datas\\out\\sink2.csv",
      "====", 1, WriteMode.NO_OVERWRITE)
    userTable.writeToSink(sink1)


    //表如果再转成流有两种方式
    //使用append模式将Table转换成为dataStream，不能用于sum，count，avg等操作，只能用于添加数据操作

    val appendStream: DataStream[User] = tableEnvironment.toAppendStream[User](userTable)

    val retractStream: DataStream[(Boolean, User)] = tableEnvironment.toRetractStream[User](userTable)

    executionEnvironment.execute()
  }

}
case class User(id: Int,name : String, age: Int)
