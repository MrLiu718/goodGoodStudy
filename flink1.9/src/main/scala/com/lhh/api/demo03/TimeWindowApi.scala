package com.lhh.api.demo03

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/20 22:03
  * @version 0.0.1
  */
object TimeWindowApi {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val inputDs: DataStream[String] = environment.socketTextStream("hadoop100",9999)

  }

}
