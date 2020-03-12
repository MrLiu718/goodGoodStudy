package com.lhh.Streaming.demo02

import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/1 21:57
  * @version 0.0.1
  *          实现自定义数据源
  */
object MultipartSource {
  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val sourceStream: DataStream[String] = environment.addSource(new MyOwnSource2)

    val resultStream: DataStream[(String, Int)] = sourceStream.flatMap(x => x.split(" ")).map(x =>(x,1)).keyBy(0).sum(1)

    resultStream.print().setParallelism(4)

    environment.execute()
  }

}
class MyOwnSource2 extends RichParallelSourceFunction[String]{
  var isRuning = true

  override def run(sourceContext: SourceFunction.SourceContext[String]): Unit = {
    while (isRuning) {
      sourceContext.collect("hello word")
      Thread.sleep(1000)
    }
  }

  override def cancel(): Unit = {
    isRuning = false
  }
}
