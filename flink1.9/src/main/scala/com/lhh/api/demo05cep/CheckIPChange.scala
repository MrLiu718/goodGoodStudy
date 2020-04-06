package com.lhh.api.demo05cep

import java.util
import java.util.Collections

import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/24 22:42
  * @version 0.0.1
  */
object CheckIPChange {
  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val inputStream: DataStream[String] = environment.socketTextStream("hadoop100",9999)
    inputStream.map(x=>{
      val strings: Array[String] = x.split(",")
      (strings(1),UserLogin(strings(0),strings(1),strings(2),strings(3)))})
      .keyBy(_._1)
      .process(new LoginCheckProcessFunction)
      .print()
      .setParallelism(1)

    environment.execute()

  }

}
class LoginCheckProcessFunction extends KeyedProcessFunction[String,(String,UserLogin),(String,UserLogin)]{
  private var listState: ListState[UserLogin] = _

  override def open(parameters: Configuration): Unit = {
    val listStateDescriptor = new ListStateDescriptor[UserLogin]("changeIp",classOf[UserLogin])
    listState = getRuntimeContext.getListState[UserLogin](listStateDescriptor)

  }
  override def processElement(i: (String, UserLogin), context: KeyedProcessFunction[String,
    (String, UserLogin), (String, UserLogin)]#Context, collector: Collector[(String, UserLogin)]): Unit = {
    val arrayList = new util.ArrayList[UserLogin]()
    listState.add(i._2)
    import scala.collection.JavaConverters._
    val list: List[UserLogin] = listState.get().asScala.toList
    list.sortBy(x=>x.time)
    if(list.size == 2){
      val first: UserLogin = list(0)
      val second: UserLogin = list(1)
      if(!first.ip.equals(second.ip)){
        println("小伙子你的IP变了，赶紧回去重新登录一下")
      }
      //移除第一个
      arrayList.removeAll(Collections.EMPTY_LIST)
      arrayList.add(second)
      listState.update(arrayList)
    }
    collector.collect(i)
  }
}
case class UserLogin (ip:String,username:String,operateUrl:String,time:String)
