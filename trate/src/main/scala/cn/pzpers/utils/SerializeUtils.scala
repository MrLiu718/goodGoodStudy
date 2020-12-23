package cn.pzpers.utils

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import scala.collection.JavaConversions._

/**
  * author: Johnny
  * contact: zhendong.bai@hypers.com
  * file: SerializeUtils
  */
//序列化和反序列化以及更新和删除
object SerializeUtils {
  //序列化
  def serializeObj(obj: java.io.Serializable): Array[Byte] = {
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos: ObjectOutputStream = new ObjectOutputStream(baos)
    oos.writeObject(obj)
    val bytes: Array[Byte] = baos.toByteArray
    baos.close()
    oos.close()
    bytes
  }
//反序列化
  def deserializeObj[T](obj: Array[Byte]): T = {
    val bais: ByteArrayInputStream = new ByteArrayInputStream(obj)
    val ois: ObjectInputStream = new ObjectInputStream(bais)
    val res = ois.readObject()
    bais.close()
    ois.close()
    res.asInstanceOf[T]
  }
//更新数据操作
  def updateField(mapKey: String, keys: Array[String], values: Array[String], getter: () => java.util.HashMap[String, java.util.HashMap[String, String]], setter: (java.util.HashMap[String, java.util.HashMap[String, String]]) => Unit): Unit = {
    if (mapKey == null) {
      throw new IllegalArgumentException("mapKey cannot be null !")
    }

    if (getter == null) {
      throw new IllegalArgumentException("getter cannot be null !")
    }

    if (keys == null || values == null) {
      throw new IllegalArgumentException("keys or values cannot be null !")
    }

    if (keys.length != values.length) {
      throw new IllegalArgumentException(s"keys or values' length must be equal ! ${keys.length} --> ${values.length}")
    }

    if (setter == null) {
      throw new IllegalArgumentException("setter cannot be null !")
    }

    // get previous data
    var stks: java.util.HashMap[String, java.util.HashMap[String, String]] = getter()
    if (stks == null) {
      stks = new java.util.HashMap[String, java.util.HashMap[String, String]]()
    }

    // set new data
    val mapValue: java.util.HashMap[String, String] = new java.util.HashMap[String, String]()
    for (idx <- keys.indices) {
      mapValue.put(keys(idx), values(idx))
    }
    stks.put(mapKey, mapValue)
    // update field data
    setter(stks)
  }
//删除数据操作
  def delField(mapKey: String, getter: () => java.util.HashMap[String, java.util.HashMap[String, String]], setter: (java.util.HashMap[String, java.util.HashMap[String, String]]) => Unit): Unit = {
    if (mapKey == null) {
      throw new IllegalArgumentException("mapKey cannot be null !")
    }

    if (getter == null) {
      throw new IllegalArgumentException("getter cannot be null !")
    }
    if (setter == null) {
      throw new IllegalArgumentException("setter cannot be null !")
    }
    // get before data
    var stks: java.util.HashMap[String, java.util.HashMap[String, String]] = getter()

    if (stks != null) {
      //update field data
      val newStks: java.util.HashMap[String, java.util.HashMap[String, String]]
      = new java.util.HashMap[String, java.util.HashMap[String, String]]()
      for (key: String <- stks.keySet()) {
        if (!mapKey.equals(key)) {
          val value = stks.get(key)
          newStks.put(key, value)
        }
      }
      stks = newStks
      if (stks.size() == 0) {
        stks = null
      }
    }
    // update field data
    setter(stks)
    // stks.remove(mapKey)
  }
//主键组装操作
  def strSepConcat(strs:String*): String = {

    strs.length match {
      case 0 => ""
      case 1 => strs(0)
      case _ => {
        var result = strs(0)
        for (index <- 1 until strs.length) {
          result += Constants.KEYSEPARATOR + strs(index)
        }
        result
      }
    }
  }

  def main(args: Array[String]): Unit = {

    // println(strSepConcat())
    //    val a = new UserStkAsset()
    //    updateField("a",Array("1","2","3"),Array("a","b","c"),a.getCbs_ofassets,a.setCbs_ofassets)
    //    updateField("b",Array("1","2","3"),Array("a","b","c"),a.getCbs_ofassets,a.setCbs_ofassets)
    //    println(a)
    //    delField("a",a.getCbs_ofassets,a.setCbs_ofassets)
    //    println(a)

  }
}
