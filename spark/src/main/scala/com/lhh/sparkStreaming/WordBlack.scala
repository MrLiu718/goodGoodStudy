package com.lhh.sparkStreaming

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/19 10:08
  * @version 0.0.1
  *
  * 使用transform 实现黑名单过滤
  */
object WordBlack {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetWordCount")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(2))

    /**
      * 读取数据
      * 自己模拟一个黑名单：
      * 各位注意：
      * 这个黑名单，一般情况下，不是我们自己模拟出来，应该是从mysql数据库
      * 或者是Reids 数据库，或者是HBase数据库里面读取出来的。
      */
    val blackData = ssc.sparkContext.parallelize(List("lhh","kuli","hadeng")).map((_,"black"))

    //使用广播变量 广播出去黑名单
    val broadcastData = ssc.sparkContext.broadcast(blackData.collect())

    //输入数据
    val inputDfream = ssc.socketTextStream("hadoop100",8888)
      .flatMap(_.split("_"))
      .map((_,1))

    //输出数据
    val wordBlackOutData = inputDfream.transform(rdd =>{
      val broadValues : RDD[(String,String)]= ssc.sparkContext.parallelize(broadcastData.value)
      val outRdd: RDD[(String,(Int,Option[String]))]= rdd.leftOuterJoin(broadValues)
      outRdd.filter(tuple => {
        tuple._2._2.isEmpty
      }).map(_._1)
    }).map((_,1)).reduceByKey(_+_)

    //打印数据
    wordBlackOutData.print()
    //程序运行
    ssc.start()
    //要等到提交的任务全部执行完才停
    ssc.awaitTermination()
    ssc.stop()
  }

}
