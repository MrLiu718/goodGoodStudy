package com.lhh.sparkCore

import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/16 21:16
  * @version 0.0.1
  */
object WordCount {

  def main(args: Array[String]): Unit = {
    /**
      * 集群运行模式下的命令
      * 文件都是保存在HDFS 上的
      *
      * spark-submit \
      * --master spark://node01:7077,node02:7077 \
      * --class com.kaikeba.WordCountOnSpark \
      * --executor-memory 1g  \
      * --total-executor-cores 4 \
      * original-spark_class04-1.0-SNAPSHOT.jar \
      * /words.txt  /out
      */

    //构建程序入口
    //本地运行模式
    val conf = new SparkConf().setMaster("local[2]").setAppName("wordCount")
    val sc = new SparkContext(conf)

    //集群运行模式
    //val conf = new SparkConf().setAppName("wordCountOnLine")
    //val sc = new SparkContext(conf)

    //设置日志输出级别
    sc.setLogLevel("warn")

    //读取本地数据
    val RDD1 = sc.textFile("F:\\开课吧 大数据\\spark\\spark_day01\\数据\\WordCount.txt")

    //读取集群运行模式的数据
    //val RDD1 = sc.textFile(args(0))

    //得到结果数据
    val resultRDD = RDD1.flatMap(_.split(" ")).map((_,1)).reduceByKey(_+_)

    //排序输出 true :升序  false ： 降序
    val resultSortRDD = resultRDD.sortBy(_._2,false)

    //将数据保存本地：
    //resultSortRDD.saveAsTextFile("F:\\开课吧 大数据\\spark\\spark_day01\\数据\\out.txt")

    //执行action算子
    val tuples = resultSortRDD.collect()

    //打印数据
    tuples.foreach(println)

    //关闭资源
    sc.stop()
  }
}
