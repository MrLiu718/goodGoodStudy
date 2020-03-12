//package com.lhh.sparkStreaming.kafka
//
//import kafka.serializer.StringDecoder
//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.SparkConf
//import org.apache.spark.storage.StorageLevel
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
///**
//  * @description
//  * @author: liuhh
//  * @date: Created in 2020/2/20 21:58
//  * @version 0.0.1
//  *          基于Receiver的方式去实现整合
//  *          0.8版本
//  *          简单知道就可以，因为这个在工作不用
//  */
//object Kafka08 {
//
//  def main(args: Array[String]): Unit = {
//    //设置日志格式
//    Logger.getLogger("org").setLevel(Level.ERROR)
//    //程序入口
//    val sparkConf = new SparkConf().setMaster("local[2]").setAppName("StreamingKafkaApp02")
//    val ssc = new StreamingContext(sparkConf, Seconds(10))
//
//    //设置kafka参数
//    val kafkaParmams = Map[String,String](
//      "zookeeper.connect"->"hadoop100:2181,hadoop110:2181,hadoop120:2181",
//      "group.id"->"testflink"
//    )
//
//    val topics = "flink".split(",").map((_,1)).toMap
//
//    //步骤二：获取数据源
//    //默认只会有一个receiver（3 consumer）
//    //
//       val lines = KafkaUtils.createStream[String,String,StringDecoder,StringDecoder](
//             ssc,kafkaParmams,topics, StorageLevel.MEMORY_AND_DISK_SER_2)
//
//    val kafkaStreams = (1 to 20).map(_ => {
//      KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
//        ssc, kafkaParmams, topics, StorageLevel.MEMORY_AND_DISK_SER)
//    })
//
//    val streamsData = ssc.union(kafkaStreams)
//
//    //处理业务代码
//    val resultData = streamsData.map(_._2).flatMap(_.split(",")).map((_,1)).reduceByKey(_+_).print()
//
//    ssc.start()
//    ssc.awaitTermination()
//    ssc.stop()
//
//
//
//
//  }
//}
