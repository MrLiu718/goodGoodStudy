package driect

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
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
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val context = new StreamingContext(conf,Seconds(5))

    val groupId = "flink_consumer" //注意，这个也就是我们的消费者的名字

    val topics = "flink"

    /**
      * kafka参数
      */
    val kafakconf = Map[String, Object](
      "bootstrap.servers" -> "192.168.30.100:9092",
      "group.id" -> groupId,
      "enable.auto.commit" -> "false",
      //sparkstremaing消费的kafka的一条消息，最大可以多大
      //默认是1M，比如可以设置为10M，生产里面一般都是设置10M。
      "fetch.message.max.bytes"->"209715200",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer]
    )

    val topSet = topics.split(",").toSet

    //获取数据源
    val Stream = KafkaUtils.createDirectStream[String, String](
      context,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topSet, kafakconf)
    )
    val result = Stream.map(_.value())
      .flatMap(_.split(","))
      .map((_, 1))
      .reduceByKey(_ + _)
    result.print()

    context.start()

    context.awaitTermination()

    context.stop()



  }

}
