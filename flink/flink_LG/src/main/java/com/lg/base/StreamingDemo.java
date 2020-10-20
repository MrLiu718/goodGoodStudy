package com.lg.base;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamingDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();
        //获取数据源
        DataStreamSource<MyStreamingSource.Item> itemDataStreamSource = executionEnvironment.addSource(new MyStreamingSource()).setParallelism(1);

        SingleOutputStreamOperator<MyStreamingSource.Item> itemmap = itemDataStreamSource.map((MapFunction<MyStreamingSource.Item, MyStreamingSource.Item>) value -> value);

        itemmap.print().setParallelism(1);

        executionEnvironment.execute("StreamingDemo");

    }
}
