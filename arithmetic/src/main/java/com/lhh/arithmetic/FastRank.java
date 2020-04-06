package com.lhh.arithmetic;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/11 17:54
 * https://blog.csdn.net/nrsc272420199/article/details/82587933
 * 快速排序
 * 快速排序 简单来说就是不断去找基准值得过程
 * ①先从队尾开始向前扫描且当low < high时,如果a[high] > tmp,则high–,但如果a[high] < tmp,
 * 则将high的值赋值给low,即arr[low] = a[high],同时要转换数组扫描的方式,即需要从队首开始向队尾进行扫描了
 * ②同理,当从队首开始向队尾进行扫描时,如果a[low] < tmp,则low++,但如果a[low] > tmp了,
 * 则就需要将low位置的值赋值给high位置,即arr[low] = arr[high],同时将数组扫描方式换为由队尾向队首进行扫描.
 * ③不断重复①和②,知道low>=high时(其实是low=high),low或high的位置就是该基准数据在数组中的正确索引位置.
 */
public class FastRank {

    public static void main(String[] args) {
        int[] arry = {49, 38, 65, 97, 23, 22, 76, 1, 5, 8, 2, 0, -1, 22};

        fastRank(arry,0,arry.length -1);
        System.out.println("排序后:");
        for (int i : arry) {
            System.out.println(i);
        }
        Map<String, Object> stringObjectHashMap = new HashMap<String, Object>();
    }

    private static void fastRank(int[] arry ,int low, int high) {
        if(low < high){

            //寻找基准值
            int index = getIndex(arry,low,high);
            // 进行迭代对index之前和之后的数组进行相同的操作使整个数组变成有序
            getIndex(arry,low,index-1);
            getIndex(arry,index+1,high);
        }
    }

    private static int getIndex(int[] arry ,int low, int high) {

        //基准值
        int tmp = arry[low];

        while(low < high){

            while(low < high && arry[high]>=tmp){
                high--;
            }

            arry[low] = arry[high];

            while(low < high && arry[low] <= tmp){
                low++;
            }

            arry[high]=arry[low];
        }
        arry[low] = tmp;
        return low;
    }


}
