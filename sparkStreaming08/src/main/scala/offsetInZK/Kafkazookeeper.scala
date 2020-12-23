package offsetInZK

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Kafkazookeeper {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.WARN)

    //创建SparkStreaming的上下文
    val conf = new SparkConf().setAppName("KafkaDirectWordCount").setMaster("local[6]")
    val ssc = new StreamingContext(conf,Seconds(3))

    othersUtil.kafkaAndZookeeper(ssc)
    ssc.start()
    ssc.awaitTermination()


  }

}
