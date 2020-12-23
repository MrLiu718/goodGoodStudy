package com.lhh.util;

/**
 * 往kafka中写数据
 * 可以使用这个main函数进行测试一下
 */
public class KafkaUtils {

    public static final String broker_list = "localhost:9092";
    public static final String topic = "metric";  // kafka topic，Flink 程序中需要和这个统一

}
