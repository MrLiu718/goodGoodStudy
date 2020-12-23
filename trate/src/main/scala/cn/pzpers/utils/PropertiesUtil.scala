package cn.pzpers.utils

import cn.jpzpers.utils.PropertiesReader

import scala.collection.immutable

//配置操作
object PropertiesUtil {
//把配置写成map形式
  def prp2Map(prp: String): Map[String, String] = {
    val proReader = new PropertiesReader(prp)
    var kvPairs = new immutable.HashMap[String, String]()
    val iterator = proReader.getKeys.iterator
    while (iterator.hasNext) {
      val key = iterator.next().asInstanceOf[String]
      val value = proReader.getValue(key)
      kvPairs.+=(key -> value)
    }
    kvPairs
  }
}