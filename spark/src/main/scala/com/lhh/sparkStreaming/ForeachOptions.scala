//package com.lhh.sparkStreaming
//
//import java.sql.DriverManager
//
//import com.lhh.utils.ConnectionPool
//import com.mysql.jdbc.Driver
//import org.apache.spark.storage.StorageLevel
//import org.apache.spark.streaming._
//import org.apache.spark.{SparkConf, SparkContext}
//
///**
//  * @description
//  * @author: liuhh
//  * @date: Created in 2020/2/19 11:06
//  * @version 0.0.1
//  */
//object ForeachOptions {
//  def main(args: Array[String]): Unit = {
//    val sparkConf = new SparkConf().setAppName("NetworkWordCountForeachRDD").setMaster("local[2]")
//    val sc = new SparkContext(sparkConf)
//
//    // Create the context with a 1 second batch size
//    val ssc = new StreamingContext(sc, Seconds(5))
//
//    //创建一个接收器(ReceiverInputDStream)，这个接收器接收一台机器上的某个端口通过socket发送过来的数据并处理
//    val lines = ssc.socketTextStream("master", 9998, StorageLevel.MEMORY_AND_DISK_SER)
//
//    //处理的逻辑，就是简单的进行word count
//    val words = lines.flatMap(_.split(" "))
//    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
//
//    /**
//      * 使用foreachpartition 保存数据到mysql
//      */
//    wordCounts.foreachRDD{(Rdd,time) =>
//      Rdd.foreachPartition(partitionRecords =>{
//        Class.forName("com.mysql.jdbc.Driver")
//        //获取连接
//        val conn = DriverManager.getConnection("jdbc:mysql://hadoop120:3306/spark", "root", "123456")
//        //预编译后的sql
//        val sql = conn.prepareStatement("insert into wordcount(ts, word, count) values (?, ?, ?)")
//
//        partitionRecords.foreach { case (word, count) =>
//          sql.setLong(1, time.milliseconds)
//          sql.setString(2, word)
//          sql.setInt(3, count)
//          sql.execute()
//        }
//        sql.close()
//        conn.close()
//      })
//    }
//
//    /**
//      * 使用foreachpartition，连接池 保存数据到mysql
//      */
//
//    wordCounts.foreachRDD{(Rdd,time)=>
//      Rdd.foreachPartition(partitionRdd =>{
//        //使用连接池获取连接
//        val conn = ConnectionPool.getConnection
//        //预编译后的sql
//        val sql = conn.prepareStatement("insert into wordcount(ts, word, count) values (?, ?, ?)")
//
//        partitionRdd.foreach{ case (word, count) =>
//          sql.setLong(1, time.milliseconds)
//          sql.setString(2, word)
//          sql.setInt(3, count)
//          sql.execute()}
//        sql.close()
//        ConnectionPool.returnConnection(conn)
//      })
//    }
//
//    /**
//      * 使用foreachpartition，连接池 批量保存 保存数据到mysql
//      */
//
//    wordCounts.foreachRDD{(Rdd,time)=>
//      Rdd.foreachPartition(partitionRdd =>{
//        //使用连接池获取连接
//        val conn = ConnectionPool.getConnection
//        //预编译后的sql
//        val sql = conn.prepareStatement("insert into wordcount(ts, word, count) values (?, ?, ?)")
//
//        partitionRdd.foreach{ case (word, count) =>
//          sql.setLong(1, time.milliseconds)
//          sql.setString(2, word)
//          sql.setInt(3, count)
//          sql.addBatch()
//        }
//
//        sql.executeBatch()
//        sql.close()
//        ConnectionPool.returnConnection(conn)
//      })
//    }
//
//    /**
//      * 使用foreachpartition，连接池 批量保存 手动提交事务 保存数据到mysql
//      */
//
//    wordCounts.foreachRDD{(Rdd,time)=>
//      Rdd.foreachPartition(partitionRdd =>{
//        //使用连接池获取连接
//        val conn = ConnectionPool.getConnection
//        //关闭自动提交事务
//        conn.setAutoCommit(false)
//        //预编译后的sql
//        val sql = conn.prepareStatement("insert into wordcount(ts, word, count) values (?, ?, ?)")
//
//        partitionRdd.foreach{ case (word, count) =>
//          sql.setLong(1, time.milliseconds)
//          sql.setString(2, word)
//          sql.setInt(3, count)
//          sql.addBatch()
//        }
//
//        sql.executeBatch()
//        sql.close()
//        conn.commit()
//        //手动提交事务
//        conn.setAutoCommit(true)
//        ConnectionPool.returnConnection(conn)
//      })
//    }
//
//    /**
//      * 使用foreachpartition，连接池 批量保存 手动提交事务 增加限制条数 保存数据到mysql
//      */
//
//    wordCounts.foreachRDD{(Rdd,time)=>
//      Rdd.foreachPartition(partitionRdd =>{
//        //使用连接池获取连接
//        val conn = ConnectionPool.getConnection
//        //关闭自动提交事务
//        conn.setAutoCommit(false)
//        //预编译后的sql
//        val sql = conn.prepareStatement("insert into wordcount(ts, word, count) values (?, ?, ?)")
//
//        partitionRdd.zipWithIndex.foreach{ case ((word, count),index) =>
//          sql.setLong(1, time.milliseconds)
//          sql.setString(2, word)
//          sql.setInt(3, count)
//          sql.addBatch()
//          if (index != 0 && index % 500 == 0) {
//            sql.executeBatch()
//            conn.commit()
//          }
//        }
//
//        sql.executeBatch()
//        sql.close()
//        conn.commit()
//        //手动提交事务
//        conn.setAutoCommit(true)
//        ConnectionPool.returnConnection(conn)
//      })
//    }
//
//    //等待Streaming程序终止
//    ssc.awaitTermination()
//  }
//
//}
