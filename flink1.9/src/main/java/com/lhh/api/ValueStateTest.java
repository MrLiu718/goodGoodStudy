package com.lhh.api;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class ValueStateTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.fromElements(Tuple2.of(1L, 3L), Tuple2.of(1L, 5L), Tuple2.of(1L, 7L), Tuple2.of(1L, 5L), Tuple2.of(1L, 2L))
                .keyBy(0)
                .flatMap(new CountWindowAverage())
                .printToErr();
        env.execute("submit job");
    }

    public static class CountWindowAverage extends RichFlatMapFunction<Tuple2<Long, Long>, Tuple2<Long, Long>>{

        private transient ValueState<Tuple2<Long, Long>> sum;
        @Override
        public void flatMap(Tuple2<Long, Long> longLongTuple2, Collector<Tuple2<Long, Long>> collector) throws Exception {

            Tuple2<Long, Long> currentSum;
            //访问 valueState
            if(sum.value() == null){
                currentSum = Tuple2.of(0L, 0L);
            }else {
                currentSum = sum.value();
            }

            //更新
            currentSum.f0 +=1;

            currentSum.f1 += longLongTuple2.f1;

            //更新 state
            sum.update(currentSum);

            if (currentSum.f0 >= 2) {
                collector.collect(new Tuple2<>(longLongTuple2.f0, currentSum.f1 / currentSum.f0));
                sum.clear();
            }
        }

        public void open(Configuration config) {
            ValueStateDescriptor<Tuple2<Long, Long>> descriptor =
                    new ValueStateDescriptor<>(
                            "average", // state的名字
                            TypeInformation.of(new TypeHint<Tuple2<Long, Long>>() {})
                    ); // 设置默认值


            StateTtlConfig ttlConfig = StateTtlConfig
                    .newBuilder(Time.seconds(10))
                    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)
                    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
                    .build();

            descriptor.enableTimeToLive(ttlConfig);

            sum = getRuntimeContext().getState(descriptor);
        }
    }
}
