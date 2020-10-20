package util;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HbaseUtil {

    private static Connection connection ;
    private final String TABLE_NAME = "myuser";
    private static Table table ;

    public static void main(String[] args) throws Exception {
        getConnection();
        createTable();
    }

    /**
     * 获取连接
     * @throws IOException
     */
    public static void getConnection() throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        //连接HBase集群不需要指定HBase主节点的ip地址和端口号
        configuration.set("HBase.zookeeper.quorum","node01:2181,node02:2181,node03:2181");
        //创建连接对象
        connection = ConnectionFactory.createConnection(configuration);
    }

    /**
     * 创建表
     * @throws IOException
     */
    public static void createTable() throws IOException {
        //获取连接对象，创建一张表
        //获取管理员对象，来对手数据库进行DDL的操作
        Admin admin = connection.getAdmin();
        //指定我们的表名
        TableName myuser = TableName.valueOf("myuser");
        HTableDescriptor hTableDescriptor = new HTableDescriptor(myuser);
        //指定两个列族
        HColumnDescriptor f1 = new HColumnDescriptor("f1");
        HColumnDescriptor f2 = new HColumnDescriptor("f2");
        hTableDescriptor.addFamily(f1);
        hTableDescriptor.addFamily(f2);
        admin.createTable(hTableDescriptor);
        admin.close();
        connection.close();
    }

    /**
     * 添加数据
     * @throws IOException
     */
    public void addData() throws IOException {
        //获取表
        table = connection.getTable(TableName.valueOf(TABLE_NAME));
        Put put = new Put("0001".getBytes());//创建put对象，并指定rowkey值
        put.addColumn("f1".getBytes(),"name".getBytes(),"zhangsan".getBytes());
        put.addColumn("f1".getBytes(),"age".getBytes(), Bytes.toBytes(18));
        put.addColumn("f1".getBytes(),"id".getBytes(), Bytes.toBytes(25));
        put.addColumn("f1".getBytes(),"address".getBytes(), Bytes.toBytes("地球人"));
        table.put(put);
        table.close();
        connection.close();
    }

    /**
     *
     * @throws IOException
     */
    public void getData() throws IOException {
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
        //通过get对象，指定rowkey
        Get get = new Get(Bytes.toBytes("0003"));

        get.addFamily("f1".getBytes());//限制只查询f1列族下面所有列的值
        //查询f2  列族 phone  这个字段
        get.addColumn("f2".getBytes(),"phone".getBytes());
        //通过get查询，返回一个result对象，所有的字段的数据都是封装在result里面了
        Result result = table.get(get);
        List<Cell> cells = result.listCells();  //获取一条数据所有的cell，所有数据值都是在cell里面 的
        for (Cell cell : cells) {
            byte[] family_name = CellUtil.cloneFamily(cell);//获取列族名
            byte[] column_name = CellUtil.cloneQualifier(cell);//获取列名
            byte[] rowkey = CellUtil.cloneRow(cell);//获取rowkey
            byte[] cell_value = CellUtil.cloneValue(cell);//获取cell值
            //需要判断字段的数据类型，使用对应的转换的方法，才能够获取到值
            if("age".equals(Bytes.toString(column_name))  || "id".equals(Bytes.toString(column_name))){
                System.out.println(Bytes.toString(family_name));
                System.out.println(Bytes.toString(column_name));
                System.out.println(Bytes.toString(rowkey));
                System.out.println(Bytes.toInt(cell_value));
            }else{
                System.out.println(Bytes.toString(family_name));
                System.out.println(Bytes.toString(column_name));
                System.out.println(Bytes.toString(rowkey));
                System.out.println(Bytes.toString(cell_value));
            }
        }
        table.close();
    }


}
