package mr.test03;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.mapreduce.LoadIncrementalHFiles;

public class LoadData {
    public static void main(String[] args) throws Exception {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum","hadoop100,hadoop110,hadoop120");
        //获取数据库的连接
        Connection connection = ConnectionFactory.createConnection(configuration);

        //获取表的管理对象
        Admin admin = connection.getAdmin();
        Table myuser2 = connection.getTable(TableName.valueOf("USER_INFO"));
        LoadIncrementalHFiles loadIncrementalHFiles = new LoadIncrementalHFiles(configuration);
        loadIncrementalHFiles.doBulkLoad(new Path("hdfs://hadoop100:8020/hbase/out_hfile_3"),admin,myuser2,connection.getRegionLocator(TableName.valueOf("USER_INFO")));
    }
}
