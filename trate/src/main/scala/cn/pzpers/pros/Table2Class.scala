package cn.pzpers.pros

object Table2Class {
  //private val conf = ConfigManager
  def getClassFullName(cls: String): String = {
    val classFullName = ""//conf.getClassOrElse(cls.toUpperCase)

//    if (classFullName == null) {
//      //throw new ParsingMessageException(s"The class name ‘${cls.toUpperCase}’ wasn't parsed, please ensure the class name in the map collection....")
//    }
    classFullName
  }

  def main(args: Array[String]): Unit = {
    println(getClassFullName("nfi_stkasset_bj20191101"))
  }
}