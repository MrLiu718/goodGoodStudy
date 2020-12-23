package cn.pzpers.pros

import cn.jpzpers.exe.JavaExceptionHandler
import cn.pzpers.utils.Constants._
import cn.pzpers.utils.{Constants, UserStkAssetCDCHandler}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream

import scala.collection.mutable.ArrayBuffer
//value属性信息
object CustDataType extends Enumeration {
  type CustDataType = Value
  //purpose : expose value to outer class or object
  val USER_STK_ASSET, EX_RATE = Value // define specific enum instance
}

//处理数据
object CdcBusinessHandlers {
  /*
    // 每一个线程池里有多少的任务
    val tasksPerPool: Int = 100

    // 每一个线程池的线程数量
    val poolSize = 100*/

  import CustDataType._
//处理表数据
  def handleAllTablesCdc(objectTupleStream: DStream[(String, String, Long)]): Unit = {
    // 处理 包含 客户号 的表数据
    process(objectTupleStream, Array(CUST_CODE), CustDataType.USER_STK_ASSET)
  }

  /**
    * process specific business.
    *
    * @param objectTupleStream source data stream
    * @param groupByFields     fields sorting by
    * @param dataType          data type ,such as USER_STK_ASSET .
    */
  // 处理 包含 客户号 的表数据
  def process(objectTupleStream: DStream[(String, String, Long)], groupByFields: Array[String], dataType: CustDataType): Unit = {
    var separators = ""
    groupByFields.length match {
      case 1 => separators = ""
      case 0 => separators = ""
      case _ => for (i <- 1 until groupByFields.length) {
        separators += Constants.KEYSEPARATOR
      }
    }

    objectTupleStream
      .map(innerTuple => {
        try {
          val record = RecordPreprocessor.extractRecord(innerTuple._1, innerTuple._2, innerTuple._3)
          if(record != null) {
            RecordPreprocessor.mergeToSnapshotRecord(record)
          } else {
            null
          }
        } catch {
          case e: Exception => JavaExceptionHandler.handleEx(e);null
        }
      })
      .filter(t => t != null)
      .map(record => (RecordPreprocessor.getValues(record, groupByFields), record))
      .filter(t => t._1 != separators)
      .foreachRDD(
        rdd => {
          val groupRdd = rdd.groupByKey(Constants.REPARTITION_NUM)
          groupRdd.foreachPartition(
            groups => {
              proGroupsCDC(groups, dataType,sort = true)
            }
          )
        }
      )
  }
//批量处理
  def batchProcess(objectTupleRdd: RDD[(String, String)], groupByFields: Array[String], dataType: CustDataType): Unit = {
    var separators = ""
    groupByFields.length match {
      case 1 => separators = ""
      case 0 => separators = ""
      case _ => for (i <- 1 until groupByFields.length) {
        separators += Constants.KEYSEPARATOR
      }
    }

    val rdd = objectTupleRdd
      .map(innerTuple => {
        try {
          //table, null, null,DataOpType.UPDATE , json, -1,json
          RecordPreprocessor.toSnapshotRecord(innerTuple._1,innerTuple._2)
        } catch {
          case e: Exception => JavaExceptionHandler.handleEx(e);null
        }
      })
      .filter(t => t != null)
      .map(record => (RecordPreprocessor.getValues(record, groupByFields), record))
      // rdd.collect().foreach(println)
      rdd.filter(t => t._1 != separators)
      .groupByKey(Constants.BATCH_REPARTITION_NUM)
      .foreachPartition(
        groups => {
          proGroupsCDC(groups, dataType,sort = false)
        }
      )
  }

//分组
  def proGroupsCDC(groups: Iterator[(String, Iterable[ProcessedIncrementalRecord])],
                   dataType: CustDataType,sort:Boolean): Unit = {
    // 先每固定数量分到同一个组里面
    val groupArrs = new ArrayBuffer[ArrayBuffer[(String, Iterable[ProcessedIncrementalRecord])]]()
    var groupArr: ArrayBuffer[(String, Iterable[ProcessedIncrementalRecord])] = null
    val src = groups.toList
    for (i <- src.indices) {
      if (i % Constants.BATCH_UPDATE_SIZE == 0) {
        groupArr = new ArrayBuffer[(String, Iterable[ProcessedIncrementalRecord])]()
        groupArrs += groupArr
      }
      groupArr += src(i)
    }

    groupArrs.foreach(
      groups => {
//        println("groups>>>>>>>"+groups)
        proGroupsBusiness(groups, dataType,sort)
        /*groups.foreach(group => {
          try {
            proGroupBusiness(group, dataType,sort)
          } catch {0
            case e: Exception => JavaExceptionHandler.handleEx(e)
          }
        })*/
      }
    )
  }
  // 按组处理数据
  def proGroupsBusiness(groups: ArrayBuffer[(String, Iterable[ProcessedIncrementalRecord])], dataType: CustDataType,sort:Boolean):Unit = {
    val processedResult = groups.map(
      group => {
        val key = group._1
        val singleGroupCDCRecords = RecordPreprocessor.processSingleGroupCDC2InstancesList(group,sort)
        (key, singleGroupCDCRecords)
      }
    )
    proGroupsCdc(processedResult,dataType)
  }

  def proGroupsCdc(records: ArrayBuffer[(String,ArrayBuffer[ProcessedIncrementalRecord])], dataType: CustDataType): Unit = {
    dataType match {
      case CustDataType.USER_STK_ASSET => UserStkAssetCDCHandler.batchUpdateUserStkAsset(records)
      case _ => Unit
    }
  }

}
