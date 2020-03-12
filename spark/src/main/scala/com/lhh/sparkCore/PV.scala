package com.lhh.sparkCore

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/16 21:53
  * @version 0.0.1
  * 对点击率进行统计
  */
object PV {

  def main(args: Array[String]): Unit = {
    //创建程序入口
    val conf = new SparkConf().setMaster("local[2]").setAppName("pv")

    val sc = new SparkContext(conf)

    //设置日志格式
    sc.setLogLevel("warn")

    //获取数据
    val RDD1 = sc.textFile("F:\\开课吧 大数据\\spark\\案例\\课程资料\\案例数据\\access.log")

    //输出数据
    val pv = RDD1.count()
    print(s"pv:$pv")

    //关闭程序
    sc.stop()
  }
}
