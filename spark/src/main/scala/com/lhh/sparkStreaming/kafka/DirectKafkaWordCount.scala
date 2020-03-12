//package com.lhh.sparkStreaming.kafka
//
//import kafka.serializer.StringDecoder
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.{kafka, _}
//import org.apache.spark.streaming.kafka.KafkaUtils
//
///**
//  * @description
//  * @author: liuhh
//  * @date: Created in 2020/2/20 22:24
//  * @version 0.0.1
//  * 0.8kafka 直连模式
//  */
//object DirectKafkaWordCount {
//  def main(args: Array[String]): Unit = {
//
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    //步骤一：初始化程序入口
//    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("DirectKafkaWordCount")
//    val ssc = new StreamingContext(sparkConf, Seconds(5))
//
//    //设置kafka的参数
//    /**
//      * 要设置关闭自动提交偏移量
//      */
//
//    val kafkaParams =  Map[String, String](
//      "bootstrap.servers"->"192.168.167.254:9092",
//      "group.id" -> "testsparkstreaming",
//      "enable.auto.commit" -> "false"
//    )
//    val topics = "flink".split(",").toSet
//
//    //获取数据源
//   val lines =  KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](
//      ssc,kafkaParams,topics
//    ).map(_._2)
//
//    //处理业务逻辑
//    val resultData = lines.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)
//    //打印结果
//    resultData.print()
//
//    ssc.start()
//    ssc.awaitTermination()
//    ssc.stop()
//  }
//
//}
