package com.lhh;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/2/8 11:34
 */
public class KafkaConsumerControllerOffset {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop100:9092,hadoop110:9092,hadoop120:9092");
        props.put("group.id", "controllerOffset");
        //关闭自动提交，改为手动提交偏移量
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        //指定消费者要消费的topic
        consumer.subscribe(Arrays.asList("test02"));

        //定义一个数字，表示消息达到多少后手动提交偏移量
        final int minBatchSize = 20;

        //定义一个数组，缓冲一批数据
        List<ConsumerRecord<String, String>> buffer = new ArrayList<ConsumerRecord<String, String>>();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(50);
            String key = "0";
            for (ConsumerRecord<String, String> record : records) {
                buffer.add(record);
                key = record.key();
            }
            if (buffer.size() >= minBatchSize) {
                //insertIntoDb(buffer);  拿到数据之后，进行消费
                System.out.println("缓冲区的数据条数："+buffer.size()+" 最后的key："+key);
                System.out.println("我已经处理完这一批数据了...");
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}
