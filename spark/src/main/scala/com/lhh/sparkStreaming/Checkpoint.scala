package com.lhh.sparkStreaming

import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/2/19 16:42
  * @version 0.0.1
  */
object Checkpoint {
  def main(args: Array[String]): Unit = {
    //checkpoint路径
    val checkpointPath = "hdfs://hadoop100:8020/streamingcheckpoint2"

    def functionToCreateContext(): StreamingContext = {
      val conf = new SparkConf().setMaster("local[2]").setAppName("NetWordCount")
      val sc = new SparkContext(conf)
      val ssc = new StreamingContext(sc,Seconds(2))
      ssc.checkpoint(checkpointPath)
      val dstream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop1",9999)

      val wordCountDStream = dstream.flatMap(_.split(" ")).map((_,1))
        .updateStateByKey((value: Seq[Int],state: Option[Int])=>{
          val currentCount = value.sum
          val lastcount = state.getOrElse(0)
          Some(currentCount + lastcount)
        })
      wordCountDStream.print()


      ssc
    }

    val ssc = StreamingContext.getOrCreate(checkpointPath,functionToCreateContext _)


    ssc.start()
    ssc.awaitTermination()
    ssc.stop()

  }

}
