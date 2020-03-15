package mr.test02;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class HBaseHdfsMain {


    private static class HbaseHdfsMapper extends Mapper<LongWritable, Text,Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value,NullWritable.get());
        }
    }

    private static class HbaseHdfsReduce extends TableReducer<Text, NullWritable, ImmutableBytesWritable>{
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            String[] split = key.toString().split("\t");
            Put put = new Put(split[0].getBytes());
            put.addColumn("f1".getBytes(),"name".getBytes(),split[1].getBytes());
            put.addColumn("f1".getBytes(),"age".getBytes(),split[2].getBytes());
            context.write(new ImmutableBytesWritable(split[0].getBytes()),put);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = HBaseConfiguration.create();
        //设定zk集群
        conf.set("hbase.zookeeper.quorum", "hadoop100:2181,hadoop110:2181,hadoop120:2181");
        Job job = Job.getInstance(conf);

        job.setJarByClass(HBaseHdfsMain.class);

        FileInputFormat.addInputPath(job ,new Path("hdfs://hadoop100:8020/hbase/input"));

        job.setMapperClass(HbaseHdfsMapper.class);
        //map端的输出的key value 类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        //指定输出到hbase的表名
        TableMapReduceUtil.initTableReducerJob("myuser2",HbaseHdfsReduce.class,job);

        //设置reduce个数
        job.setNumReduceTasks(1);

        System.exit(job.waitForCompletion(true)?0:1);
    }
}
