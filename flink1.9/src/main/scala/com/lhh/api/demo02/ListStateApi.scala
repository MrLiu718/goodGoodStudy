package com.lhh.api.demo02

import java.util.Collections
import java.{lang, util}

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/19 22:52
  * @version 0.0.1
  */
object ListState {

  def main(args: Array[String]): Unit = {
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val listBuffer = ListBuffer[(String,String,String)]()
    listBuffer.append(("192.168.1.110","lhh","1584629170"))
    listBuffer.append(("192.168.1.110","lhh","1584629180"))
    listBuffer.append(("192.168.1.110","lhh","1584629195"))
    listBuffer.append(("192.168.1.110","lhh","1584629196"))
    listBuffer.append(("192.168.1.110","lhh","1584629197"))
    listBuffer.append(("192.168.1.111","lhh1","1584629198"))
    listBuffer.append(("192.168.1.111","lhh1","1584629200"))
    listBuffer.append(("192.168.1.111","lhh1","1584629210"))
    listBuffer.append(("192.168.1.111","lhh1","1584629240"))
    val inputDs: DataStream[(String, String, String)] = environment.fromCollection(listBuffer)
    inputDs.keyBy(_._2)
      .flatMap(new CountWindowAverageWithList)
      .print()
    environment.execute()
  }
}
class CountWindowAverageWithList extends RichFlatMapFunction[(String,String,String),(String,String)]{

  //声明一个历史数据获取
  private var listStateData: ListState[(String,String)] = _

  override def open(parameters: Configuration): Unit = {
     val listState = new ListStateDescriptor[(String,String)]("listState",classOf[(String,String)])
    listStateData = getRuntimeContext.getListState(listState)
  }
  override def flatMap(in: (String, String, String), collector: Collector[(String, String)]): Unit = {

    val tuples: lang.Iterable[(String, String)] = listStateData.get()
    if(tuples == null){
      listStateData .addAll(Collections.emptyList())
    }

    listStateData.add((in._2,in._3))
    import scala.collection.JavaConverters._
    val allList: Iterator[( String, String)] = listStateData.get().iterator().asScala
    val allLists: List[( String, String)] = allList.toList

    if (allLists.size >= 3){
      val res: Int = getTimediff(allLists)
      if(res == 1){
        collector.collect(allLists.last)
      }
      listStateData.update(allLists.slice(1,2).asJava)
    }
  }

  def getTimediff(data: List[( String, String)]): Int ={

    val head: (String, String) = data.head
    val last: (String, String) = data.last
    val time: Long = last._2.toLong - head._2.toLong
    if(time < 30L){
      1
    }else{
      0
    }

  }
}