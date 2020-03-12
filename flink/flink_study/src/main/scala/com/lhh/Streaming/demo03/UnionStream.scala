package com.lhh.Streaming.demo03

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/1 22:10
  * @version 0.0.1
  * 合并多个不同的流
  */
object UnionStream {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._
    //第一个流
    val firstStream: DataStream[String] = environment.fromElements("hello,world","flink,spark")
    //第二个流
    val secondStream: DataStream[String] = environment.fromElements("second,stream","hadoop,hive")
    //第三个流
    val threeStream: DataStream[String] = environment.fromElements("three,stream","hbase,es")
    //合并三个流
    val result: DataStream[String] = firstStream.union(secondStream).union(threeStream)
    result.print().setParallelism(1)

    environment.execute()
  }

}
