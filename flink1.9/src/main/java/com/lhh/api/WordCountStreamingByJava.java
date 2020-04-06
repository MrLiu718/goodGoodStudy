package com.lhh.api;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/17 19:50
 */
public class WordCountStreamingByJava {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> source = executionEnvironment.socketTextStream("hadoop100", 8888);

        SingleOutputStreamOperator<WordWithCount> sumRrsult = source.flatMap(new FlatMapFunction<String, WordWithCount>() {
            @Override
            public void flatMap(String s, Collector<WordWithCount> collector) throws Exception {
                for (String s1 : s.split(" ")) {
                    collector.collect(new WordWithCount(s1, 1));
                }
            }
        }).keyBy("word").timeWindow(Time.seconds(2)).sum("count");
        sumRrsult.print();

        executionEnvironment.setParallelism(1);
        executionEnvironment.execute();

    }

    public static class WordWithCount{
        public String word;
        public int count;

        public WordWithCount(){

        }

        public WordWithCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return "WordWithCount{" +
                    "word='" + word + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
