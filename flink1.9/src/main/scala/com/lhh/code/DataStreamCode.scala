package com.lhh.code

import java.{lang, util}

import com.lhh.util.{MyPartitioner, redisUtils}
import org.apache.flink.streaming.api.collector.selector.OutputSelector
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object DataStreamCode {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    //获取第一个dataStream
    val resultDataStream: DataStream[String] = env.fromElements("hello world","test spark","spark flink","hello bigData")

    //切分流
    /*val splitList = resultDataStream.split(new OutputSelector[String] {
      override def select(out: String): lang.Iterable[String] = {
        val arrayList = new util.ArrayList[String]()
        if (out.contains("world")) {
          //将数据加入名称为hello的流中
          arrayList.add("hello")
        } else {
          arrayList.add("other")
        }
        arrayList
      }
    })*/

    /*val splitList = resultDataStream.filter(_.contains("hello"))
        .rebalance
        .flatMap(_.split(" "))
        .map((_,1))
        .keyBy(0)
        .sum(1)*/

    val value = resultDataStream.partitionCustom(new MyPartitioner,x => x+"")


    value.map(x => {
      println("数据Key为： " + x + " 当前线程ID： "+Thread.currentThread().getId)

      (x.split(" ")(0),x.split(" ")(1))
    }).addSink(redisUtils.getRedisSink())



    //value.print()

    env.execute()

//    splitList.print().setParallelism(1)
//    env.execute()
  }



}
