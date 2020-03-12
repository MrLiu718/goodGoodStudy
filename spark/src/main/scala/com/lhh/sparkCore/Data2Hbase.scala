package com.lhh.sparkCore

import java.util

import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/17 15:41
  * @version 0.0.1
  */
object Data2Hbase {
  def main(args: Array[String]): Unit = {
    //设置程序入口
    val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("Data2Hbase")

    val sc = new SparkContext(conf)

    //读取数据
    val Rdd1 = sc.textFile("F:\\开课吧 大数据\\spark\\案例\\课程资料\\案例数据\\users.dat")

    val Rdd2 = Rdd1.map(_.split("::"))

    Rdd2.foreachPartition(arry =>{
      //创建Hbase连接
      var connection: Connection=null
      try{
        val configuration = HBaseConfiguration.create()
        configuration.set("hbase.zookeeper.quorum","hadoop100:2181,hadoop110:2181,hadoop120:2181")
        connection = ConnectionFactory.createConnection(configuration)
        //获取表对象
        val table = connection.getTable(TableName.valueOf("person"))

        arry.foreach(x =>{
          val puts = new util.ArrayList[Put]()
          val put = new Put(x(0).getBytes)
          val put1 = put.addColumn("info".getBytes(),"gerder".getBytes(),x(1).getBytes())
          val put2: Put = put.addColumn("info".getBytes, "age".getBytes, x(2).getBytes)
          val put3: Put = put.addColumn("data".getBytes, "position".getBytes, x(3).getBytes)
          val put4: Put = put.addColumn("data".getBytes, "code".getBytes, x(4).getBytes)
          puts.add(put1)
          puts.add(put2)
          puts.add(put3)
          puts.add(put4)
          //加入table
          table.put(puts)

        })
      }catch {
        case e:Exception => println(e.getMessage)
      }finally {
        if(null != connection){
          connection.close()
        }
      }


    })
  }



}
