package utils

object Constants {
  //offset存放的表名
  var offsetTableName = "kafka_offSet"
  //ZK地址
  var ZKquorum = "data01.bigdata.com:2181,name01.bigdata.com:2181,name01.bigdata.com:2181"
  //topic名字
  var topicName = "kt-nfi-fundasset-sh-dsg"
  //offSet表的Family字段名称
  var offSetColumnFamilyName = "TopicName"
  //商品点击量表名
  var goodsCheckName = "click_goods"

  //商品点击量表Family字段名称
  var goodsCheckFamilyName = "info"

  //商品点击量表Family中cell名称
  var goodsCheckInfoFamilyCellName = "CheckNum"
}
