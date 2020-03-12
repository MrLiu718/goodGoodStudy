package com.lhh.Streaming.demo07

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/4 12:13
  * @version 0.0.1
  */
object FlinkKafkaSource {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._

    //checkpoint配置
    environment.enableCheckpointing(100);
    environment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
    environment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500);
    environment.getCheckpointConfig.setCheckpointTimeout(60000);
    environment.getCheckpointConfig.setMaxConcurrentCheckpoints(1);
    environment.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);

    val topic = "kfk_flink01"
    val properties = new Properties()
    properties.setProperty("bootstrap.servers","hadoop100:9092")
    properties.setProperty("group.id","con1")
    properties.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    val kafkaSoruce: FlinkKafkaConsumer011[String] = new FlinkKafkaConsumer011[String](topic,new SimpleStringSchema(),properties)


    kafkaSoruce.setCommitOffsetsOnCheckpoints(true)
    environment.setStateBackend(new RocksDBStateBackend("hdfs://hadoop100:8020/flink_kafka/checkpoints",true))

    val resultData: DataStream[String] = environment.addSource(kafkaSoruce)

    resultData.print()
    environment.execute()
  }
}
