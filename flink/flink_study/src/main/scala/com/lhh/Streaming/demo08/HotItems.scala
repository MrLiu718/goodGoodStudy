package com.lhh.Streaming.demo08

import java.sql.Timestamp
import java.util.Properties

import org.apache.commons.lang.time.FastDateFormat
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.{AssignerWithPeriodicWatermarks, KeyedProcessFunction}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/28 11:00
  * @version 0.0.1
  */
object HotItems {
  def main(args: Array[String]): Unit = {
    //隐式转换
    import org.apache.flink.api.scala._
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    environment.setParallelism(1)

//    val properties = new Properties()
    //    properties.setProperty("bootstrap.servers", "hadoop100:9092,hadoop110:9092,hadoop120:9092")
    //    properties.setProperty("group.id", "consumer-group")
    //    properties.setProperty("key.deserializer",
    //      "org.apache.kafka.common.serialization.StringDeserializer")
    //    properties.setProperty("value.deserializer",
    //      "org.apache.kafka.common.serialization.StringDeserializer")
    //    properties.setProperty("auto.offset.reset", "latest")
    //
    //    val inputDS: DataStream[String] = environment.addSource(new FlinkKafkaConsumer011[String]("hotitems",new SimpleStringSchema(),properties))

    val inputData: DataStream[String] = environment.socketTextStream("hadoop100",9999)
    inputData.map(x =>{
      val s: Array[String] = x.split(",")
      UserBehavior(
        s(0).toLong,
        s(1).toLong,
        s(2).toInt,
        s(3),
        s(4).toLong
      )
    })//.assignAscendingTimestamps(_.timeStamp*1000)
      .assignTimestampsAndWatermarks(new myWaterMark)
      .keyBy(x => x.itemId)
      .timeWindow(Time.seconds(10),Time.seconds(2))
      .aggregate(new CountAgg(),new WindowResultFunction())
      .keyBy(x => x.windowEnd)
      .process(new TopNHostItems(3)).print()
    environment.execute()

  }

}
//生成水印
class myWaterMark extends AssignerWithPeriodicWatermarks[UserBehavior] {
  private val format: FastDateFormat = FastDateFormat.getInstance("HH:mm:ss")
  var currentTimestamp: Long = 0L

  val maxDelayTime = 5000L

  var watermark: Watermark = null
  override def getCurrentWatermark: Watermark = {
    watermark = new Watermark(currentTimestamp - maxDelayTime)
    //print("当前水位线:---------"+watermark)
    watermark
  }

  override def extractTimestamp(t: UserBehavior, l: Long): Long = {
    val timeStamp = t.timeStamp*1000
    //print("当前时间戳:------------"+currentTimestamp)
    currentTimestamp = Math.max(timeStamp, currentTimestamp)
    var log:String = "event数据为： " + t + "|" + "数据event_time为："+format.format(timeStamp)  + "|" + "当前数据最大event_time为："+format.format(currentTimestamp) + "|" + "当前watermark值为："+format.format(getCurrentWatermark().getTimestamp())
    println(log)
    currentTimestamp
  }
}

//输入数据的样例类
case class UserBehavior(userId: Long,
                        itemId: Long,
                        categoryId: Int,
                        behavior: String,
                        timeStamp: Long)

//中间输出的商品浏览量的样例类
case class ItemViewCount(itemId: Long,
                         windowEnd: Long,
                         count: Long)

 //自定义预聚合函数，来一个数据就加一
class CountAgg extends AggregateFunction[UserBehavior,Long,Long]{
  override def createAccumulator(): Long = 0L

  override def add(in: UserBehavior, acc: Long): Long = acc + 1

  override def getResult(acc: Long): Long = acc

  override def merge(acc1: Long, acc2: Long): Long = acc1 + acc2
}

//用于输出窗口的结构
class WindowResultFunction extends WindowFunction[
  Long,
  ItemViewCount,
  Long,
  TimeWindow]{

  override def apply(
                      key: Long,
                      window: TimeWindow,
                      aggregateResult: Iterable[Long],
                      collector: Collector[ItemViewCount]) : Unit = {


    val itemId = key
    val windowEnd = window.getEnd
    val count: Long = aggregateResult.iterator.next()

    collector.collect(ItemViewCount(itemId, windowEnd, count))

  }
}

//自定义process Function
// 求某个窗口中前 N 名的热门点击商品，key 为窗口时间戳，输出为 TopN 的结果字符串
class TopNHostItems(topSize: Int) extends
  KeyedProcessFunction[Long,ItemViewCount,String]{
  //自定义一个list State,用来保存所有的ItemViewCount
  private var itemState : ListState[ItemViewCount] = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    //命名状态变量的名字和状态变量的类型
    val itemList: ListStateDescriptor[ItemViewCount] = new ListStateDescriptor[ItemViewCount] ("itemViewCount",classOf[ItemViewCount])
    //定义状态变量
    itemState = getRuntimeContext.getListState(itemList)

  }
  override def processElement(i: ItemViewCount, context: KeyedProcessFunction[Long, ItemViewCount, String]#Context, collector: Collector[String]): Unit = {
    //每一条数据存入state中
    itemState.add(i)
    // 注册定时器，延时触发；当定时器触发时，当前windowEnd的一组数据应该都到齐，统一排序处理
    // 也就是当程序看到windowend + 1的水位线watermark时，触发onTimer回调函数
    context.timerService().registerEventTimeTimer(i.windowEnd + 1)

  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, ItemViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {
    //定时器触发时，已经收集到所有的数据，首先把所有数据放到一个list中
    val allItems: ListBuffer[ItemViewCount] = new ListBuffer()
    import scala.collection.JavaConversions._
    for(item <- itemState.get){
      allItems += item
    }
    //提前清楚状态中的数据，释放空间
    itemState.clear()

    //按点击量从大到小排序
    val sortedItems: ListBuffer[ItemViewCount]
    = allItems.sortBy(_.count)(Ordering.Long.reverse)take(topSize)

    // 将排名信息格式化成 String, 便于打印
    val result: StringBuilder = new StringBuilder
    result.append("====================================\n")
    result.append("时间: ").append(new Timestamp(timestamp - 1)).append("\n")

    for(i <- sortedItems.indices){
      val currentItem: ItemViewCount = sortedItems(i)
      // e.g.  No1：  商品ID=12224  浏览量=2413
      result.append("No").append(i+1).append(":")
        .append("  商品ID=").append(currentItem.itemId)
        .append("  浏览量=").append(currentItem.count).append("\n")
    }
    result.append("====================================\n\n")

    // 控制输出频率，模拟实时滚动结果
    Thread.sleep(1000)
    out.collect(result.toString())

  }
}