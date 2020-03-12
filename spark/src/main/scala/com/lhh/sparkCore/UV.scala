package com.lhh.sparkCore

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/16 22:03
  * @version 0.0.1
  * 获取UV
  */
object UV {

  def main(args: Array[String]): Unit = {
    //获取程序入口
    val conf = new SparkConf().setMaster("local[2]").setAppName("UV")
    val sc = new SparkContext(conf)

    //设置日志格式
    sc.setLogLevel("warn")

    //读取数据
    val Rdd1 = sc.textFile("F:\\开课吧 大数据\\spark\\案例\\课程资料\\案例数据\\access.log")

    //计算结果数据
    val uv = Rdd1.map(_.split(" ")(0)).distinct().count()

    print(s"UV:$uv")

    //关闭资源
    sc.stop()


  }
}
