package cn.pzpers.utils

import cn.jpzpers.bean.UserStkAsset
import cn.jpzpers.exe.{JavaExceptionHandler, ProcessingMessageException}
import cn.jpzpers.utils.GsonUtil
import cn.pzpers.coretrn.{Deletable, Updatable}
import cn.pzpers.pros.{DataOpType, ProcessedIncrementalRecord, Table2Class}

import scala.collection.mutable.ArrayBuffer

/**
  * author: Johnny
  * contact: zhendong.bai@hypers.com
  * file: UserStkAssetCDCHandler
  */
//从redis中获取相应的值
object UserStkAssetCDCHandler  {

//    val writer:FileWriter = FileWriterUtil.getFileWriter
//  val log = Logger.getRootLogger

  //val logger : Logger = LogManager.getLogger(this.getClass.getName)

  def batchUpdateUserStkAsset(batchIncrementalRecords: ArrayBuffer[(String,ArrayBuffer[ProcessedIncrementalRecord])]): Unit = {
    // retrieve object from redis
    
    val readFlag: Option[String] = Option[String](RedisSentinelUtils.get(Constants.WRITE_FLG))
    // 客户号与 cdc 记录的对应关系
    val custIdAndCDCRecordsMap = batchIncrementalRecords.toMap
    // 客户号与 redis key 之间的对应关系
    val custIdRedisKeyMap = custIdAndCDCRecordsMap.keySet.map(
      custId => {
        if ("0".equals(readFlag.getOrElse("0").trim)) {
//          println("custid>>>>>>>" + custId)
          (custId, Constants.USER_STOCK_ASSET + Constants.KEYSEPARATOR + custId)
        } else {
          (custId, Constants.USER_STOCK_ASSET_1 + Constants.KEYSEPARATOR + custId)
        }
      }
    ).toMap

    // 批量获取redis key对应的value
    val redisKeys = custIdRedisKeyMap.values.map(key => key.getBytes).toArray

    // 获取 redis key 与历史记录的对应关系
    val redisKeyAndHisSnapshotMap = RedisSentinelUtils.get(redisKeys).map(redisKeyValue => {
      val bytes = redisKeyValue._2
      val redisKey = new String(redisKeyValue._1)
//      println("redisKey" + redisKey) //Ok
      if (bytes != null && bytes.nonEmpty) {
        (redisKey, SerializeUtils.deserializeObj(bytes))
      } else {
        // 如果是null，则创建一个新实例
        (redisKey, new UserStkAsset())
      }
    })
//    println("redisKeyAndHisSnapshotMap>>>>>" + redisKeyAndHisSnapshotMap)
    val processedResult = custIdAndCDCRecordsMap.map(
      custIdAndCDCRecords => {
//        println("custIdAndCDCRecords>>>>>" + custIdAndCDCRecords)
        val custId = custIdAndCDCRecords._1
        // 获取 custId对应的redis key
        val redisKey = custIdRedisKeyMap(custId)
        // 获取 redis key对应的历史snapshot
        val userStkAsset = redisKeyAndHisSnapshotMap(redisKey)
        // 最新更新的记录
        val incrementalRecords = custIdAndCDCRecords._2
        incrementalRecords.foreach(incrementalRecord => {
          doUpdateOrDelete(incrementalRecord, userStkAsset)
        })
        // 返回
        (redisKey, userStkAsset)
      }
    ).filter(tuple => tuple != null)
    // finally, batch flush to redis .
    //RedisSentinelUtils.flush2Redis(processedResult)
    RedisSentinelUtils.flush2Redis(processedResult)
  }
//更新删除
  def doUpdateOrDelete(incrementalRecord: ProcessedIncrementalRecord, userStkAsset: UserStkAsset): Unit = {

    val clazzShortName = Table2Class.getClassFullName(incrementalRecord.table)
    val clazz = Class.forName(clazzShortName)
    // 实例对象中没有的字段不会被反射，json中的多余数据会被丢弃
    // 实例对象中有多余的字段，实例的多余字段会被赋值为null.
    try {
      val instance = GsonUtil.getGson.fromJson(incrementalRecord.snapshot, clazz)
      incrementalRecord.opType match {
        // current support insert , update and delete .
        case DataOpType.INSERT => doInsert(instance, userStkAsset,incrementalRecord)
        case DataOpType.UPDATE => doUpdate(instance, userStkAsset,incrementalRecord)
        case DataOpType.DELETE => doDelete(instance, userStkAsset,incrementalRecord)
        case _ => throw new ProcessingMessageException(s"com.hypers.core.UserStkAssetCDCHandler.doUpdateOrDelete : unkown DataOpType ==> ${incrementalRecord.opType}")
      }
    } catch {
      case e: Exception => {
//        logError("error" , new RuntimeException(incrementalRecord.snapshot,e))
        JavaExceptionHandler.handleEx(new RuntimeException(incrementalRecord.snapshot,e))
      }
    }
  }

  // 插入操作
  def doInsert(instance: Any, userStkAsset: UserStkAsset, incrementalRecord: ProcessedIncrementalRecord): Unit = {
    processInsertOrUpdate(instance, userStkAsset,incrementalRecord)
  }

  // 插入和更新
  private def processInsertOrUpdate(instance: Any, userStkAsset: UserStkAsset, incrementalRecord: ProcessedIncrementalRecord): Unit = {
    try {
      instance match {
        case insOrUpdate: Updatable => {
//          writer.write("updating cdc record: " + incrementalRecord.original)
//          log.debug("updating cdc record: " + incrementalRecord.original)
//          logger.info("updating cdc record: " + incrementalRecord.original)
//          logger.debug("updating cdc record: " + incrementalRecord.original)
//          logInfo("updating cdc record: " + incrementalRecord.original)
          insOrUpdate.update(userStkAsset)
        }
        case _ => Unit
      }
    } catch {
      case e: Exception => throw new ProcessingMessageException(e)
    }
  }
  // 更新
  def doUpdate(instance: Any, userStkAsset: UserStkAsset, incrementalRecord: ProcessedIncrementalRecord): Unit = {
    processInsertOrUpdate(instance, userStkAsset,incrementalRecord)
  }
//删除
  def doDelete(instance: Any, userStkAsset: UserStkAsset, incrementalRecord: ProcessedIncrementalRecord): Unit = {
//    val date = new Date()
//    val simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
//    val timeStampStr = simpleDateFormat.format(date)
    try {
      instance match {
        case insOrUpdate: Deletable => {
//          writer.write("deleting cdc record: " + incrementalRecord.original)
//          log.debug("deleting cdc record: " + incrementalRecord.original)
//          logger.info("deleting cdc record: " + incrementalRecord.original)
          //logger.debug("deleting cdc record: " + incrementalRecord.original)
//          logInfo("deleting cdc record: " + incrementalRecord.original)
          insOrUpdate.delete(userStkAsset)
        }
        case _ => Unit
      }
    } catch {
      case e: Exception => {
        JavaExceptionHandler.handleEx(e)
      }
    }
  }
}
