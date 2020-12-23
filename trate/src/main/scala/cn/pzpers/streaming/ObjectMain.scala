package cn.pzpers.streaming

import cn.pzpers.pros.CdcBusinessHandlers
import cn.pzpers.utils.{ConfigManager, OffsetManager}
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Milliseconds, Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

object ObjectMain {
  private val conf = ConfigManager
  def main(args: Array[String]): Unit = {
    val batchDuration = conf.getInt("batchDuration")
    val checkpointDir = conf.getString("checkpointDir")
    val checkPointInterval = conf.getInt("checkpointInterval")
    val tpName=args(0)

    // kafka
    val brokers = conf.getString("kafka.brokers")
    val zkConnect = conf.getString("kafka.zookeeper.connect")
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers,
      "auto.offset.reset" -> "smallest",
      "zookeeper.connect" -> zkConnect)

    val fromOffsets: Map[TopicAndPartition, Long] = OffsetManager.getTopicOffsetsFromRedisV2(tpName)

  }

  //sparkstreamin初始化
  def setupSsc(batchDuration: Int,
               checkpointDir: String,
               fromOffsets: Map[TopicAndPartition, Long],
               kafkaParams: Map[String, String],
               checkPointInterval: Int,tpName:String): StreamingContext = {
    // 创建sparkConf
    val sparkConf = new SparkConf()
    // .setMaster("local[*]")
    //.setAppName(this.getClass.getName)
    sparkConf
      .set("spark.streaming.kafka.maxRetries", "5")
      .set("spark.shuffle.blockTransferService", "nio")
      // 默认无上限
      .set("spark.streaming.kafka.maxRatePerPartition", "500")
      .set("spark.locality.wait", "10")
      .set("spark.locality.wait.process", "10")
      .set("spark.locality.wait.node", "10")
      .set("spark.locality.wait.rack", "10")

    // 配置ss的批次时间
    val sc = new SparkContext(config = sparkConf)
    val ssc = new StreamingContext(sc, Milliseconds(batchDuration))

    // 配置ss的checkpoint文件的位置
    ssc.checkpoint(checkpointDir)
    val stream: InputDStream[(String, String, Long)] =
      KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder,(String,String,Long)](
        ssc,kafkaParams,fromOffsets,(mmd:MessageAndMetadata[String, String]) =>
          (mmd.key(), mmd.message(), mmd.offset))
    // 配置DStream checkpoint的时间间隔
    stream.checkpoint(Seconds(checkPointInterval))
    // 操作DStream
    // 执行业务更新操作
    CdcBusinessHandlers.handleAllTablesCdc(stream)
    // 更新 topic 的偏移量
    OffsetManager.updateTopicsOffsets2Redis(tpName,stream)
    // ssc.remember(Seconds(checkPointInterval))
    ssc
  }
}
