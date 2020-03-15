package mr.test01;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HBaseReadMain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(super.getConf(), "hbaseMain");
        job.setJarByClass(HBaseReadMain.class);
        //mapper
        TableMapReduceUtil.initTableMapperJob(TableName.valueOf("myuser"),new Scan(),HBaseReadMapper.class, Text.class
        , Put.class,job);
        //reduce
        TableMapReduceUtil.initTableReducerJob("myuser2",HBaseReadReduce.class,job);
        boolean b = job.waitForCompletion(true);
        return b?1:0;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        //设定绑定的zk集群
        configuration.set("hbase.zookeeper.quorum", "hadoop100:2181,hadoop110:2181,hadoop120:2181");
        int run = ToolRunner.run(configuration, new HBaseReadMain(), args);
        System.exit(run);
    }

}
