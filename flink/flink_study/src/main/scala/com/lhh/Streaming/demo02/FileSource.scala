package com.lhh.Streaming.demo02

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/29 23:43
  * @version 0.0.1
  * 读取hdfs上的数据 写入到hdfs
  */
object FileSource {

  def main(args: Array[String]): Unit = {
    //程序入口
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    import org.apache.flink.api.scala._
    //读取数据
    val fileData: DataStream[String] = environment.readTextFile("hdfs://hadoop100:8020/flink_input/")

    val resuleData: DataStream[(String, Int)] = fileData.flatMap(_.split(" ")).map((_,1)).keyBy(0).sum(1)

    resuleData.writeAsText("hdfs://hadoop100:8020/out_result/out_file1").setParallelism(1)

    environment.execute()
  }

}
