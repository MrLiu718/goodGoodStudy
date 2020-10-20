package com.lg.base;


import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.configuration.Configuration;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CacheFile {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        env.registerCachedFile("D:\\practise-code\\goodGoodStudy\\flink\\flink_LG\\src\\main\\cash\\distributedcache.txt","distributedcache");

        DataSource<String> dataSource = env.fromElements("开拓者", "湖人", "雷霆", "马赛克", "勇士");

        DataSet< String> result  = dataSource.map(new RichMapFunction<String, String>() {

            private ArrayList<String> dataList = new ArrayList<String>();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);

                //使用缓存文件
                File cacheFile = getRuntimeContext().getDistributedCache().getFile("distributedcache");
                List<String> lines = FileUtils.readLines(cacheFile);
                for (String line : lines) {
                    this.dataList.add(line);
                    System.err.println("分布式缓存为:" + line);
                }

            }

            @Override
            public String map(String s) throws Exception {
                //在这里就可以使用dataList
                System.err.println("使用datalist：" + dataList + "-------" + s);
                return dataList + ":" + s;
            }
        });

        result.printToErr();
    }
}
