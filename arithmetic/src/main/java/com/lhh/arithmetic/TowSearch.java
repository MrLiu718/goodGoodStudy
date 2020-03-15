package com.lhh.arithmetic;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/13 1:13
 * 二分法查找
 */
public class TowSearch {
    public static int binarySearch(int arr[], int key){
        int low = 0;
        int high = arr.length - 1;
        while (low <= high){
            //防止Int越界
            int mid = low + (high -low)/2 ;
            if(key == arr[mid]){
                return mid;
            }else if (key > arr[mid]){
                low = mid + 1;
            }else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int arr[] = { 0,2,3,5,7,9,12,34,55,56,88};
        int result = binarySearch(arr, 3);
        System.out.println(result);
    }
}
