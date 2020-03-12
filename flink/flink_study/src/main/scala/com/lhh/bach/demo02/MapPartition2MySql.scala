package com.lhh.bach.demo02

import java.sql.{Connection, PreparedStatement}

import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/5 15:57
  * @version 0.0.1
  */
object MapPartition2MySql {

  def main(args: Array[String]): Unit = {
    val environment: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    //隐式转换
    import org.apache.flink.api.scala._

    val datas: DataSet[String] = environment.fromElements("1 zhangsan","2 lisi","3 wangwu")

    datas.mapPartition(data =>{
      Class.forName("com.mysql.jdbc.Driver").newInstance()
      val connection: Connection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/flink_db", "root", "123456")
      data.map(x => {
        val statement: PreparedStatement = connection.prepareStatement("insert into user (id,name) values (?,?)")
        statement.setInt(1,x.split(" ")(0).toInt)
        statement.setString(2,x.split(" ")(1))
        statement.execute()
      })
      //val conn: Unit = connection.close()
    }).print()
    environment.execute()
  }
}
