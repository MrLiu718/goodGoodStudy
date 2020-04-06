package lhh.api;

import com.alibaba.fastjson.JSONObject;
import lhh.bean.Person;
import lhh.util.ESutil;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/4/5 10:42
 */
public class UpdateDataApi {

    public static TransportClient client;

    public static void main(String[] args) throws Exception {
        client = ESutil.createClient();
    }

    //todo 根据系统给数据生成的id来进行更新索引
    // 直接经添加覆盖就可以
    public void updateIndex(){
        Person guansheng = new Person(5, "宋江", 88, 0, "水泊梁山", "17666666666", "wusong@kkb.com","及时雨宋江");
        client.prepareUpdate().setIndex("indexsearch").setType("mysearch").setId("5")
                .setDoc(JSONObject.toJSONString(guansheng), XContentType.JSON)
                .get();
        client.close();
    }

    //按Id进行删除
    public void deleteById(){
        DeleteResponse deleteResponse = client.prepareDelete("indexsearch", "mysearch", "14").get();
        client.close();
    }

    //todo 按照查询条件来进行删除
    public void  deleteByQuery(){

        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.rangeQuery("id").gt(2).lt(4))
                .source("indexsearch")
                .get();
        long deleted = bulkByScrollResponse.getDeleted();
        System.out.println(deleted);

    }

    //todo 删除整个索引库
    public  void  deleteIndex(){
        AcknowledgedResponse indexsearch = client.admin().indices().prepareDelete("indexsearch").execute().actionGet();
        client.close();
    }
}
