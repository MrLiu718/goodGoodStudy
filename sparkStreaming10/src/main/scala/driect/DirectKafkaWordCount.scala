package driect

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DirectKafkaWordCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR )
    /**
      * 初始化程序入口
      * local[5] 指的是 设置5个线程  * 的话就是尽可能多的线程
      */
    val conf = new SparkConf().setMaster("local[5]").setAppName("DirectKafkaWordCount")
    conf.set("spark.streaming.kafka.maxRatePerPartition","50")
    val context = new StreamingContext(conf,Seconds(5))

    /**
      * kafka参数
      */
    val kafakconf = Map[String, String](
      "bootstrap.servers" -> "192.168.30.100:9092",
      "group.id" -> "testsparkstreaming",
      "enable.auto.commit" -> "false"
    )

    val topSet = "flink".split(",").toSet

   // KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder]()

//    val lines = KafkaUtils.createDirectStream[String, String](
//      context,
//      kafakconf,
//      topSet)

    //提交



  }

}
