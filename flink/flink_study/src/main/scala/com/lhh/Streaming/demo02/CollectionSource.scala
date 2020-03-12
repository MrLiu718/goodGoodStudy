package com.lhh.Streaming.demo02

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/1 22:07
  * @version 0.0.1
  *
  */
object CollectionSource {

  def main(args: Array[String]): Unit = {
    //从一个已经存在的集合当中创建数据源
    //读取hdfs上面的文件进行处理
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import  org.apache.flink.api.scala._


    val collectionStream: DataStream[String] = environment.fromElements("hello world","test flink","flink spark")

    val resultStream: DataStream[(String, Int)] = collectionStream.flatMap(x => x.split(" ")).map(x =>(x,1)).keyBy(0).sum(1)

    resultStream.print()

    environment.execute()
  }
}
