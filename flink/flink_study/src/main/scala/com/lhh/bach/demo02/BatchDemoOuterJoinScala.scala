package com.lhh.bach.demo02

import org.apache.flink.api.scala.ExecutionEnvironment

import scala.collection.mutable.ListBuffer

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/5 18:40
  * @version 0.0.1
  */
object BatchDemoOuterJoinScala {
  def main(args: Array[String]): Unit = {

    val env = ExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._

    val data1 = ListBuffer[Tuple2[Int,String]]()
    data1.append((1,"zs"))
    data1.append((2,"ls"))
    data1.append((3,"ww"))


    val data2 = ListBuffer[Tuple2[Int,String]]()
    data2.append((1,"beijing"))
    data2.append((2,"shanghai"))
    data2.append((4,"guangzhou"))

    val text1 = env.fromCollection(data1)
    val text2 = env.fromCollection(data2)

    text1.leftOuterJoin(text2).where(0).equalTo(0).apply((x,y) =>{
      if(null == y){
        (x._1,x._2,"null")
      }else{
        (x._1,x._2,y._2)
      }
    }).print()

    text1.rightOuterJoin(text2).where(0).equalTo(0).apply((x,y) =>{
      if(null == x){
        (y._1,"null",y._2)
      }else{
        (y._1,x._2,y._2)
      }
    }).print()

    text1.fullOuterJoin(text2).where(0).equalTo(0).apply((x,y) => {
      if(null == x){
        (y._1,"null",y._2)
      }else if (null == y){
        (x._1,x._2,y._2)
      }else{
        (x._1,x._2,y._2)
      }
    }).print()


    env.execute()
  }

}
