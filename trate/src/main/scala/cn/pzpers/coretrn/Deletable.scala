package cn.pzpers.coretrn

import cn.jpzpers.bean.UserStkAsset

/**
  * Created by bzd on 2017/9/26.
  */
trait Deletable {
  def delete(userStkAsset: UserStkAsset): Unit
}
