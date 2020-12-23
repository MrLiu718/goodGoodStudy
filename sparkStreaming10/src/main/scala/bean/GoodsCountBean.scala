package bean

import scala.beans.BeanProperty

/**
  * 商品收藏点击实体
  * @param goodsId
  * @param userId
  * @param pageId
  * @param clickCount
  * @param collectCount
  */
case class GoodsCountBean(
                           @BeanProperty var goodsId:String,
                           @BeanProperty var userId:String,
                           @BeanProperty var pageId:String,
                           @BeanProperty var clickCount:String,
                           @BeanProperty var collectCount:String
                         )