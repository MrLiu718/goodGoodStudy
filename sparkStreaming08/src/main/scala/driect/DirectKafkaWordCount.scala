package driect

import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DirectKafkaWordCount {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR )
    //初始化程序入口
    val conf = new SparkConf().setMaster("local[2]").setAppName("DirectKafkaWordCount")
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

    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      context,
      kafakconf,
      topSet).map(_._2)

    val result = lines.flatMap(_.split(","))
      .map((_, 1))
      .reduceByKey(_ + _)

    result.print()

    context.start()

    context.awaitTermination()

    context.stop()



  }

}
