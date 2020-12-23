package utils

import javax.ws.rs.core.Response.Status.Family
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, HBaseConfiguration, HColumnDescriptor, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{Admin, Connection, ConnectionFactory, Get, Put, Result, Table}
import org.apache.hadoop.hbase.util.Bytes

import scala.collection.mutable

object HbaseUtil {
  var conf: Configuration = _
  //线程池
  lazy val connection: Connection = ConnectionFactory.createConnection(conf)
  lazy val admin: Admin = connection.getAdmin

  /**
    * hbase conf
    *
    * @param quorum hbase的zk地址
    * @param port   zk端口2181
    * @return
    */
  def setConf(quorum: String, port: String): Unit = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", quorum)
    conf.set("hbase.zookeeper.property.clientPort", port)
    this.conf = conf
  }

  /**
    * 如果不存在就创建表
    * @param tableName 命名空间：表名
    */
  def ensureHbaseTBExsit(tableName: String,familyName: String): Unit = {
    val tbName = TableName.valueOf(tableName)
    if (!admin.tableExists(tbName)) {
      val htableDescriptor = new HTableDescriptor(tbName)
      val hcolumnDescriptor = new HColumnDescriptor(familyName)
      htableDescriptor.addFamily(hcolumnDescriptor)
      admin.createTable(htableDescriptor)
    }
  }

  /**
    * 获取hbase单元格内容
    * @param tableName 命名空间：表名
    * @param rowKey rowkey
    * @return 返回单元格组成的List
    */
  def getCell(tableName: String, rowKey: String): mutable.Buffer[Cell] = {
    val get = new Get(Bytes.toBytes(rowKey))
    val table = connection.getTable(TableName.valueOf(tableName))
    val result: Result = table.get(get)
    if(result.isEmpty){
      null
    }else{
      import scala.collection.JavaConverters._
      result.listCells().asScala
    }

  }

  /**
    * 单条插入
    * @param tableName 命名空间：表名
    * @param rowKey rowkey
    * @param family 列族
    * @param qualifier column列
    * @param value 列值
    */
  def singlePut(tableName: String, rowKey: String, family: String, qualifier: String, value: String): Unit = {
    //单个插入
    val put: Put = new Put(Bytes.toBytes(rowKey)) //参数是行健
    put.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(value))

    //获得表对象
    val table: Table = connection.getTable(TableName.valueOf(tableName))
    table.put(put)
    table.close()
  }

  def close(): Unit = {
    admin.close()
    connection.close()
  }



}
