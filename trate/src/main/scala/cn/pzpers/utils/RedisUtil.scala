package cn.pzpers.utils


import cn.jpzpers.exe.RedisConnectionException
import com.typesafe.config.ConfigFactory
import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis._

import scala.collection.mutable

//redis操作工具类
object RedisUtil {
  private[this] val cluster: JedisCluster = {
    val conf = ConfigFactory.load("application.conf")
    val hostsAndPortsStr = conf.getString("redis.pool.sentinels")
    val maxIdle = conf.getInt("redis.pool.maxIdle")
    val maxTotal = conf.getInt("redis.pool.maxTotal")
    val minIdle = conf.getInt("redis.pool.minIdle")
    val testOnBorrow = conf.getBoolean("redis.pool.testOnBorrow")
    val testOnReturn = conf.getBoolean("redis.pool.testOnReturn")
    val testWhileIdle = conf.getBoolean("redis.pool.testWhileIdle")
    val maxWaitMillis = conf.getLong("redis.pool.maxWaitMillis")
    val minEvictableIdleTimeMillis = conf.getLong("redis.pool.minEvictableIdleTimeMillis")
    val config:GenericObjectPoolConfig = new GenericObjectPoolConfig()
    config.setMaxIdle(maxIdle)
    config.setMaxTotal(maxTotal)
    config.setMinIdle(minIdle)
    config.setTestOnBorrow(testOnBorrow)
    config.setTestOnReturn(testOnReturn)
    config.setTestWhileIdle(testWhileIdle)
    config.setMaxWaitMillis(maxWaitMillis)
    config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis)
//    # 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
//    maxIdle = 2000
//    # 最大连接数
//    maxTotal = 5000
//    # 最小空闲连接数
//    minIdle = 0
//    # 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//    maxWaitMillis = -1
//    # 在获取连接的时候检查有效性, 默认false
//    testOnBorrow = false
//    # 返回时检查有效性 ，默认为false
//    testOnReturn = false
//    # 在空闲时检查有效性, 默认false
//    testWhileIdle = false
//
//    # 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
//    maxWaitMillis=100
//    # 在空闲时检查有效性, 默认false
//    minEvictableIdleTimeMillis=600000

    val nodes = new java.util.HashSet[HostAndPort]()
    hostsAndPortsStr
      .split(",")
      .foreach(
        str => {
          val node = str.split(":")
          if (node.length == 2) {
            nodes.add(new HostAndPort(node(0), Integer.parseInt(node(1))))
          }
        }
      )
    if (nodes.isEmpty) {
      throw new RuntimeException(
        "please configure jedis.connect parameter in application.conf file")
    }
    val cluster = new JedisCluster(nodes,100000,config)
    cluster
  }
  // 把数据写入到redis中
  def flush2Redis(key: String, value: Array[Byte]): String = {
    var ret: String = null
    try {
      ret = cluster.set(key.getBytes, value)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }

  //把数据写入到redis中
  def flush2Redis(map: Map[String, java.io.Serializable]): Unit = {
    var jedisServer: Jedis = null
    try {
      map.foreach(kv => {
        cluster.set(kv._1.getBytes, SerializeUtils.serializeObj(kv._2))
      })
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
  }
  //把数据写入到redis中
  def flush2Redis(key: String, value: java.io.Serializable): String = {
    val bytes: Array[Byte] = SerializeUtils.serializeObj(value)
    var ret: String = null
    try {
      ret = cluster.set(key.getBytes, bytes)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //把数据pipeline写入到redis中
  def flush2Redis(
                   key: String,
                   map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      for ((field, value) <- map) {
        cluster.hset(key, field, value)
      }
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
  }
  //把数据pipeline写入到redis中
  def flush2Redis(map: scala.collection.mutable.HashMap[String, String]): Unit = {
    try {
      for ((field, value) <- map) {
        cluster.set(field, value)
      }
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
  }


  /**
    * 返还到连接池
    */
  def returnResource(redis: Jedis) {
    if (redis != null) redis.close()
  }

  def hget(key: String): java.util.Map[String, String] = {
    var ret: java.util.Map[String, String] = null
    try {
      ret = cluster.hgetAll(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //获取redis数据
  def get(key: String): String = {
    var ret: String = null
    try {
      ret = cluster.get(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //根据key获取redis数据
  def get(keys: Array[Array[Byte]]): Map[Array[Byte], Array[Byte]] = {
    var ret: Map[Array[Byte], Array[Byte]] = null
    try {
      ret = keys
        .map(
          key => {
            (key, cluster.get(key))
          }
        )
        .toMap
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }

  //根据key获取redis数据
  def get(key: Array[Byte]): Array[Byte] = {
    var ret: Array[Byte] = null
    try {
      ret = cluster.get(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //根据key删除redis中的数据
  def del(key: String): Long = {
    var ret: Long = 0
    try {
      ret = cluster.del(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //根据key删除redis中的数据
  def del(key: Array[Byte]): Long = {
    var ret: Long = 0
    try {
      ret = cluster.del(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  //将数据写入到redis中
  def flush2Redis(key: String, value: String): String = {
    var ret: String = null
    try {
      ret = cluster.set(key, value)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
    ret
  }
  def getRedisCluster: JedisCluster = cluster
}


//redis操作工具类
object RedisPoolUtils {

  private[this] val jedisPool: JedisPool = {
    try {
      val conf = ConfigFactory.load("application.conf")
      val masterName = conf.getString("redis.pool.mastername")
      val sentinelsStr = conf.getString("redis.pool.sentinels")
      val str = sentinelsStr.split(",")(1)
      val host = str.split(":")(0)
      val port = str.split(":")(1).toInt

      // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
      // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
      val config = new JedisPoolConfig
      val blockWhenExhausted = conf.getBoolean("redis.pool.blockWhenExhausted")
      val maxIdle = conf.getInt("redis.pool.maxIdle")
      val maxTotal = conf.getInt("redis.pool.maxTotal")
      val minIdle = conf.getInt("redis.pool.minIdle")
      val maxWaitMillis = conf.getInt("redis.pool.maxWaitMillis")
      val testOnBorrow = conf.getBoolean("redis.pool.testOnBorrow")
      val testOnReturn = conf.getBoolean("redis.pool.testOnReturn")
      val testWhileIdle = conf.getBoolean("redis.pool.testWhileIdle")

      // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
      config.setBlockWhenExhausted(blockWhenExhausted)
      // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
      // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
      config.setMaxIdle(maxIdle)
      // 最大连接数
      config.setMaxTotal(maxTotal)
      // 最小空闲连接数
      config.setMinIdle(minIdle)
      // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
      config.setMaxWaitMillis(maxWaitMillis)
      // 在获取连接的时候检查有效性, 默认false
      config.setTestOnBorrow(testOnBorrow)
      // 在回收资源的时候检查有效性
      config.setTestOnReturn(testOnReturn)
      // 在空闲时检查有效性, 默认false
      config.setTestWhileIdle(testWhileIdle)
      // JedisPool
      new JedisPool(config, host, port, 1000000000)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
  }
//把数据写入到redis中
  def flush2Redis(key: String, value: Array[Byte]): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key.getBytes, value)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 释放redis对象
      returnResource(jedisServer)
    }
    ret
  }


  //把数据写入到redis中
  def flush2Redis(map: Map[String, java.io.Serializable]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      val pipeline = jedisServer.pipelined()
      map.foreach(kv => {
        pipeline.set(kv._1.getBytes, SerializeUtils.serializeObj(kv._2))
      })
      pipeline.sync()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
  }
  //把数据写入到redis中
  def flush2Redis(key: String, value: java.io.Serializable): String = {
    val bytes: Array[Byte] = SerializeUtils.serializeObj(value)
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key.getBytes, bytes)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //把数据pipeline写入到redis中
  def flush2Redis(
      key: String,
      map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      val pipelineBase = jedisServer.pipelined()
      for ((field, value) <- map) {
        pipelineBase.hset(key, field, value)
      }
      pipelineBase.sync()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
  }
  //把数据pipeline写入到redis中
  def flush2Redis(map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      val pipelineBase = jedisServer.pipelined()
      for ((field, value) <- map) {
        pipelineBase.set(field, value)
      }
      pipelineBase.sync()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
  }

  /**
    * 返还到连接池
    */
  def returnResource(redis: Jedis) {
    if (redis != null) redis.close()
  }

  def hget(key: String): java.util.Map[String, String] = {
    var ret: java.util.Map[String, String] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.hgetAll(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//获取redis数据
  def get(key: String): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.get(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }

  //根据key获取redis数据
  def get(keys: Array[Array[Byte]]): Map[Array[Byte], Array[Byte]] = {
    var ret: Map[Array[Byte], Array[Byte]] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      val pipeline = jedisServer.pipelined()
      ret = keys
        .map(
          key => {
            (key, pipeline.get(key).get())
          }
        )
        .toMap
      pipeline.sync()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //获取rediis中的key值
  def keys(key: String): java.util.Set[String] = {
    var ret: java.util.Set[String] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.keys(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //根据key获取redis数据
  def get(key: Array[Byte]): Array[Byte] = {
    var ret: Array[Byte] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.get(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//根据key删除redis中的数据
  def del(key: String): Long = {
    var ret: Long = 0
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.del(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //根据key删除redis中的数据
  def del(key: Array[Byte]): Long = {
    var ret: Long = 0
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.del(key)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//将数据写入到redis中
  def flush2Redis(key: String, value: String): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key, value)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
}
//redis工具类
object RedisSentinelUtils {
  private[this] val jedisPool: JedisSentinelPool = {
    try {
      val conf = ConfigFactory.load("application.conf")
      val masterName = conf.getString("redis.pool.mastername")
      val sentinelsStr = conf.getString("redis.pool.sentinels")
      val sentinels: java.util.Set[String] = new java.util.HashSet[String]()
      sentinelsStr
        .split(",")
        .foreach(
          sentinel => {
            if (sentinel != "") {
              sentinels.add(sentinel)
            }
          }
        )

      // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
      // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
      val config = new JedisPoolConfig

      val blockWhenExhausted = conf.getBoolean("redis.pool.blockWhenExhausted")
      val maxIdle = conf.getInt("redis.pool.maxIdle")
      val maxTotal = conf.getInt("redis.pool.maxTotal")
      val minIdle = conf.getInt("redis.pool.minIdle")
      val maxWaitMillis = conf.getInt("redis.pool.maxWaitMillis")
      val testOnBorrow = conf.getBoolean("redis.pool.testOnBorrow")
      val testOnReturn = conf.getBoolean("redis.pool.testOnReturn")
      val testWhileIdle = conf.getBoolean("redis.pool.testWhileIdle")

      // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
      config.setBlockWhenExhausted(blockWhenExhausted)
      // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
      // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
      config.setMaxIdle(maxIdle)
      // 最大连接数
      config.setMaxTotal(maxTotal)
      // 最小空闲连接数
      config.setMinIdle(minIdle)
      // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
      config.setMaxWaitMillis(maxWaitMillis)
      // 在获取连接的时候检查有效性, 默认false
      config.setTestOnBorrow(testOnBorrow)
      // 在回收资源的时候检查有效性
      config.setTestOnReturn(testOnReturn)
      // 在空闲时检查有效性, 默认false
      config.setTestWhileIdle(testWhileIdle)
      // JedisSentinelPool
      new JedisSentinelPool(masterName, sentinels, config, 1000000000)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }
  }



  def get(keys: Array[Array[Byte]]): Map[Array[Byte], Array[Byte]] = {
    var ret: Map[Array[Byte], Array[Byte]] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = keys.map(key=>{
        (key,jedisServer.get(key))
      }).toMap
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }



  def StkPriceflush2Redis(map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      for ((field, value) <- map) {
        val str = field.split("@")
        jedisServer.hset(str(0), str(1),value)
        jedisServer.close()
      }
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
  }

  def flush2Redis(map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      for ((field, value) <- map) {
        jedisServer.set(field,value)
      }
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
  }



  //将数据写入到redis中
  def flush2Redis(key: String, value: Array[Byte]): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key.getBytes, value)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //将数据写入到redis中
  def flush2Redis(key: String, value: java.io.Serializable): String = {
    val bytes: Array[Byte] = SerializeUtils.serializeObj(value)
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key.getBytes, bytes)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }


  def flush2Redis(map: Map[String, java.io.Serializable]): Unit = {
    var jedisServer: Jedis = null
    try {
      map.foreach(kv => {
        jedisServer = jedisPool.getResource
        jedisServer.set(kv._1.getBytes, SerializeUtils.serializeObj(kv._2))
        jedisServer.close()
      })
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    }finally {
      //返还到连接池
      returnResource(jedisServer)
    }
  }

  def flush2Redis(
                   key: String,
                   map: scala.collection.mutable.HashMap[String, String]): Unit = {
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      val pipelineBase = jedisServer.pipelined()
      for ((field, value) <- map) {
        pipelineBase.hset(key, field, value)
      }
      pipelineBase.sync()
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
  }

  /**
    * 返还到连接池
    */
  def returnResource(redis: Jedis) {
    if (redis != null) redis.close()
  }

  def get(key: String): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.get(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//获取redis中的key值
  def keys(key: String): java.util.Set[String] = {
    var ret: java.util.Set[String] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.keys(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//根据key获取对应得value
  def get(key: Array[Byte]): Array[Byte] = {
    var ret: Array[Byte] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.get(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
//根据key删除对应的value
  def del(key: String): Long = {
    var ret: Long = 0
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.del(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //根据key删除对应的value
  def del(key: Array[Byte]): Long = {
    var ret: Long = 0
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.del(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }

  // 把数据写入到redis中
  def flush2Redis(key: String, value: String): String = {
    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key, value)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
  //
  def set(key:String,value:String): String ={

    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.set(key, value)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }

  def hget(key: String): java.util.Map[String, String] = {
    var ret: java.util.Map[String, String] = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      ret = jedisServer.hgetAll(key)
      jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      // 返还到连接池
      returnResource(jedisServer)
    }
    ret
  }


/*  def set(key:String,value:mutable.HashMap[String, String]): String ={

    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
     // ret = jedisServer.set(key, value)
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }*/

  def hset(key:String,value:mutable.HashMap[String, String]): String ={

    var ret: String = null
    var jedisServer: Jedis = null
    try {
      jedisServer = jedisPool.getResource
      value.keys.foreach(key=>{
        jedisServer.hset(key,key,value.get(key).toString)
      })
  jedisServer.close()
    } catch {
      case e: Exception => {
        throw new RedisConnectionException(e)
      }
    } finally {
      //返还到连接池
      returnResource(jedisServer)
    }
    ret
  }
}
