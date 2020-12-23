package cn.pzpers.utils

import cn.jpzpers.bean.UserStkAsset
import com.typesafe.config.ConfigFactory

import scala.collection.immutable

object ConfigManager {

//  // kafka redis mysql spark 等配置信息
//  private val conf = ConfigFactory.load("application.conf")
//
//  private val classPairs: immutable.Map[String, String] =
//    PropertiesUtil.prp2Map("classes.properties")
//
//  // table connections mapping
//  private val tableConns: immutable.Map[String, String] =
//    PropertiesUtil.prp2Map("table_conns.properties")
//
//  def getString(key: String): String = {
//    conf.getString(key)
//  }
//
//  //根据key获取配置信息
//  def getInt(key: String): Int = {
//    conf.getInt(key)
//  }
//
//  //获取配置信息
//  def getClassOrElse(clazz: String): String = {
//    classPairs.getOrElse(clazz, null)
//  }
//
//  def getBatchConnUrl(table: String): String = {
//    tableConns.getOrElse(table.trim().toUpperCase(),"")
//  }
//
//  def main(args: Array[String]): Unit = {
//    val obj:UserStkAsset = SerializeUtils.deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET_1
//      + Constants.KEYSEPARATOR  + "4619102527").getBytes()))
//    println()
//  }

  // kafka redis mysql spark 等配置信息
  private val conf = ConfigFactory.load("application.conf")
  // classes mapping
  private val classPairs: immutable.Map[String, String] =
    PropertiesUtil.prp2Map("classes.properties")

  // table connections mapping
  private val tableConns: immutable.Map[String, String] =
    PropertiesUtil.prp2Map("table_conns.properties")

  def getString(key: String): String = {
    conf.getString(key)
  }

  //根据key获取配置信息
  def getInt(key: String): Int = {
    conf.getInt(key)
  }

  //获取配置信息
  def getClassOrElse(clazz: String): String = {
    classPairs.getOrElse(clazz, null)
  }


  def getBatchConnUrl(table: String): String = {
    tableConns.getOrElse(table.trim().toUpperCase(),"")
  }

  def main(args: Array[String]): Unit = {
    //        println(getBatchConnUrl("dbo.opt_asset"))
    //108538677669
    // val obj:UserStkzAsset = SerializeUtils.deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET_1+ Constants.KEYSEPARATOR  + "4885103046").getBytes()))

    //val obj:UserStkAsset = SerializeUtils.deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET+ Constants.KEYSEPARATOR  + "117474120805").getBytes()))//108274129226


    // val obj:UserStkAsset = SerializeUtils.deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET+ Constants.KEYSEPARATOR  + "117674129976").getBytes()))//108274129226
    // println(obj.getRiskCreditStatus)

    /*  val obj:UserStkAsset = SerializeUtils.deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET+ Constants.KEYSEPARATOR  + "121474129699").getBytes()))//108274129226
      println(obj.getHoldStkValue)*/
    //val map = new  util.HashMap[String,String]()
    /* map.put("keeprate","80")
     map.put("fundid","117474120805")
     val map2 = new util.HashMap[String, util.HashMap[String, String]]()
     map2.put("8:123:117474120805",map)*/
    /*
    println(obj.setGuaranteeProportion(map2))
    println(obj.getGuaranteeProportion)
    val key1 = Array("custid", "keeprate","fundid")
    // val key2 = Array("custid", "allasset", "alldebts","fundid")
    //维持担保比例
    val values1 = Array("117474120805","80","117474120805")
    SerializeUtils.updateField("8:123:117474120805", key1, values1, obj.getGuaranteeProportion, obj.setGuaranteeProportion)*/
    /* val key1 = Array("custid", "keeprate","fundid")
      // val key2 = Array("custid", "allasset", "alldebts","fundid")
      //维持担保比例
      val values1 = Array("117474120805","80","117474120805")

      SerializeUtils.updateField("8:123:117474120805", key1, values1, obj.getGuaranteeProportion, obj.setGuaranteeProportion)

  */
    // println(obj.getCustRqCost)
    /* println(obj.getRqStarMarketCts)
     println(obj.getRqMainBoardCts)
     println(obj.getRqMainBoardThreeMonthCts)

    println("----------")
    println(obj.getRzStarMarketCts)
    println(obj.getRzMainBoardCts)
    println(obj.getRzMainBoardThreeMonthCts)*/
    // SerializeUtils.delField(mapKey, userStkAsset.getFundassetCapital, userStkAsset.setFundassetCapital)
    val obj2:UserStkAsset = SerializeUtils.
      deserializeObj(RedisSentinelUtils.get((Constants.USER_STOCK_ASSET+ Constants.KEYSEPARATOR  + "121674129023").getBytes()))
    //println(obj2.getRzStarMarketCts)


    /* val str = RedisSentinelUtils.get(Constants.STKTRD_BASE+ "1:600570")
       println(str)*/

    //println(obj.getFundidSales)
    //println(obj2.getFundidSales)

    // println(obj.getZtOther)
    //println(obj.getRzMainBoardCts)


    // println(obj.getRzMainBoardCts)
    // println(obj.getRzStarMarketCts)
    // println(obj.getRzStarMarketCts)
    //println(obj.getFundassetCapital)
    //println(obj.getRzMainBoardThreeMonthCts)
    // println(obj.getRqMainBoardThreeMonthCts)
    /*
        val dtinfo: String = RedisSentinelUtils.get(Constants.DATEROLL_BASE + SerializeUtils.strSepConcat(DateUtil.NowDt()))
        var dt_1 = ""
        var dt_3 = ""
        if (dtinfo != null) {
          val dtinfos = dtinfo.split(",")
          dt_1 = dtinfos(0)
          dt_3 = dtinfos(1)
        }


        println(dt_3)
        println(dt_1)*/
    // println()


    //val str = RedisSentinelUtils.get("STKTRD_BASE:1:601766")
    //println(str)
    //    var gpzy: java.util.HashMap[String, java.util.HashMap[String, String]] = SerializeUtils.deserializeObj(RedisUtil.get((Constants.GPZY_DETAIL + "201703290B1025186").getBytes()))
    //    println()
    //    println(RedisUtil.get(Constants.OTCINST_ORG + "8"))
    //    RedisUtil.del(Constants.USER_STOCK_ASSET + Constants.KEYSEPARATOR  + "129500184580")USER_STOCK_ASSET:4640168507
    //    println(RedisUtil.get(Constants.OTCINSTINFO_BASE + "841"))
    //        println(RedisUtil.get(Constants.OTCINSTINFO_BASE + "2412"))
    //    println(RedisUtil.get(Constants.OFCODENAV_BASE + "002640"))
    //    println(RedisUtil.get(Constants.OFCODE_BASE+"002640"))
    //    RedisUtil.flush2Redis(Constants.STOCK_BASE + "0" + ":" + "131806","G" + Constants.SEPARATOR + "Ｒ-182")
    //    println(RedisUtil.get(Constants.STOCK_BASE + "2" + ":" + "299901"))
    //    println(RedisUtil.get(Constants.DELI_VARIETY + "211"))
    //    println(RedisUtil.get(Constants.GOLDEN_TICK + "Au99.99"))
    //    RedisUtil.del(Constants.STK_TICK + "0" + Constants.KEYSEPARATOR + "0000002")
    //    println(RedisUtil.get(Constants.NEWSTOCK_BASE + "0" + Constants.KEYSEPARATOR + "159905"))
    //    println(RedisUtil.get(Constants.STK_TICK + "0" + Constants.KEYSEPARATOR + "0000002"))
  }

}
