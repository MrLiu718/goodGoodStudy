package com.lhh.sparkCore

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/16 22:12
  * @version 0.0.1
  */
object TonpN {
  def main(args: Array[String]): Unit = {
    //程序入口
    val conf = new SparkConf().setMaster("local[2]").setAppName("topN")
    val sc = new SparkContext(conf)

    //设置日志级别
    sc.setLogLevel("warn")

    //读取数据
    val Rdd1 = sc.textFile("F:\\开课吧 大数据\\spark\\案例\\课程资料\\案例数据\\access.log")

    //过滤数据

    val Rdd2 = Rdd1.map(_.split(" ")).filter(arry => arry.length>10).filter(x => x(10) != "\"-\"")

    //取地址
    val tuples5 = Rdd2.map(x => (x(10),1)).reduceByKey(_+_).sortBy(_._2,false).take(5)

    tuples5.foreach(println)

    //关闭资源
    sc.stop()

  }

}
