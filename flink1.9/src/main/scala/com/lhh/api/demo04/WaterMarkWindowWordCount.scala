package com.lhh.api.demo04

import org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/24 20:48
  * @version 0.0.1
  */
object WaterMarkWindowWordCount {
  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setParallelism(1)
    //设置事件处理时间
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val outputTag = new OutputTag[(String,Long)]("lateDatas"){}

    val inputData: DataStream[String] = environment.socketTextStream("hadoop100",9999)

    inputData.map(x => (x.split(",")(0),x.split(",")(1).toLong))
      .assignTimestampsAndWatermarks(new MyOwnWaterMark1)
      .keyBy(0)
      .timeWindow(Time.seconds(3))
      .process(new MySumFunction1)
      .print()
      .setParallelism(1)

    environment.execute()
  }
}
class MySumFunction1 extends ProcessWindowFunction[(String,Long),String,Tuple,TimeWindow]{
  private val dateFormat: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")

  override def process(key: Tuple, context: Context, elements: Iterable[(String, Long)], out: Collector[String]): Unit = {
    println("程序处理时间为" + dateFormat.format(context.currentProcessingTime))
    println("window 开始时间为" + dateFormat.format(context.window.getStart))

    val strings = new ListBuffer[String]
    for(eachElement <- elements){
      strings.+=(eachElement.toString + "|" + dateFormat.format(eachElement._2))
    }
    out.collect(strings.toString())

    println("window结束时间为" + dateFormat.format(context.window.getEnd))
  }
}
class MyOwnWaterMark1 extends AssignerWithPeriodicWatermarks[(String,Long)]{
  private val format: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
  private var currentMaxEventTime :Long = 0L
  private var maxOutOfOrderness:Long = 10000L // 最大允许的乱序时间 10 秒
  override def getCurrentWatermark: Watermark = {
    new Watermark(currentMaxEventTime - maxOutOfOrderness)
  }

  override def extractTimestamp(element: (String, Long), l: Long): Long = {
    val eventTime: Long = element._2
    currentMaxEventTime = Math.max(currentMaxEventTime,eventTime)
    var log:String = "event数据为： " + element + "|" + "数据event_time为："+format.format(element._2)  + "|" + "当前数据最大event_time为："+format.format(currentMaxEventTime) + "|" + "当前watermark值为："+format.format(getCurrentWatermark().getTimestamp())
    println(log)
    return eventTime
  }
}