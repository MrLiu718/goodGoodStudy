package com.lhh.code

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow
import org.apache.flink.util.Collector

object WindowCode {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = env.socketTextStream("node01",9000)
    //timeWindowMt(socketStream)
    countWindowMt(socketStream)

    env.execute()
  }

  def timeWindowMt(socketStream : DataStream[String]): Unit ={
    //防止出现隐式转换的错误
    import org.apache.flink.api.scala._
    socketStream.map((_,1)).keyBy(0).timeWindow(Time.seconds(5)).reduce(new ReduceFunction[(String, Int)] {
      override def reduce(value1: (String, Int), value2: (String, Int)): (String, Int) = {
        (value1._1,value2._2 + value1._2)
      }
    }).print()
  }

  def countWindowMt(socketStream : DataStream[String]): Unit ={
    //防止出现隐式转换的错误
    import org.apache.flink.api.scala._
    socketStream.map(x => (1,x.toInt)).keyBy(0).countWindow(5).process(new MyProcessWindowFunction).print()
  }

  class MyProcessWindowFunction extends ProcessWindowFunction[(Int , Int) , Double , Tuple , GlobalWindow]{
    override def process(key: Tuple, context: Context, elements: Iterable[(Int, Int)], out: Collector[Double]): Unit = {
      var totalnum = 0
      var totalcount = 0

      for (elem <- elements) {
        totalnum += 1
        totalcount += elem._2
      }
      out.collect(totalcount/totalnum)
    }
  }


}
