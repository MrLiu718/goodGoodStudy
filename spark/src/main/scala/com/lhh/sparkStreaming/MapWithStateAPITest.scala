package com.lhh.sparkStreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming._

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/18 22:41
  * @version 0.0.1
  */
object MapWithStateAPITest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("mapWithStateAPITest")
    val ssc = new StreamingContext(new SparkContext(conf),Seconds(5))

    //设置checkpoint文件目录
    ssc.checkpoint("hdfs://hadoop100:8020/streamingcheckpoint/Streaming1")

    // currentBatchTime : 表示当前的Batch的时间
    // key: 表示需要更新状态的key
    // value: 表示当前batch的对应的key出现的次数（上一次出现的次数）
    // currentState: 对应key的当前的状态 共出现几次
    val stateSpec = StateSpec.function((currentTime: Time, key: String, value: Option[Int], currentState: State[Long]) => {
      val sum = value.getOrElse(0).toLong + currentState.getOption().getOrElse(0L)
      val output = (key,sum)

      if (!currentState.isTimingOut()) currentState.update(sum)
      Some(output)
    }).numPartitions(2).timeout(Seconds(30))

    val Dfream = ssc.socketTextStream("hadoop100",8888).flatMap(_.split(" ")).map((_,1))
      .mapWithState(stateSpec)

    Dfream.print()

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()

  }

}
