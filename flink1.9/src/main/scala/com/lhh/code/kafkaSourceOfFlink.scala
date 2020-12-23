package com.lhh.code

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}

object kafkaSourceOfFlink {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    //checkpoint配置
    env.enableCheckpointing(1000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

    env.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    val topic = "test"
    val prop = new Properties()
    prop.setProperty("bootstrap.servers","50.2.68.116:9092,50.2.68.117:9092,50.2.68.121:9092")
    prop.setProperty("group.id","con1")
    prop.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    prop.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    var kafkaSoruce: FlinkKafkaConsumer011[String] = new FlinkKafkaConsumer011[String](topic, new SimpleStringSchema(), prop)
    kafkaSoruce.setCommitOffsetsOnCheckpoints(true)
    //从头开始消费
    kafkaSoruce.setStartFromEarliest()
    env.setStateBackend(new RocksDBStateBackend("hdfs://50.2.68.121:8020/swhq/flink_kafka/checkpoints",true))
    val result: DataStream[String] = env.addSource(kafkaSoruce)

    val topic1 = "test-fl"
    val prop1 = new Properties()
    prop1.setProperty("bootstrap.servers","50.2.68.116:9092,50.2.68.117:9092,50.2.68.121:9092")
    prop1.setProperty("group.id","con2")
    //设置事务超时时间
    prop1.setProperty("transaction.timeout.ms",60000*15+"")
    val sinkData = new FlinkKafkaProducer011[String](topic1, new KeyedSerializationSchemaWrapper[String]
    (new SimpleStringSchema()), prop1, FlinkKafkaProducer011.Semantic.EXACTLY_ONCE)

    result.print()
    //result.addSink(sinkData)

    env.execute()


  }

}
