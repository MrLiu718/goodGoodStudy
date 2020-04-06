package com.kkb.flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/28 20:53
 */
public class HotTopDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setParallelism(1);
        DataStreamSource<String> inputDS = environment.socketTextStream("hadoop100", 9999);

        SingleOutputStreamOperator<UserBehavior> mapDS = inputDS.map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String s) throws Exception {
                String[] split = s.split(",");
                UserBehavior userBehavior = UserBehavior.of(Long.parseLong(split[0]), Long.parseLong(split[0]), Long.parseLong(split[0]), split[0], Long.parseLong(split[0]));
                return userBehavior;
            }
        });

        //todo 加水印
        SingleOutputStreamOperator<UserBehavior> watermarksDS =
                mapDS.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserBehavior>(){
                    @Override
                    public long extractAscendingTimestamp(UserBehavior userBehavior) {
                        return userBehavior.getTimeStamp()*1000;
                    }
                });

        //todo 窗口统计点击量,设置窗口大小为20s，10s滑动一次

        DataStream<ItemViewCount> windowedData = watermarksDS.keyBy("itemId") //按字段分组
                .timeWindow(Time.seconds(5), Time.seconds(2))
                .aggregate(new CountAgg(), new WindowResultFunction()); //使用aggregate做增量聚合统计

        //todo TopN 计算最热门商品
        DataStream<String> topItems = windowedData
                .keyBy("windowEnd")
                .process(new TopNHotItems(2));  // 求点击量前名的商品
        topItems.print();
        environment.execute("Hot Items Job");
    }
}

