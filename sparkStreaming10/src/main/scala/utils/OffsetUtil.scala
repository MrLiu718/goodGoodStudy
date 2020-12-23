package utils

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import scala.collection.JavaConversions._

object OffsetUtil {


  /**
    * 从hbase中获取offset
    * @return
    */
  // 得到历史 OffSet
  def getOffset(topic: Array[String]):(Map[TopicPartition, Long], Int) ={


    val topics = topic(0).toString
    val fromOffSets = scala.collection.mutable.Map[TopicPartition, Long]()

    HbaseUtil.ensureHbaseTBExsit(Constants.offsetTableName,Constants.offSetColumnFamilyName)


    val table = new HTable(HbaseUtil.conf, Constants.offsetTableName)
    val rs = table.getScanner(new Scan())

    // 获取数据  每条数据的列名为partition，值为offset
    for (r:Result <- rs.next(10)) {
      for (kv:KeyValue <- r.raw()) {
        var partitionString = Bytes.toString(kv.getQualifier)
        val partition = partitionString.substring(partitionString.length-1,partitionString.length).toInt

        val offSet = Bytes.toLong(kv.getValue)
        println("获取到的partition:" + partition + ",   opffset:" + offSet)
        fromOffSets.put(new TopicPartition(topics, partition), offSet)
      }
    }

    // 返回值
    if (fromOffSets.isEmpty){
      (fromOffSets.toMap, 0)
    } else {
      (fromOffSets.toMap, 1)
    }

  }

  // 保存新的 OffSet
  def storeOffSet(ranges: Array[OffsetRange], topic:Array[String]) = {

    val table = new HTable(HbaseUtil.conf, Constants.offsetTableName)
    table.setAutoFlush(false, false)

    HbaseUtil.ensureHbaseTBExsit(Constants.offsetTableName,Constants.offSetColumnFamilyName)

    var putList:List[Put]= List()
    for(o <- ranges){
      val rddTopic = o.topic
      val rddPartition = o.partition
      val rddOffSet = o.untilOffset
      println("topic:" + rddTopic + ",    partition:" + rddPartition + ",    offset:" + rddOffSet)
      // ColumnFamily
      val put = new Put(Bytes.toBytes("kafka_offSet_" + o.topic))
      put.add(Bytes.toBytes(Constants.offSetColumnFamilyName), Bytes.toBytes(o.topic+o.partition), Bytes.toBytes(o.untilOffset))

      putList = put+:putList

    }
    table.put(putList)
    table.flushCommits()
    println("保存新 offset 成功！")
  }

}
