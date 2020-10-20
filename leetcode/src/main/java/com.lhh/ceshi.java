package com.lhh;

public class ceshi {

    public static void main(String[] args) {
        String s = "SELECT * FROM student where id>9";

        System.out.println(s.substring(0,s.indexOf(">")+1));
    }
}
