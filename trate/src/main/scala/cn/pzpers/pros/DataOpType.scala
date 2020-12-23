package cn.pzpers.pros

/**
  * 数据操作的类型，增加或更新或删除操作
  */

object DataOpType extends Enumeration {
  type ops = Value
  //purpose : expose value to outer class or object
  val INSERT, UPDATE, DELETE = Value // define specific enum instance
}