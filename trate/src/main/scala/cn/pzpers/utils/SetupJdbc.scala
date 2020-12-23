package cn.pzpers.utils

import scalikejdbc.ConnectionPool

//连接jdbc
object SetupJdbc {
  def apply(driver: String, url: String, user: String, password: String): Unit = {
    Class.forName(driver)
    ConnectionPool.singleton(url, user, password)
  }
}
