package cn.pzpers.utils

import org.apache.spark.Logging

class KafkaMessageProducer(brokers: String, topic: String) extends Logging {
  private val brokerList = brokers
  private val targetTopic = topic


}
