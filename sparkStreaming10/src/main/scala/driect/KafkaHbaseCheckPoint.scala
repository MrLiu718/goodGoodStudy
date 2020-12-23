package driect

import java.util

import bean.GoodsCountBean
import org.apache.hadoop.hbase.CellUtil
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, HasOffsetRanges, KafkaUtils, LocationStrategies}
import org.json4s.jackson.{Json, JsonMethods}
import utils.{Constants, DateUtil, HbaseUtil, OffsetUtil}

import scala.collection.mutable.ListBuffer
import scala.util.parsing.json.JSON

case class filebiteBean(timestamp: String)
object KafkaHbaseCheckPoint {
  /**
    * 创建DStream
    * @param ssc
    * @param topic
    * @param kafkaParams
    * @return
    */
  def createMyStreamingContextHbase(ssc:StreamingContext, topic:Array[String],
                                    kafkaParams:Map[String, Object]):InputDStream[ConsumerRecord[String, String]]= {

    var kafkaStreams:InputDStream[ConsumerRecord[String, String]] = null
    val (fromOffSet, flag) = OffsetUtil.getOffset(topic)

    println("获取到的Offset：" + fromOffSet)

    if (flag == 1) {
      kafkaStreams = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe(topic, kafkaParams, fromOffSet))
    } else {
      kafkaStreams = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent,
        ConsumerStrategies.Subscribe(topic, kafkaParams))
    }
    kafkaStreams
  }
  case class loveBean()

  def doOpertion(goodsCount: GoodsCountBean): Unit ={
    HbaseUtil.ensureHbaseTBExsit(Constants.goodsCheckName,Constants.goodsCheckFamilyName)
      var rowKeyString = goodsCount.goodsId+DateUtil.NowDate2()
      val cells = HbaseUtil.getCell(Constants.goodsCheckName,rowKeyString)
      if(cells != null){
        println("cells.size: "+cells.size)
        cells.foreach(x =>{
         var i = (Bytes.toString(CellUtil.cloneValue(x)).toInt+1).toString
          println("i: " + i)
          println(
            Bytes.toString(CellUtil.cloneRow(x))+"  "+          // 获取row
            Bytes.toString(CellUtil.cloneFamily(x))+"  "+     // 获取列族
            Bytes.toString(CellUtil.cloneQualifier(x))+"  "+ // 获取列名
            Bytes.toString(CellUtil.cloneValue(x))     // 获取值
          )
          //保存数据
          HbaseUtil.singlePut(Constants.goodsCheckName,rowKeyString,
            Constants.goodsCheckFamilyName,Constants.goodsCheckInfoFamilyCellName,i)
        })
      }
      else {
        val value = ""
        HbaseUtil.singlePut(Constants.goodsCheckName,rowKeyString,
          Constants.goodsCheckFamilyName,Constants.goodsCheckInfoFamilyCellName,"1")
      }
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    // spark streaming 配置
    val conf = new SparkConf().setAppName("offSet Hbase").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(5))

    HbaseUtil.setConf(Constants.ZKquorum,"2181")
    // Kafka 配置
    val brokerList = "data01.bigdata.com:9092,name01.bigdata.com:9092,name01.bigdata.com:9092"
    val topics = Array(Constants.topicName)
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokerList,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "zk_group1",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false:java.lang.Boolean)
    )


    // StreamingContext
    val lines = createMyStreamingContextHbase(ssc, topics, kafkaParams)

    lines.foreachRDD(rdds => {
      println("####################################################" )
      import scala.collection.mutable._
      //var massages = rdds.foreach(x => x.value())
      var arrayList = new ListBuffer[GoodsCountBean]()
      if(!rdds.isEmpty()) {

        rdds.foreach(x =>{
          println(x.value())
//          var messageMap = JSON.parseFull(x.value())match {
//            case Some(map:collection.immutable.Map[String, Any]) => map
//          }
//          var num = messageMap.get("message").toString.split(" ")
//          val goodsCountBean = GoodsCountBean(num(5),num(6),num(7),num(8),num(9))
//          println("goodsId: "+goodsCountBean.goodsId)
//          arrayList.append(goodsCountBean)
//          //println("arrayList: "+arrayList.size)
//          //处理业务逻辑
//          doOpertion(goodsCountBean)
        })
        //println("arrayList.Size: " + arrayList.size)

        //  保存新的 Offset
       // OffsetUtil.storeOffSet(rdds.asInstanceOf[HasOffsetRanges].offsetRanges, topics)
      }
      println("####################################################" )


    })

    // 启动
    ssc.start()
    ssc.awaitTermination()
  }

}
