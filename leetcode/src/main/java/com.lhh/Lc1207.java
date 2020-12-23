package com.lhh;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Lc1207 {

    public static boolean unqiureNums(int[] arr){

        int arrLength = arr.length;

        Map<Integer, Integer> numsMap1 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> numsMap2 = new HashMap<Integer, Integer>();
        for (int i = 0; i < arrLength; i++) {
            int num = arr[i];
//            if(null == numsMap.get(String.valueOf(num))){
//
//            }

            numsMap1.put(num,numsMap1.get(num)== null ? 1:numsMap1.get(num)+1);

            if (numsMap2.get(numsMap1.get(num)) != null){
                return false;
            }
            numsMap2.put(numsMap1.get(num),num);

        }
        return true;
    }

    public static boolean unqiureNums2(int[] arr){
        Map<Integer, Integer> numsMap1 = new HashMap<Integer, Integer>();

        for (int i : arr) {
            numsMap1.put(i,numsMap1.getOrDefault(i,0)+1);
        }

        return numsMap1.size() == new HashSet<Integer>(numsMap1.values()).size();
    }

    public static void main(String[] args) {
        int[] a = {1,2,2};
        System.out.println(unqiureNums2(a));
    }
}
