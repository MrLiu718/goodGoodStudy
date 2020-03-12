package com.lhh.Streaming.demo02

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/29 23:29
  * @version 0.0.1
  * 获取socket数据源
  */
object SocketSouce {

  def main(args: Array[String]): Unit = {

    //获取程序入口
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //获取数据源
    val socketData: DataStream[String] = environment.socketTextStream("hadoop100",8888)

    import org.apache.flink.api.scala._
    //获得数据
    val resultData: DataStream[(String, Int)] = socketData
      .flatMap(_.split(" "))
      .map((_,1)).keyBy(0)
      .timeWindow(Time.seconds(3),Time.seconds(5))
      .sum(1)

    //打印数据
    resultData.print()

    //执行程序
    environment.execute()
  }
}
