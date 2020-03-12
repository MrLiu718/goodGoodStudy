package com.lhh.house

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/9 15:23
  * @version 0.0.1
  */
object KafkaJsonSource {
  def main(args: Array[String]): Unit = {
    val executionEnvironment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //隐式转换
    import org.apache.flink.api.scala._
    //checkpoint配置//checkpoint配置
    executionEnvironment.enableCheckpointing(100)
    executionEnvironment.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    executionEnvironment.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    executionEnvironment.getCheckpointConfig.setCheckpointTimeout(60000)
    executionEnvironment.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    executionEnvironment.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    executionEnvironment.setStateBackend(new RocksDBStateBackend("hdfs://hadoop100:8020/flink_kafka/checkpoints", true))


    val props = new Properties
    props.put("bootstrap.servers", "hadoop100:9092,hadoop100:9092,hadoop100:9092")
    props.put("zookeeper.connect", "hadoop100:2181,hadoop110:2181,hadoop120:2181")
    props.put("group.id", "flinkHouseGroup1")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "latest")
    props.put("flink.partition-discovery.interval-millis", "30000")
    val kafkaSouce = new FlinkKafkaConsumer011[String]("flink_house",new SimpleStringSchema(),props)

    kafkaSouce.setCommitOffsetsOnCheckpoints(true)

    val resultDStream: DataStream[String] = executionEnvironment.addSource(kafkaSouce)

    val orderresult: DataStream[OrderObj] = resultDStream.map(x => {
      //将数据进行解析
      val jsonObj: JSONObject = JSON.parseObject(x)

      val database: AnyRef = jsonObj.get("database")
      val table: AnyRef = jsonObj.get("table")
      val `type`: AnyRef = jsonObj.get("type")
      val string: String = jsonObj.get("data").toString

      OrderObj(database.toString, table.toString, `type`.toString, string)
    })
    orderresult.addSink(new HBaseSinkFunction)
    executionEnvironment.execute()


  }

}

case class OrderObj (database:String,table:String,`type`:String,data:String) extends Serializable