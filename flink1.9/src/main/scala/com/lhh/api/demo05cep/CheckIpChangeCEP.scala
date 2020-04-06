package com.lhh.api.demo05cep

import java.{lang, util}

import com.lhh.api.Constants
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.flink.cep.PatternSelectFunction
import org.apache.flink.cep.pattern.conditions.IterativeCondition
import org.apache.flink.cep.scala.{CEP, PatternStream}
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * @description
  * @author: liuhh
  * @date: Created in 2020/3/25 16:11
  * @version 0.0.1
  */
object CheckIpChangeCEP {
  private val format: FastDateFormat = FastDateFormat.getInstance("yyy-MM-dd HH:mm:ss")

  def main(args: Array[String]): Unit = {
    import org.apache.flink.api.scala._
    val environment: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val inputDS: DataStream[String] = environment.socketTextStream("hadoop100",9999)

    //得到流
    val resultDS: KeyedStream[(String, UserLogin), String] = inputDS.map(x => {
      val strings = x.split(",")
      (strings(1), UserLogin(strings(0), strings(1), strings(2), strings(3)))
    })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[(String, UserLogin)](Time.seconds(5)) {
        //使用BoundedOutOfOrdernessTimestampExtractor 指定时间值为5s，表示允许数据最大的乱序时间为5s
        override def extractTimestamp(element: (String, UserLogin)): Long = {
          //element._2.time
          val time: Long = format.parse(element._2.time).getTime
          time
        }
      }).keyBy(x => x._1)

    //创建pattern 判断连续的ip，更换了就进行报警 判断第一次IP与第二次IP不一样
    val pattens: Pattern[(String, UserLogin), (String, UserLogin)]
      = Pattern.begin[(String, UserLogin)](Constants.BEGIN)
      .where(x => x._2.username != null)
      .next(Constants.SECOND)
      .where(new IterativeCondition[(String, UserLogin)] {
        override def filter(t: (String, UserLogin), context: IterativeCondition.Context[(String, UserLogin)])
        : Boolean = {
          val values: util.Iterator[(String, UserLogin)] = context.getEventsForPattern(Constants.BEGIN).iterator()
          while (values.hasNext) {
            val tuple: (String, UserLogin) = values.next()
            if (!t._2.ip.equals(tuple._2.ip)) {
              return true
            }
          }
          false
        }
      })
      .within(Time.seconds(180))

    //pattern与流都有了，将pattern作用到流上面去
    val patternStream: PatternStream[(String, UserLogin)] = CEP.pattern(resultDS,pattens)

    patternStream.select(new Owenfuction).print()

    environment.execute()

  }

}
class Owenfuction extends PatternSelectFunction[(String,UserLogin),String]{
  override def select(map: util.Map[String, util.List[(String, UserLogin)]]): String = {
     val username: String = map.get(Constants.SECOND).iterator().next()._2.username
    username+"您的ip变化嘞"
  }
}
case class UserLogin1 (ip:String,username:String,operateUrl:String,time:String)
