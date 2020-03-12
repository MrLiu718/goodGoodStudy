package com.lhh.sparkStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/18 22:12
  * @version 0.0.1
  */
object UpdateStateBykeyWordCount {
  def main(args: Array[String]): Unit = {
    //设置日志格式
    Logger.getLogger("org").setLevel(Level.ERROR)

    //添加程序入口
    val conf = new SparkConf().setAppName("UpdataState").setMaster("local[2]")
    val ssc = new StreamingContext(new SparkContext(conf),Seconds(2))

    //添加checkpoint
    ssc.checkpoint("hdfs://hadoop100:8020/streamingcheckpoint")

    //数据输入
    val Dfream1 = ssc.socketTextStream("hadoop100",8888)
      .flatMap(_.split(" ")).map((_,1))
      .updateStateByKey((value: Seq[Int],state:Option[Int]) => {
        val currentsum = value.sum
        val lastSum = state.getOrElse(0)
        Some(currentsum + lastSum)
      })

    /**
      * 数据的输出
      */
     Dfream1.print()

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()



  }

}
