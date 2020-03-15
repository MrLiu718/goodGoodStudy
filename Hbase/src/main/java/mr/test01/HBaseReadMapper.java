package mr.test01;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.List;

public class HBaseReadMapper extends TableMapper<Text, Put> {
    /**
     *
     * @param key rowkey
     * @param value rowkey此行的数据 Result类型
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        //获取rowKey 字节数组
        byte[] bytes = key.get();
        String string = Bytes.toString(bytes);
        Text text = new Text(string);

        //输出数据 -> 写数据 -> Put 构建Put对象
        Put put = new Put(bytes);
        //获取一行中所有的Cell对象

        List<Cell> cells = value.listCells();
        for (Cell cell : cells) {
            byte[] cloneFamily = CellUtil.cloneFamily(cell);
            if("f1".equals(Bytes.toString(cloneFamily))){
                byte[] cloneQualifier = CellUtil.cloneQualifier(cell);
                if("name".equals(Bytes.toString(cloneQualifier))){
                    put.add(cell);
                }
                if ("address".equals(Bytes.toString(cloneQualifier))){
                    put.add(cell);
                }
            }
        }
        if (!put.isEmpty()){
            context.write(text,put);
        }
    }
}
