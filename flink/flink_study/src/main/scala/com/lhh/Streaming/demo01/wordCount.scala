package com.lhh.Streaming.demo01

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/29 21:14
  * @version 0.0.1
  */
object wordCount {

  def main(args: Array[String]): Unit = {

    //构建程序入口
    val environment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._

    //从socket中获取数据

    val dataFream: DataStream[String] = environment.socketTextStream("hadoop100", 8888)


    val result : DataStream[(String, Int)] = dataFream.flatMap(_.split(" "))
      .map((_,1))
      .keyBy(0)
      .sum(1)

    result.print()

    environment.execute()
  }

}
