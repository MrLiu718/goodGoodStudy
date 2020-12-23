package cn.pzpers.utils

/**
  * author: Johnny
  * contact: zhendong.bai@hypers.com
  * file: Constants
  */
object Constants {

  // 每一个实体和主键的分割符，以及主键和主键的分割符
  val SEPARATOR: String = ","
  val KEYSEPARATOR : String = ":"

  val KAFKA_TOPIC_OFFSETS="TOPIC_OFFSETS"
  // // 分组后的分区
  val REPARTITION_NUM = 5
  val BATCH_REPARTITION_NUM = 50
  // 每一次批次执行数量
  val BATCH_UPDATE_SIZE = 10
  // 证券价格
  @Deprecated
  val STOCK_PRICE: String = "STK_PRICE"

  // 汇率
  //  @Deprecated
  //  val EXCHANGE_RATE: String = "EXCHANGE_RATE"

  // 用户资产
  val USER_STOCK_ASSET = "USER_STOCK_ASSET"
  // sec_kind1
  val SEC_KIND1_PREFIX = "KIND,"
  val SEC_KIND1_PREFIX_2 = "SEC_KIND,"
  // 表示表名的json字段
  val TABLE_FIELD = "table"
  // 代表custid 的字段
  val CUST_CODE = "cust_code"
  val USER_ID_LIST: String = "custid,cust_code,khh,cust_code,user_code,cust_code_in,cust_no"
  val DATA_TIME = "data_time"
  val DATA_TIME_LIST: String = "data_time,date_time"
  // ogg json 字段名称
  val CURRENT_TS = "LOADERTIME"
  // : "2018-06-19T08:52:55.161000",
  val OP_TS = "op_ts"
  // : "2018-06-19 00:52:47.078555",
  val OP_TYPE = "operType"
  // : "I",
  val POS = "pos"
  // : "00000000050003417038",
  val TABLE = "tableName"
  // : "RZQKGDB.ORDERS"
  val AFTER = "After"
  val COLUMN_INFO = "columnInfo"
  val SYSID = "SERVERID"
  val BEFORE = "Before"
  var USER_STOCK_ASSET_1 = "USER_STOCK_ASSET_1"
  var WRITE_FLG = "WRITE_FLG"
  var READ_FLG = "READ_FLG"

  val CREDITPLEDGESTKRATE_BASE = "CREDITPLEDGESTKRATE_BASE:"
  val STKTRD_BASE = "STKTRD_BASE:"
  val T02PROD = "T02PROD:"
  val CALENDAR_BASE = "CALENDAR_BASE:"
  val OTCINFO_BASE = "OTCINFO_BASE:"
  val OTCINSTBASEINFO_BASE = "OTCINSTBASEINFO_BASE:"

  val CREDITTARGETSTK_BASE = "CREDITTARGETSTK_BASE:"
  val RISK_CREDIT_STATUS_BASE = "RISKCREDITSTATUS_BASE:"
  val RISK_CREDIT_LINES_BASE = "RISKCREDITLINES_BASE:"

  //债券协议逆回购所需基表  主键为stkcode
  val STOCK_BASE_ZQZY = "STOCK_BASE_ZQZY:"
  //集中交易
  val FUND_BASE = "FUND_BASE:"
  //两融
  val LRFUND_BASE = "LRFUND_BASE:"
  val OFCODE_BASE = "OFCODE_BASE:"
  val OTCINSTINFO_BASE = "OTCINSTINFO_BASE:"
  val GOLD_BASE =  "GOLD_BASE:"
  val GOLDEN_PERSON_CUST_NO_BASE = "GOLDEN_PERSON_CUST_NO_BASE:"
  // pif.vm_pif_cpbqmx 基表前缀
  val BASE_PIF_VM_PIF_CPBQMX = "BASE_PIF_VM_PIF_CPBQMX:"
  // pif.tpif_cpdm_gl_sdx 基表前缀
  val BASE_PIF_TPIF_CPDM_GL_SDX = "BASE_PIF_TPIF_CPDM_GL_SDX:"
  //可取日维表
  val BASE_NEXT_TRD_DT = "BASE_NEXT_TRD_DT:"
  //t-1日行情表
  val STK_TICK = "STK_TICK:"
  val SH_TICK="sh_hq"
  val SZ_TICK="sz_hq"
  //\"SecurityID\":\"143692\"
  val SECURITYID="SecurityID"
  // 汇率
  val EXCHANGE_RATE: String = "EXCHANGE_RATE:"
  //期权行情表
  val OPT_TICK = "OPT_TICK:"
  val GOLDEN_TICK = "GOLDEN_TICK:"
  //  opt_stds.dbo.OPT_INFO
  val OPT_INFO = "OPT_INFO:"
  //otc
  val OTC_TICK = "OTC_TICK:"
  //ofcode
  val OFCODENAV_BASE = "OFCODENAV_BASE:"
  val OFCODINFO_BASE = "OFCODINFO_BASE:"
  val OFCODINFO = "OFCODINFO:"
  //otcts.otc_inst_org
  val OTCINST_ORG = "OTCINST_ORG:"
  //ofta
  val OFTA = "OFTA:"
  val GPZY_DETAIL = "GPZY_DETAIL:"

  //goldperson.delivery_variety
  val DELI_VARIETY ="DELI_VARIETY:"
  //newstock
  val NEWSTOCK_BASE = "NEWSTOCK_BASE:"

  def main(args: Array[String]): Unit = {
    //    val str = """{"SITE_ID": "01", "SERVERID": "1", "SNO": "712424", "ORDERDATE": "20171221", "CUSTID": "10100000921.0", "DATA_TIME": "2017-12-22T09:55:31.030000000000"}"""
    //
    //    val instance = GsonUtil.getGson.fromJson(str, classOf[Cbs_bb_contract])
    //
    //    val mapKey = instance.serverid + Constants.SEPARATOR + instance.sno + Constants.SEPARATOR + instance.orderdate + Constants.SEPARATOR + instance.site_id
    //    println(mapKey)
  }
}

