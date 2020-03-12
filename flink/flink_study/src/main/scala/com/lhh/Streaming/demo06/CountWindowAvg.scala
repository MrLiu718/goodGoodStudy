package com.lhh.Streaming.demo06

import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector
import org.apache.log4j.{Level, Logger}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/2 16:42
  * @version 0.0.1
  * 去最近5条数据的平均值
  */
object CountWindowAvg {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val socketData: DataStream[String] = environment.socketTextStream("hadoop100",8888)

    socketData.map(x => (1,x.toInt)).keyBy(0).countWindow(5)
      .process(new MyProcessWindowFunction)
      .print()

    environment.execute()
  }
}

class MyProcessWindowFunction extends ProcessWindowFunction[(Int,Int),Double,Tuple,GlobalWindow]{
  override def process(key: Tuple, context: Context, elements: Iterable[(Int, Int)], out: Collector[Double]): Unit = {
    //用于统计一共有多少条数据
    var totalNum:Int = 0
    //用于定义我们所有数据的累加的和
    var totalResult:Int = 0
    for(element <- elements){
      totalNum += 1
      totalResult += element._2
    }
    val resultData : Double= totalResult/totalNum
    out.collect(resultData)
  }
}
