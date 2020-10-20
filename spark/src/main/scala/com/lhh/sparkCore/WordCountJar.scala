package com.lhh.sparkCore

import org.apache.spark.{SparkConf, SparkContext}

object WordCountJar {

  def main(args: Array[String]): Unit = {
    //构建SparkConf对象
    val conf = new SparkConf().setAppName("WordCountJar")
    //构建SparkContext对象
    val context = new SparkContext(conf)

    context.setLogLevel("warn")

    //读取文件
    val datas = context.textFile(args(0))

    val words = datas.flatMap(_.split(" "))

    //每个单词记为1
    val wordAndOne = words.map((_,1))

    //相同的key进行累加
    val result = wordAndOne.reduceByKey(_+_)

    //保存结果
    result.saveAsTextFile(args(1))

    //关闭连接
    context.stop()
  }

}
