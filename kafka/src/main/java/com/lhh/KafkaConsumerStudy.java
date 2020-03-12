package com.lhh;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/2/8 10:58
 */
public class KafkaConsumerStudy {
    public static void main(String[] args) {
        //准备配置属性
        Properties properties = new Properties();
        //kafka集群地址
        properties.put("bootstrap.servers","hadoop100:9092,hadoop110:9092,hadoop120:9092");
        //消费者组ID
        properties.put("group.id","test02");
        //自动提交偏移量
        properties.put("enable.auto.commit","true");
        //自动提交偏移量的时间间隔
        properties.put("auto.commit.interval.ms","1000");
        //默认是latest
        //earliest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        //latest: 当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        //none : topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        properties.put("auto.offset.reset","earliest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);
        //指定消费哪些topic
        kafkaConsumer.subscribe(Arrays.asList("test02"));

        while (true){
            ConsumerRecords<String, String> poll = kafkaConsumer.poll(100);

            for (ConsumerRecord<String, String> record : poll) {
                System.out.printf("partition"+record.partition()+" offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }

        }
    }
}
