package com.lhh.sparkCore

import java.sql.{Connection, DriverManager}

import org.apache.log4j.Logger
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/17 10:39
  * @version 0.0.1
  */
object DataToMysql {
  def main(args: Array[String]): Unit = {
    Logger
    //每个线程并行计算 插入的顺序不同
    val sparkConf = new SparkConf().setAppName("ForeachInMysql").setMaster("local[2]")

    val sc = new SparkContext(sparkConf)

    val scData = sc.textFile("F:\\开课吧 大数据\\spark\\案例\\课程资料\\案例数据\\person.txt")


    val arrayRdd = scData.map(_.split(",")).map(x => (x(0).toInt,x(1),x(2).toInt))

    arrayRdd.foreachPartition(arry => {

      var connection : Connection = null
      try{
        connection = DriverManager.getConnection("jdbc:mysql://hadoop120:3306/spark", "root", "123456")
        val sql = "insert into person (id,pname,age) value(?,?,?)"
        //预编译sql
        val statement = connection.prepareStatement(sql)

        arry.foreach(x =>{
          statement.setInt(1,x._1)
          statement.setString(2,x._2)
          statement.setInt(3,x._3)
          //设置批量提交数据
          statement.addBatch()
        })

        //执行sql
        statement.executeBatch()
      }catch {
        case e:Exception =>println(e.getMessage)
      }finally {
        //释放放资源
        if(null != connection){
          connection.close()
        }
      }
    })
    sc.stop()
  }

}
