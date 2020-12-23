package cn.pzpers.utils

import kafka.common.TopicAndPartition
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.HasOffsetRanges
import scalikejdbc.{DB, SQL}

import scala.collection.JavaConversions._
import scala.collection.mutable

/**
  * 负责topic的offset获取与更新
  */
object OffsetManager {
  private val conf = ConfigManager
  def getTopicOffsetsFromRedis(tpName:String):Map[TopicAndPartition,Long]= {
    //val map:java.util.Map[String,String] = RedisUtil.hget(Constants.KAFKA_TOPIC_OFFSETS)
    val map:java.util.Map[String,String] = RedisSentinelUtils.hget(tpName)
    //从数据库中读取kafka偏移量
    val mapBuffer = new mutable.HashMap[TopicAndPartition,Long]()
    for (entry <- map.entrySet()){
      val fields = entry.getKey.split(Constants.SEPARATOR)
      val topic = fields(0)
      val partition = fields(1).toInt
      val offset = entry.getValue.toLong
      mapBuffer += (TopicAndPartition(topic,partition) -> offset)
    }
    val fromOffsets = mapBuffer.toMap
    fromOffsets
  }

  def getTopicOffsetsFromRedisV2(tpNames:String):Map[TopicAndPartition,Long]= {
    val tpNameArr = tpNames.split(",")

    val partitionToLongs: Array[Map[TopicAndPartition, Long]] = tpNameArr.map(tpName => {
      getTopicOffsetsFromRedis(tpName)
    })
    val map: Map[TopicAndPartition, Long] = partitionToLongs.flatten.toMap
    map
  }
  /**
    * 更新 offset 到 redis 中
    * @param stream 输入流
    *               */

 /* def updateTopicsOffsets2Redis(stream: InputDStream[(String, String, Long)]) = {

    stream.foreachRDD(rdd => {
      // 获取偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // 更新偏移量
      offsetRanges.foreach { offsetRange => {
        val mapBuffer = new mutable.HashMap[String,String]()
        mapBuffer += (offsetRange.topic + Constants.SEPARATOR + offsetRange.partition -> offsetRange.untilOffset.toString)
        RedisPoolUtils.flush2Redis(Constants.KAFKA_TOPIC_OFFSETS, mapBuffer)
      }
      }
    })
  }*/
  def updateTopicsOffsets2Redis(tpName:String ,stream: InputDStream[(String, String, Long)]) = {

    stream.foreachRDD(rdd => {
      // 获取偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // 更新偏移量
      offsetRanges.foreach { offsetRange => {
        val mapBuffer = new mutable.HashMap[String,String]()
        mapBuffer += (offsetRange.topic + Constants.SEPARATOR + offsetRange.partition -> offsetRange.untilOffset.toString)
        val tpName = offsetRange.topic
        RedisSentinelUtils.flush2Redis(tpName, mapBuffer)
      }
      }
    })
  }

  //注册jdbc驱动程序
  @Deprecated
  def getTopicOffsetsFromMySQL:Map[TopicAndPartition,Long]= {
    //注册JDBC驱动程序
    val jdbcDriver = conf.getString("jdbc.driver")
    val jdbcUrl = conf.getString("jdbc.url")
    val jdbcUser = conf.getString("jdbc.user")
    val jdbcPassword = conf.getString("jdbc.password")
    val jdbcOffsetsTable = conf.getString("jdbc.offsets_table")
    SetupJdbc(jdbcDriver, jdbcUrl, jdbcUser, jdbcPassword)

    //从数据库中读取kafka偏移量
    val fromOffsets = DB.readOnly { implicit session =>
      SQL(s"select `topic`, `partition`,`offset` from $jdbcOffsetsTable").
        map { resultSet =>
          TopicAndPartition(resultSet.string(1), resultSet.int(2)) -> resultSet.long(3)
        }.list.apply().toMap
    }
    fromOffsets
  }

  /**
    *  更新 offset 到 MySQL 中
    *
    * @param stream 输入流
    * @param jdbcOffsetsTable 更新的offset表
    */
  @Deprecated
  def updateTopicsOffsets2MySQL(stream: InputDStream[(String, String, Long)], jdbcOffsetsTable: String) = {

    stream.foreachRDD(rdd => {
      // 获取偏移量
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      // 更新偏移量
      DB.localTx { implicit session =>
        offsetRanges.foreach { offsetRange => {
          /*val offsetRows =*/
          SQL(s"update $jdbcOffsetsTable set `offset` = ${offsetRange.untilOffset} where `topic` = '${offsetRange.topic}' and `partition` = ${offsetRange.partition} and `offset` = ${offsetRange.fromOffset}").update.apply()
          /*if (offsetRows != 1) {
            throw new Exception(
              s"""
Got $offsetRows rows affected instead of 1 when attempting to update offsets for
${offsetRange.topic} ${offsetRange.partition} ${offsetRange.fromOffset} -> ${offsetRange.untilOffset}
Was a partition repeated after a worker failure?
""")
          }*/
        }
        }
      }
    })
  }

  def main(args: Array[String]): Unit = {
    val partitionToLong = getTopicOffsetsFromRedisV2("mateconfirm,creditdebts")
    println(partitionToLong)

  }
}