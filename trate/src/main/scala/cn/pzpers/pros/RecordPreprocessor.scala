package cn.pzpers.pros

import cn.jpzpers.exe.ParsingMessageException
import cn.pzpers.utils.{Constants, DataTypeConvertors, RedisSentinelUtils}
import org.json.JSONObject
import cn.pzpers.utils.Constants._

import scala.collection.JavaConversions._
import scala.collection.mutable.ArrayBuffer

/**
  * process database incremental record utils , e.g. update , delete , etc.
  */
//
object RecordPreprocessor {

  def extractRecord(key: String, message: String, offset: Long): IncrementalRecord = {
    val jsonObj: JSONObject = new JSONObject(message)
    // TODO 目前已经过滤掉 诸如 {"table":"TRAKGDB.SHARES","op_type":"I","after":"{\"CUST_CODE\":\"1824189\" }"} 的消息
    // TODO 需要后续发现问题再处理
    if (!jsonObj.has(CURRENT_TS)) {
      return null
      // throw new ParsingMessageException("current_ts field not exists in json !")
    }

    if (!jsonObj.has(TABLE)) {
      throw new ParsingMessageException("table field not exists in json !")
    }


    val table = jsonObj.getString(TABLE)
    var currentTs: String = ""
    var opTs: String = ""
    var ops: String = ""
    var opType: String = ""
    if (jsonObj.has(CURRENT_TS)) {
      currentTs = jsonObj.getString(CURRENT_TS)
    }

   /* if (jsonObj.has(OP_TS)) {
      opTs = jsonObj.getString(OP_TS)
    }

    if (jsonObj.has(POS)) {
      ops = jsonObj.getString(POS)
    }
*/
    if (jsonObj.has(OP_TYPE)) {
      opType = jsonObj.getString(OP_TYPE)
    }
    if (opType == null || opType == "" || opType.trim == "") {
      throw new ParsingMessageException(s"opType field is '$opType' ! ")
    }

    opType = opType.trim
    val record = opType match {
        //上海ServerID:9
      case "I" => {
        // insert
       /* if(jsonObj.getInt(SYSID).equals(9) || jsonObj.getInt(SYSID).equals(8)){
          IncrementalInsertRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.INSERT, after = message, offset = offset, original = message)
        }else {*/
          val columnInfo = jsonObj.getJSONObject(COLUMN_INFO)
          IncrementalInsertRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.INSERT, after = columnInfo.toString, offset = offset, original = message)
       // }
      }
      case "N" => {
           IncrementalInsertRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.INSERT, after = message, offset = offset, original = message)

      }

      case "U" => {
        // Update
        val after = jsonObj.getJSONObject(AFTER)
        val before = jsonObj.getJSONObject(BEFORE)
        IncrementalUpdateRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.UPDATE, before = before.toString(), after = after.toString, offset = offset, original = message)
      }

      case "D" => {
        // Delete
        val before = jsonObj.getJSONObject(BEFORE)
        IncrementalDeleteRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.DELETE, before = before.toString(), offset = offset, original = message)
      }
      case _ => throw new ParsingMessageException(s"unkown incremental record type: $opType")
    }
    record
  }


  /*def extractRecord(key: String, message: String, offset: Long): IncrementalRecord = {
    val jsonObj: JSONObject = new JSONObject(message)

    // TODO 目前已经过滤掉 诸如 {"table":"TRAKGDB.SHARES","op_type":"I","after":"{\"CUST_CODE\":\"1824189\" }"} 的消息
    // TODO 需要后续发现问题再处理
    if (!jsonObj.has(CURRENT_TS)) {
      return null
      // throw new ParsingMessageException("current_ts field not exists in json !")
    }

    if (!jsonObj.has(TABLE)) {
      throw new ParsingMessageException("table field not exists in json !")
    }


    val table = jsonObj.getString(TABLE)
    var currentTs: String = ""
    var opTs: String = ""
    var ops: String = ""
    var opType: String = ""
    if (jsonObj.has(CURRENT_TS)) {
      currentTs = jsonObj.getString(CURRENT_TS)
    }

    if (jsonObj.has(OP_TS)) {
      opTs = jsonObj.getString(OP_TS)
    }

    if (jsonObj.has(POS)) {
      ops = jsonObj.getString(POS)
    }

    if (jsonObj.has(OP_TYPE)) {
      opType = jsonObj.getString(OP_TYPE)
    }
    if (opType == null || opType == "" || opType.trim == "") {
      throw new ParsingMessageException(s"opType field is '$opType' ! ")
    }

    opType = opType.trim
    val record = opType match {
      case "I" => {
        // insert
        val after = jsonObj.getJSONObject(AFTER)
        IncrementalInsertRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.INSERT, after = after.toString, offset = offset, original = message)
      }

      case "U" => {
        // Update
        val after = jsonObj.getJSONObject(AFTER)
        val before = jsonObj.getJSONObject(BEFORE)
        IncrementalUpdateRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.UPDATE, before = before.toString(), after = after.toString, offset = offset, original = message)
      }

      case "D" => {
        // Delete
        val before = jsonObj.getJSONObject(BEFORE)
        IncrementalDeleteRecord(table = table, currentTs = currentTs, opTs = opTs, opType = DataOpType.DELETE, before = before.toString(), offset = offset, original = message)
      }
      case _ => throw new ParsingMessageException(s"unkown incremental record type: $opType")
    }
    record
  }*/

  def mergeToSnapshotRecord(incrementalRecord: IncrementalRecord): ProcessedIncrementalRecord = {
    incrementalRecord match {
      case IncrementalDeleteRecord(table, currentTs, opTs, opType, before, offset,original) => {
        // 将after对象里的字段值覆盖到before对象里的字段值
        val mergedObj = new JSONObject()
        val beforeObj = new JSONObject(before)
        for (key <- beforeObj.keys()) {
          mergedObj.put(key.toUpperCase,beforeObj.get(key))
        }
        ProcessedIncrementalRecord(table, currentTs, opTs, opType, mergedObj.toString, offset,original)
      }

      case IncrementalInsertRecord(table, currentTs, opTs, opType, after, offset,original) => {
        // 将after对象里的字段值覆盖到before对象里的字段值
        val mergedObj = new JSONObject()
        val afterObj = new JSONObject(after)

        for (key <- afterObj.keys()) {
          mergedObj.put(key.toUpperCase,afterObj.get(key))
        }

        ProcessedIncrementalRecord(table, currentTs, opTs, opType, mergedObj.toString, offset,original)
      }

      case IncrementalUpdateRecord(table, currentTs, opTs, opType, before, after, offset,original) => {
        // 将after对象里的字段值覆盖到before对象里的字段值
        val mergedObj = new JSONObject()
        val beforeObj = new JSONObject(before)
        val afterObj = new JSONObject(after)
        for (key <- beforeObj.keys()) {
          mergedObj.put(key.toUpperCase,beforeObj.get(key))
        }
        for (key <- afterObj.keys()) {
          mergedObj.put(key.toUpperCase,afterObj.get(key))
        }
        ProcessedIncrementalRecord(table, currentTs, opTs, opType, mergedObj.toString, offset,original)
      }
      case _ => throw new ParsingMessageException(s"merging unkown incremental record type : $incrementalRecord")
    }
  }

  def toSnapshotRecord(table: String,json:String): ProcessedIncrementalRecord = {
    ProcessedIncrementalRecord(table, null, null,DataOpType.UPDATE , json, -1,json)
  }

  def getValues(incrementalRecord: ProcessedIncrementalRecord, keys: Array[String]): String = {
    if (keys == null || keys.isEmpty) {
      throw new ParsingMessageException("RecordPreprocessor -->getValues : keys must not be empty !")
    }

    var keyStr: String = ""

    for (i <- keys.indices) {
      keys(i) match {
        case Constants.CUST_CODE => keyStr += getCustId(incrementalRecord)
        case Constants.DATA_TIME => keyStr += getDataTime(incrementalRecord)
        case key: String => keyStr += getValue(incrementalRecord, key)
      }

      if (i < keys.length - 1) {
        keyStr += Constants.KEYSEPARATOR
      }
    }
    keyStr
  }

  private def getValue(incrementalRecord: ProcessedIncrementalRecord, key: String): String = {
    try {
      val jsonObj: JSONObject = new JSONObject(incrementalRecord.snapshot)
      var keyValue: String = null
      val upperKey = key.toUpperCase
      if (jsonObj.has(upperKey)) {
        keyValue = jsonObj.get(upperKey).toString
      } else {
        keyValue = ""
      }
      keyValue
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }

  /**
    * get cust_code field value, may be the field is not named custid ,such as cust_no etc ...
    *
    * @param incrementalRecord the source to parse
    * @return
    */
  //获取系统账号所关联的客户号
  private def getCustId(incrementalRecord: ProcessedIncrementalRecord): String = {
    // DBO.GLDP_CUST_REAL_FUND=com.hypers.schema.GoldpersonCustRealFund
    // DBO.GLDP_CUST_REAL_STORAGE=com.hypers.schema.GoldpersonCustRealStorage
    // DBO.GLDP_DEFER_REAL_HOLD
    // T+0
//    GOLDPERSON.CUST_REAL_FUND=com.hypers.schema.GoldpersonCustRealFund
//    GOLDPERSON.CUST_REAL_STORAGE=com.hypers.schema.GoldpersonCustRealStorage
//    GOLDPERSON.DEFER_REAL_HOLD=com.hypers.schema.GoldpersonDeferRealHold
    val tableName = incrementalRecord.table.toLowerCase.trim
    if (tableName.startsWith("goldperson") ||
      tableName.equals("dbo.gldp_cust_real_fund") ||
      tableName.equals("dbo.gldp_cust_real_storage") ||
      tableName.equals("dbo.gldp_defer_real_hold") ||
      tableName.equals("goldperson.cust_real_fund") ||
      tableName.equals("goldperson.cust_real_storage") ||
      tableName.equals("goldperson.defer_real_hold") ||
      tableName.equals("dbo.gld_defer_real_hold") ||
      tableName.equals("dbo.gld_cust_real_fund") ||
      tableName.equals("dbo.gld_cust_real_storage")
    ) {
      return getGoldenPersonCustNo(incrementalRecord)
    }

    val list = Constants.USER_ID_LIST.split(",")
    var isFound = false
    var custKey = ""
    for (custId <- list) {
      if (!isFound) {
        custKey = getValue(incrementalRecord, custId)
        if (custKey != "") {
          isFound = true
        }
      }
    }
    custKey
  }

  // 获取贵金属系统账号所关联的客户号
  private def getGoldenPersonCustNo(incrementalRecord: ProcessedIncrementalRecord): String = {
    try {
      val jsonObj: JSONObject = new JSONObject(incrementalRecord.snapshot)
      var keyValue: String = null
      // 1. 获取贵金属账号
      val upperKey = "CUST_NO".toUpperCase
      if (jsonObj.has(upperKey)) {
        keyValue = jsonObj.get(upperKey).toString
      } else {
        keyValue = ""
      }

      // 2. 根据 贵金属账号 获取 客户号
      // select cust_no,extrainfo1 from goldperson.bankaccextrainfo#id
      // TODO 写入 基表 goldperson.bankaccextrainfo#id
      // TODO key : cust_no, value : extrainfo1
      //val realCustNo = RedisUtil.get(Constants.GOLDEN_PERSON_CUST_NO_BASE + keyValue)
      val realCustNo = RedisSentinelUtils.get(Constants.GOLDEN_PERSON_CUST_NO_BASE + keyValue)
      realCustNo
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }

  /*def main(args: Array[String]) {
    val value = ProcessedIncrementalRecord("DCDB.AXODS_F_TRA_CAPITAL",
      null,null,
      DataOpType.UPDATE,"""{"OTD_DR":0,"CAL_INT_DATE":"2018-07-06 08:00:00.0","LAST_INT_BLN":11761.69,"CHQ_ACCRUAL":0,"BACKUP_DATE":20180706,"CASH_ACCRUAL":11000,"DR_AMT":0,"OPENED_BIZ":null,"ACCOUNT":380000165575,"STATUS":"0","RELATED_ACC_FLAG":"0000","ASSETS_FLOOR":0,"SAVING_ACCRUAL":164168.66,"CR_AMT":0,"CERTIFIED_AMT":0,"SAVING_INT_TAX":0,"CURRENCY":"0","SAVING_INT":0,"CR_AVL":0,"DR_RATE_GRP":1,"DR_INT":0,"ACC_CHAR":5700,"SAVING_GRP":1,"MAC":"0Q1T34PH4A1X4YVB","BALANCE":11761.69,"AVAILABLE":11761.69,"CR_BLN":0,"USER_CODE":101472450,"TRD_FRZ":0,"OUTSTANDING":0,"OTD_AVL":0,"FROZEN":0,"UPD_DATE":"2018-06-26 03:30:52.436947","MKT_VAL":42262.88}""",-1)
    // println(getCustId(value))
  }*/

  /**
    * get data_time field value, may be the field is not named custid ,such as cust_no etc.
    *
    * @param incrementalRecord the source to parse
    * @return
    */
  private def getDataTime(incrementalRecord: ProcessedIncrementalRecord): String = {
    val list = Constants.DATA_TIME_LIST.split(",")
    var isFound = false
    var dataTime = ""
    for (dataTimeField <- list) {
      if (!isFound) {
        dataTime = getValue(incrementalRecord, dataTimeField)
        if (dataTime != "") {
          isFound = true
        }
      }
    }
    dataTime
  }

  /**
    * @param group a single user group data , i.e. one specific group just belongings to one user
    */
  def processSingleGroupCDC2InstancesList(group: (String, Iterable[ProcessedIncrementalRecord]),sort:Boolean): ArrayBuffer[ProcessedIncrementalRecord] = {
    var incrementalRecords = new scala.collection.mutable.ArrayBuffer[ProcessedIncrementalRecord]()
    group._2.foreach(
      incrementalRecord => incrementalRecords += incrementalRecord
    )
    // order by offset asc
    if(sort){
      orderByOffsetASC(incrementalRecords)
    } else { // 全量刷新是不需要按时间排序的
      incrementalRecords
    }
  }

  /**
    * order by field named offset asc
    *
    * @param array source array to sort
    * @return result ArrayBuffer
    */
  def orderByOffsetASC(array: ArrayBuffer[ProcessedIncrementalRecord]): ArrayBuffer[ProcessedIncrementalRecord] = {
    // single record doesn't need to sort
    if (array.length <= 1) {
      return array
    }

    // order by current_ts field
    array.sortWith((data1, data2) => DataTypeConvertors.date2ms(data1.currentTs).compareTo(DataTypeConvertors.date2ms(data2.currentTs)) < 0)
  }
}