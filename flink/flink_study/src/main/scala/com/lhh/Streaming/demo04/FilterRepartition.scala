package com.lhh.Streaming.demo04

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/2 11:42
  * @version 0.0.1
  */
object FilterRepartition {

  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    val sourceStream: DataStream[String] = environment.fromElements("hello,world","spark,flink","hadoop,hive")

    //过滤数据里面包含hello元素的数据
    val resultStream: DataStream[(String, Int)] = sourceStream.filter(x => x.contains("hello"))
      .rebalance
      .flatMap(_.split(","))
      .map((_,1))
      .keyBy(0)
      .sum(1)

    resultStream.print().setParallelism(1)

    environment.execute()
  }
}
