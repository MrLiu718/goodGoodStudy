package com.lhh.util

import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}

object redisUtils {

  //获取redisSink
  def  getRedisSink(): RedisSink[Tuple2[String,String]]={
    val builder = new FlinkJedisPoolConfig.Builder

    builder.setHost("50.2.68.119")
    builder.setPort(6379)

    builder.setTimeout(5000)
    builder.setMaxTotal(50)
    builder.setMaxIdle(10)
    builder.setMinIdle(5)

    val config = builder.build()

    val redisSink = new RedisSink[(String, String)](config,new MyRedisMapper)
    redisSink
  }

  class MyRedisMapper  extends RedisMapper[Tuple2[String,String]]{
    override def getCommandDescription: RedisCommandDescription = {
      new RedisCommandDescription(RedisCommand.SET)
    }

    override def getKeyFromData(t: (String, String)): String = {
      t._1
    }

    override def getValueFromData(t: (String, String)): String = {
      t._2
    }
  }

}
