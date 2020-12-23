package cn.pzpers.coretrn

import java.text.SimpleDateFormat
import java.util.Date

import cn.jpzpers.bean.UserStkAsset


/**
  * Created by bzd on 2017/9/26.
  */
trait Updatable {
  def update(userStkAsset: UserStkAsset): Unit

  //  def update():Unit
  def getTime(): String = {
    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(new Date())
  }
}
