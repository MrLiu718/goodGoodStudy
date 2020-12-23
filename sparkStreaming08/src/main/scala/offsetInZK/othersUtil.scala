package offsetInZK

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import kafka.utils.{ZKGroupTopicDirs, ZkUtils}
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}

object othersUtil {
  def kafkaAndZookeeper(ssc: StreamingContext): InputDStream[(String, String)] = {

    val group = "DirectAndZk"
    val topic = "ainZk"
    val brokerList = "data01.bigdata.com:9092,name01.bigdata.com:9092,name01.bigdata.com:9092"
    val zkQuorum = "data01.bigdata.com:2181,name01.bigdata.com:2181,name01.bigdata.com:2181"
    val topics: Set[String] = Set(topic)
    val topicDirs = new ZKGroupTopicDirs(group, topic)
    val zkTopicPath = s"${topicDirs.consumerOffsetDir}"

    val kafkaParams = Map(
      "metadata.broker.list" -> brokerList,
      "group.id" -> group,
      "auto.offset.reset" -> kafka.api.OffsetRequest.LargestTimeString
    )

    val zkClient = new ZkClient(zkQuorum)
    val children = zkClient.countChildren(zkTopicPath)
    var kafkaStream: InputDStream[(String, String)] = null
    var fromOffsets: Map[TopicAndPartition, Long] = Map()

    if (children > 0) {
      for (i <- 0 until children) {
        val partitionOffset = zkClient.readData[String](s"$zkTopicPath/${i}")
        val tp = TopicAndPartition(topic, i)
        fromOffsets += (tp -> partitionOffset.toLong)
      }

      val messageHandler = (mmd: MessageAndMetadata[String, String]) => (mmd.key(), mmd.message())
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc,
        kafkaParams, fromOffsets, messageHandler)
    } else {
      kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    }

    var offsetRanges = Array[OffsetRange]()

    kafkaStream.map(_._2)
      .foreachRDD(x => {
        println("****************************************")
        x.foreach(a => println(a))
        //println(x.)
        println("****************************************")
      })

    kafkaStream.foreachRDD { kafkaRDD =>

      offsetRanges = kafkaRDD.asInstanceOf[HasOffsetRanges].offsetRanges
      for (o <- offsetRanges) {
        val zkPath = s"${topicDirs.consumerOffsetDir}/${o.partition}"
        ZkUtils.updatePersistentPath(zkClient, zkPath, o.untilOffset.toString)
      }
    }

    //val streamrdd = kafkaStream.map()
    //streamrdd
    kafkaStream
  }

}
