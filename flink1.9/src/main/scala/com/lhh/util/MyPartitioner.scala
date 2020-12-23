package com.lhh.util

import org.apache.flink.api.common.functions.Partitioner

class MyPartitioner extends Partitioner[String]{
  override def partition(k: String, i: Int): Int = {
    println("分区数为： " + i)

    if(k.contains("hello")){
      0
    }else{
      1
    }
  }
}
