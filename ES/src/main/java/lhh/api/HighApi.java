package lhh.api;

import lhh.util.ESutil;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;

import java.util.List;
import java.util.Map;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/4/5 11:00
 */
public class HighApi {
    public static TransportClient client;

    public static void main(String[] args) throws Exception {
        client = ESutil.createClient();
    }

    //todo 每个球队的球员数量
    public void groupAndCount() {
        //1：构建查询提交
        SearchRequestBuilder builder = client.prepareSearch("player").setTypes("player");
        //2：指定聚合条件
        TermsAggregationBuilder team = AggregationBuilders.terms("player_count").field("team");
        //3:将聚合条件放入查询条件中
        builder.addAggregation(team);
        //4:执行action，返回searchResponse
        SearchResponse searchResponse = builder.get();
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms stringTerms = (StringTerms) aggregation;
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                System.out.println(bucket.getKey());
                System.out.println(bucket.getDocCount());
            }
        }
    }

    /**
     * 统计每个球队中每个位置的球员数量
     */
    public void teamAndPosition(){
        SearchRequestBuilder builder = client.prepareSearch("player").setTypes("player");
        TermsAggregationBuilder team = AggregationBuilders.terms("player_count").field("team");
        TermsAggregationBuilder position = AggregationBuilders.terms("posititon_count").field("position");
        //多重聚合的条件关系
        team.subAggregation(position);
        SearchResponse searchResponse = builder.addAggregation(team).addAggregation(position).get();
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            // System.out.println(aggregation.toString());
            StringTerms stringTerms = (StringTerms) aggregation;
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                long docCount = bucket.getDocCount();
                Object key = bucket.getKey();
                System.out.println("当前队伍名称为" +  key + "该队伍下有"+docCount + "个球员");
                Aggregation posititon_count = bucket.getAggregations().get("posititon_count");
                if(null != posititon_count){
                    StringTerms positionTrem = (StringTerms) posititon_count;
                    List<StringTerms.Bucket> buckets1 = positionTrem.getBuckets();
                    for (StringTerms.Bucket bucket1 : buckets1) {
                        Object key1 = bucket1.getKey();
                        long docCount1 = bucket1.getDocCount();
                        System.out.println("该队伍下面的位置为" +  key1+"该位置下有" +  docCount1 +"人");
                    }
                }
            }
        }
    }

    /**
     * 计算每个球队年龄最大值
     */
    public  void groupAndMax(){
        SearchRequestBuilder builder = client.prepareSearch("player").setTypes("player");
        TermsAggregationBuilder team = AggregationBuilders.terms("team_group").field("team");

        MaxAggregationBuilder age = AggregationBuilders.max("max_age").field("age");

        team.subAggregation(age);
        SearchResponse searchResponse = builder.addAggregation(team).get();
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            StringTerms stringTerms = (StringTerms) aggregation;
            List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
            for (StringTerms.Bucket bucket : buckets) {
                Aggregation max_age = bucket.getAggregations().get("max_age");
                System.out.println(max_age.toString());
            }
        }
    }

    /**
     * 统计每个球队当中的球员平均年龄，以及队员总年薪
     */
    public void avgAndSum(){
        SearchRequestBuilder builder = client.prepareSearch("player").setTypes("player");

        TermsAggregationBuilder team_group = AggregationBuilders.terms("team_group").field("team");

        AvgAggregationBuilder avg_age = AggregationBuilders.avg("avg_age").field("age");

        SumAggregationBuilder sumMoney = AggregationBuilders.sum("sum_money").field("salary");


        TermsAggregationBuilder termsAggregationBuilder = team_group.subAggregation(avg_age).subAggregation(sumMoney);

        SearchResponse searchResponse = builder.addAggregation(termsAggregationBuilder).get();
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation.toString());
        }
    }


    /**
     * 计算每个球队总年薪，并按照年薪进行排序
     *     select team, sum(salary) as total_salary from player group by team order by total_salary desc;
     */
    public void orderBySum(){
        SearchRequestBuilder builder = client.prepareSearch("player").setTypes("player");
        TermsAggregationBuilder teamGroup = AggregationBuilders.terms("team_group").field("team").order(BucketOrder.count(true));
        SumAggregationBuilder sumSalary = AggregationBuilders.sum("sum_salary").field("salary");
        TermsAggregationBuilder termsAggregationBuilder = teamGroup.subAggregation(sumSalary);
        SearchResponse searchResponse = builder.addAggregation(termsAggregationBuilder).get();

        Map<String, Aggregation> stringAggregationMap = searchResponse.getAggregations().asMap();
        System.out.println(stringAggregationMap.toString());
        Aggregations aggregations = searchResponse.getAggregations();
        for (Aggregation aggregation : aggregations) {
            System.out.println(aggregation.toString());
        }
    }


}
