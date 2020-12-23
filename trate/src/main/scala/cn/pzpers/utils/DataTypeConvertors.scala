package cn.pzpers.utils

import java.math.RoundingMode
import java.text.SimpleDateFormat

import cn.jpzpers.exe.ParsingMessageException

//负责时间类型和数据的转换操作
object DataTypeConvertors {
//  private val sdf: FastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss")
//  private val sdf2 = FastDateFormat.getInstance("yyyyMMdd")
  private val obj = new Object()
  private val sdf2 = new SimpleDateFormat("yyyyMMdd")
  def bigDecimal2str(original: java.math.BigDecimal): String = {
    if (original == null) {
      "0"
    } else {
      original.toBigInteger.toString(10)
    }
  }

  /*def floatStr2LongStr(original: String): String = {
    if (original == null) {
      return "0"
    }
    /*if (original.contains(".")) {
      original.split("\\.")(0)
    } else {
      original
    }*/
    val flt = original.toFloat
    val lon = flt.toLong.toString
    println(flt)
    println(lon)
    lon
  }*/
//数据nvl操作
  def nvl(original: java.lang.Double,default:java.lang.Double): java.lang.Double = {
    try {
      if (original == null) {
        default
      } else {
        original.doubleValue()
      }
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }

  def round(original: java.lang.Double): java.lang.Double = round(original,2)


  def round(original: java.lang.Double,scale:Int): java.lang.Double = {
    try {
      if (original == null) {
        0.00
      } else {
        new java.math.BigDecimal(original).setScale(scale,RoundingMode.HALF_UP).doubleValue()
      }
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }

  //数据nvl操作
  def nvl(original: java.lang.Long,default:java.lang.Long): java.lang.Long = {
    try {
      if (original == null) {
        default
      } else {
        original.longValue()
      }
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }
  //数据nvl操作
  def nvl(original: Integer,default:Integer): Integer = {
    try {
      if (original == null) {
        default
      } else {
        original
      }
    } catch {
      case e: Exception => throw new ParsingMessageException(e)
    }
  }
  //数据nvl操作
  def nvl(original: Integer): Integer = {
    nvl(original,0)
  }
  //数据nvl操作
  def nvl(original: Int): Int = {
    nvl(original,0)
  }
  //数据nvl操作
  def nvl(original: Long): Long = {
    nvl(original,0L)
  }
  //数据nvl操作
  def nvl(original: java.lang.Double): java.lang.Double = {
    nvl(original,0.0)
  }


  def nvl(original: java.math.BigInteger,default:String): String = {
    if (original == null) {
      default
    } else {
      original.toString
    }
  }

  def nvl(original: java.math.BigInteger,default:Integer): java.math.BigInteger = {
    if (original == null) {
      new java.math.BigInteger(default.toString)
    } else {
      original
    }
  }

 //求绝对值
  def abs(original: Integer): Integer = {
    Math.abs(original)
  }
  //求绝对值
  def abs(original: Int): Int = {
    Math.abs(original)
  }
  //求绝对值
  def abs(original: Long): Long = {
    Math.abs(original)
  }
  //求绝对值
  def abs(original: java.lang.Double): java.lang.Double = {
    Math.abs(original)
  }
  /*def nvl(original:Double): java.lang.Double = {
    original
  }*/


  /*def date2ms(date:String): java.math.BigDecimal = {
    var dateStr: String = ""
    try {
      // val fields: Array[String] = date.split(Pattern.quote("."))
      // 20180822113955.201002
       dateStr = date.replace("T","").replace("-","").replace(":","")
      // val ms = sdf.parse(dateStr).getTime
      // System.out.println(ms +"." + fields[1] +" before [" + ts + "] ==> after [" + sdf.format(new Date(ms)) + "." + fields[1] + "] " + ((sdf.format(new Date(ms)) + "." + fields[1]).equals(ts)));
      val decimal = new java.math.BigDecimal(dateStr)
      decimal
    } catch {
      case e:Exception => throw new RuntimeException("'"+ date +"','" + dateStr + "'", e)
    }
  }*/

  def date2ms(date:String): java.math.BigDecimal = {
    var dateStr: String = ""
    try {
      // val fields: Array[String] = date.split(Pattern.quote("."))
      // 20180822113955.201002
      dateStr = date.replace("T","").replace("-","").replace(":","").replace(" ","")
      // val ms = sdf.parse(dateStr).getTime
      // System.out.println(ms +"." + fields[1] +" before [" + ts + "] ==> after [" + sdf.format(new Date(ms)) + "." + fields[1] + "] " + ((sdf.format(new Date(ms)) + "." + fields[1]).equals(ts)));
      val decimal = new java.math.BigDecimal(dateStr)
      decimal
    } catch {
      case e:Exception => throw new RuntimeException("'"+ date +"','" + dateStr + "'", e)
    }
  }

  def subDate(dateStr1:String, dateStr2:String):Long = {
    obj.synchronized{
      try {
          val date1 = sdf2.parse(dateStr1)
          val date2 = sdf2.parse(dateStr2)
          Math.abs(date2.getTime - date1.getTime) / (1000 * 3600 * 24).toLong
      } catch {
        case e:Exception => 0L
      }
    }
  }

  def addDate(dateStr1:String,days:Int):String = {
    obj.synchronized{
      val date = sdf2.parse(dateStr1)
      date.setTime(date.getTime + (1000 * 3600 * 24 * days))
      sdf2.format(date)
    }
  }
//比较数据大小
  def greatest(num1:Integer,num2:Integer):Integer = {
    if (num1 >= num2) {
      num1
    } else {
      num2
    }
  }
  //比较数据大小
  def greatest(num1:Double,num2:Double):Double = {
    if (num1 >= num2) {
      num1
    } else {
      num2
    }
  }


  def main(args: Array[String]) {
    /*
    val sdf2 = FastDateFormat.getInstance("yyyyMMdd")
    val cal = Calendar.getInstance()
    cal.set(2018,8 - 1,8)
    println(sdf2.format(cal))
    */

//    println(round(2.335d,2))
    println(subDate("20181102","20181003"))
    print(subDate("20160601","20170907"))
  }
}
