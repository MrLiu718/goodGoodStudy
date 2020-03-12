package com.lhh.Streaming.demo03

import java.{lang, util}

import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala.{DataStream, SplitStream, StreamExecutionEnvironment}
import org.apache.log4j.{Level, Logger}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/2 10:57
  * @version 0.0.1
  *
  */
object SplitStream {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    //获取程序入口类
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    //导入隐式转换的包
    import org.apache.flink.api.scala._

    //初始的流  ， 将元素当中包含hello的作为一个流，不包含hello的作为另外一个流
    val sourceStream: DataStream[String] = environment.fromElements("hello,world","spark,flink","hadoop,hive")

    val splitStreamData: SplitStream[String] = sourceStream.split(new OutputSelector[String] {
      override def select(out: String): lang.Iterable[String] = {
        val strings = new util.ArrayList[String]()
        if (out.contains("hello")) {
          strings.add("hello")
        }
        else {
          strings.add("other")
        }
        strings
      }
    })
    val resultData: DataStream[String] = splitStreamData.select("other")

    resultData.print().setParallelism(2)

    environment.execute()

  }

}
