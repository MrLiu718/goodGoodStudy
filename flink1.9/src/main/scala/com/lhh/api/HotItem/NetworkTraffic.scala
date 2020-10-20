package com.lhh.api.HotItem

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

case class ApacheLogEvent( ip: String, userId: String, eventTime: Long, method: String, url: String )

case class UrlViewCount( url: String, windowEnd: Long, count: Long )
object NetworkTraffic {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    env.readTextFile("D:\\practise-code\\goodGoodStudy\\flink1.9\\src\\main\\resources\\apache.log")
      .map(line => {
        val LineArray = line.split(" ")
        val simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
        val timestamp = simpleDateFormat.parse(LineArray(3)).getTime
        ApacheLogEvent( LineArray(0), LineArray(1), timestamp, LineArray(5), LineArray(6) )
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ApacheLogEvent](Time.seconds(10)) {
        override def extractTimestamp(t: ApacheLogEvent): Long = {
          t.eventTime
        }
      })
      .filter(_.method == "GET")
      .keyBy(_.url)
      .timeWindow(Time.minutes(1),Time.seconds(5))
      .aggregate(new CountAgg,new WindowResultFunction)
      .keyBy(_.windowEnd)
      .process(new TopNHotUrls(3))
      .print()

    env.execute("Network Traffic Analysis Job")
  }

  class  CountAgg extends AggregateFunction[ApacheLogEvent, Long, Long]{
    override def createAccumulator(): Long = 0L

    override def add(in: ApacheLogEvent, acc: Long): Long = {
      acc + 1
    }

    override def getResult(acc: Long): Long = acc

    override def merge(acc: Long, acc1: Long): Long = {
      acc + acc1
    }
  }

  class WindowResultFunction extends WindowFunction[Long,UrlViewCount,String,TimeWindow]{
    override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[UrlViewCount]): Unit = {
      val url : String = key
      val count : Long = input.iterator.next()

      out.collect(UrlViewCount(url,window.getEnd,count))
    }
  }

  class TopNHotUrls(topSize: Int) extends KeyedProcessFunction[Long,UrlViewCount,String]{

    //定义state
    lazy val urlState : ListState[UrlViewCount] = getRuntimeContext.getListState(new ListStateDescriptor[UrlViewCount]("urlViewCount",classOf[UrlViewCount]))



    override def processElement(i: UrlViewCount, context: KeyedProcessFunction[Long, UrlViewCount, String]#Context, collector: Collector[String]): Unit = {

      urlState.add(i)
      //设置定时器
      context.timerService().registerEventTimeTimer(i.windowEnd + 10*1000)
    }

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, UrlViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {
      lazy val allUrlViews : ListBuffer[UrlViewCount] = ListBuffer()
      import scala.collection.JavaConversions._

      for(urlView <- urlState.get()){
        allUrlViews += urlView
      }

      urlState.clear()

      val sortedViews = allUrlViews.sortBy(_.count)(Ordering.Long.reverse).take(topSize)

      val result : StringBuffer = new StringBuffer

      result.append("======================================")
      result.append("时间： ").append(new Timestamp(timestamp - 10*1000)).append("\n")

      for (i <- sortedViews.indices){

        val currentUrlView: UrlViewCount = sortedViews(i)

        result.append("No").append(i+1).append(":")
          .append("  URL=").append(currentUrlView.url)
          .append("  流量=").append(currentUrlView.count).append("\n")
      }
      result.append("====================================\n\n")
      Thread.sleep(1000)
      out.collect(result.toString())
    }




  }
}
