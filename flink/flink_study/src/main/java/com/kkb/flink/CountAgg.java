package com.kkb.flink;


import org.apache.flink.api.common.functions.AggregateFunction;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/30 13:09
 * todo 功能是统计窗口中的条数，即遇到一条数据就加一
 */
public class CountAgg  implements AggregateFunction<UserBehavior, Long, Long> {
        @Override
    public Long createAccumulator() { //创建一个数据统计的容器，提供给后续操作使用。
        return 0L;
    }

    @Override
    public Long add(UserBehavior userBehavior, Long acc) { //每个元素被添加进窗口的时候调用。
        return acc + 1;
    }

    @Override
    public Long getResult(Long acc) {
        ;//窗口统计事件触发时调用来返回出统计的结果。
        return acc;
    }

    @Override
    public Long merge(Long acc1, Long acc2) { //只有在当窗口合并的时候调用,合并2个容器
        return acc1 + acc2;
    }

}