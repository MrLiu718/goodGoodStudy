package com.lhh.sparkStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming._

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/19 10:55
  * @version 0.0.1
  */
object windowsOptions {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf().setAppName("windows").setMaster("local[2]")
    val ssc = new StreamingContext(new SparkContext(conf),Seconds(1))

    val inputData = ssc.socketTextStream("hadoop100",8888)
      .flatMap(_.split(" "))
      .map((_,1))
      .reduceByKeyAndWindow((x:Int,y:Int) => x+y, Seconds(4),Seconds(2))
    //数据打印
    inputData.print()

    ssc.start()
    ssc.awaitTermination()
    ssc.stop()

  }

}
