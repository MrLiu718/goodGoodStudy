package com.lhh;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/2/8 7:26
 */
public class KafkaProducerStudy {
    public static void main(String[] args) {

        //准备配置属性
        Properties properties = new Properties();
        //kafka集群地址
        properties.put("bootstrap.servers","hadoop100:9092,hadoop110:9092,hadoop120:9092");

        //acks代表消息确认机制
        properties.put("acks","all");

        //重试次数
        properties.put("retries",0);

        //批处理的数据大小
        properties.put("batch.size",16384);

        properties.put("linger.ms",1);

        properties.put("buffer.memory",33554432);

        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
 
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        for (int i =0; i <100; i++){
            kafkaProducer.send(new ProducerRecord<String, String>("test02", Integer.toString(i), "hello-kafka-"+i));
        }
        kafkaProducer.close();

    }
}
