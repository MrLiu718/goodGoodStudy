package com.lhh.Streaming.demo03

import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/1 22:20
  * @version 0.0.1
  */
object ConnectStream {
  def main(args: Array[String]): Unit = {
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._
    //第一个流，是string类型
    val firstStream: DataStream[String] = environment.fromElements("hello,world","spark,flink")

    //第二个流，是int类型
    val secondStream: DataStream[Int] = environment.fromElements(1,2)
    //通过connect合并两个不同类型的流
    val resultStream: ConnectedStreams[String, Int] = firstStream.connect(secondStream)

    val result: DataStream[Any] = resultStream.map(x => {x + "abs"},y=>{y * 10})

    result.print().setParallelism(3)

    environment.execute()
  }

}
