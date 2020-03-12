package com.lhh.Streaming.demo06

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.log4j.{Level, Logger}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/2 16:13
  * @version 0.0.1
  * 读取socket5秒内的累加值
  */
object TimeWindows {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._

    val socketData: DataStream[String] = environment.socketTextStream("hadoop100",8888)
    socketData.map(x => (1,x.toInt)).keyBy(0)
      .timeWindow(Time.seconds(5))
      .reduce(new ReduceFunction[(Int, Int)] {
        override def reduce(t: (Int, Int), t1: (Int, Int)): (Int, Int) = {

          val resultNum = t._2 + t1._2
          (t._1,resultNum)
        }
      })
      .print()

    environment.execute()

  }
}
