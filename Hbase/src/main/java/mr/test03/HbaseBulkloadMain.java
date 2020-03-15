package mr.test03;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat2;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * 利用bulkload 导入数据
 *
 * 原理BulkLoad会将tsv/csv格式的文件编程hfile文件,然后再进行数据的导入,
 * 这样可以避免大量数据导入时造成的集群写入压力过大
 */
public class HbaseBulkloadMain extends Configured implements Tool {

    public static class BulkloadMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            Put put = new Put(split[0].getBytes());
            put.addColumn("INFO".getBytes(),"IID".getBytes(),split[0].getBytes());
            put.addColumn("INFO".getBytes(),"ICRED".getBytes(),split[1].getBytes());
            put.addColumn("INFO".getBytes(),"ITIME".getBytes(),split[2].getBytes());
            put.addColumn("INFO".getBytes(),"IPASSWORD".getBytes(),split[3].getBytes());
            put.addColumn("INFO".getBytes(),"IAGE".getBytes(),split[4].getBytes());
            put.addColumn("INFO".getBytes(),"IUSE".getBytes(),split[5].getBytes());
            put.addColumn("INFO".getBytes(),"IJSP".getBytes(),split[6].getBytes());
            put.addColumn("INFO".getBytes(),"IWIN".getBytes(),split[7].getBytes());
            put.addColumn("INFO".getBytes(),"IBROWSER".getBytes(),split[8].getBytes());
            put.addColumn("INFO".getBytes(),"IIP".getBytes(),split[9].getBytes());
            put.addColumn("INFO".getBytes(),"IPROVINCE".getBytes(),split[10].getBytes());
            put.addColumn("INFO".getBytes(),"ICITY".getBytes(),split[11].getBytes());
            put.addColumn("INFO".getBytes(),"IPAGE".getBytes(),split[12].getBytes());
            put.addColumn("INFO".getBytes(),"IGOODS".getBytes(),split[13].getBytes());
            put.addColumn("INFO".getBytes(),"ISHOP".getBytes(),split[14].getBytes());

            context.write(new ImmutableBytesWritable(split[0].getBytes()),put);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = super.getConf();
        Job job = Job.getInstance(conf);
        job.setJarByClass(HbaseBulkloadMain.class);

        FileInputFormat.addInputPath(job,new Path("hdfs://hadoop100:8020/hbase/input"));

        //mapper
        job.setMapperClass(BulkloadMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);

        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("USER_INFO"));

        //使MR可以向myuser2表中，增量增加数据
        HFileOutputFormat2.configureIncrementalLoad(job,table,connection.getRegionLocator(TableName.valueOf("USER_INFO")));
        //数据写回到HDFS，写成HFile -> 所以指定输出格式为HFileOutputFormat2
        job.setOutputValueClass(HFileOutputFormat2.class);
        HFileOutputFormat2.setOutputPath(job,new Path("hdfs://hadoop100:8020/hbase/out_hfile_3"));
        boolean b = job.waitForCompletion(true);
        return b?1:0;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        //设定zk集群
        configuration.set("hbase.zookeeper.quorum", "hadoop100:2181,hadoop110:2181,hadoop120:2181");
        int run = ToolRunner.run(configuration, new HbaseBulkloadMain(), args);
        System.exit(run);
    }
}
