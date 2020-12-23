package utils

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by lhh
  */
object DateUtil {

  def NowDate1():String={
    val now = new Date()
    val format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = format1.format(now)
    date
  }

  def NowDate2():String={
    val now = new Date()
    val format1 = new SimpleDateFormat("yyyyMMdd")
    val date = format1.format(now)
    date
  }

}
