package com.lhh.api.HotItem


import java.sql.Timestamp

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.java.tuple.{Tuple, Tuple1}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

case class ItemViewCount( itemId: Long, windowEnd: Long, count: Long )
object HotItems {
  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //设置并行度
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.readTextFile("D:\\practise-code\\goodGoodStudy\\flink1.9\\src\\main\\resources\\UserBehavior.csv")
      .map(line=>{
        val lineArray = line.split(",")
        UserBehavior(lineArray(0).toLong,lineArray(1).toLong,lineArray(2).toInt,lineArray(3),lineArray(4).toLong)
      })
      .assignAscendingTimestamps(_.timestamp*1000)
      .filter(_.behavior == "pv")
      .keyBy("itemId")
      .timeWindow(Time.hours(1),Time.minutes(5))
      .aggregate(new CountAgg,new WindowResultFunction)
      .keyBy("windowEnd")
      .process(new TopNHotItems(3) )
      .print()

    env.execute("Hot Item Job")
  }

  class CountAgg extends AggregateFunction[UserBehavior,Long,Long]{
    override def createAccumulator(): Long = 0L

    override def add(in: UserBehavior, acc: Long): Long = acc + 1

    override def getResult(acc: Long): Long = acc

    override def merge(acc: Long, acc1: Long): Long = acc + acc1
  }

  class WindowResultFunction extends WindowFunction[Long,ItemViewCount ,Tuple , TimeWindow] {
    override def apply(key: Tuple, window: TimeWindow, input: Iterable[Long], out: Collector[ItemViewCount]): Unit = {
      val itemId = key.asInstanceOf[Tuple1[Long]].f0
      val count = input.iterator.next()
      out.collect(ItemViewCount(itemId,window.getEnd,count))
    }
  }

  class TopNHotItems(topSize: Int) extends KeyedProcessFunction[Tuple, ItemViewCount, String]{
    private var itemState: ListState[ItemViewCount] = _

    //初始化itemState
    override def open(parameters: Configuration): Unit = {
      super.open(parameters)

      val itemViewCount = new ListStateDescriptor[ItemViewCount]("itemViewCount",classOf[ItemViewCount])

      itemState = getRuntimeContext.getListState(itemViewCount)
    }

    override def processElement(i: ItemViewCount, context: KeyedProcessFunction[Tuple, ItemViewCount, String]#Context, collector: Collector[String]): Unit = {

      itemState.add(i)

      //注册了定时器 窗口结束后一毫秒进行计算
      context.timerService().registerEventTimeTimer(i.windowEnd + 1)
    }

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Tuple, ItemViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {

      val buffer: ListBuffer[ItemViewCount] = ListBuffer()

      import  scala.collection.JavaConversions._

      for(item <- itemState.get){

        buffer += item
      }

      itemState.clear()
      val sortedItems = buffer.sortBy(_.count)(Ordering.Long.reverse).take(topSize)

      val result: StringBuilder = new StringBuilder

      result.append("====================================\n")
      result.append("时间：").append(new Timestamp(timestamp - 1)).append("\n")

      for(i <- sortedItems.indices){

        val currentItem: ItemViewCount = sortedItems(i)
        result.append("No").append(i+1).append(":")
          .append("  商品ID=").append(currentItem.itemId)
          .append("  浏览量=").append(currentItem.count).append("\n")
      }
      result.append("====================================\n\n")

      Thread.sleep(1000)
      out.collect(result.toString)
    }
  }

}
