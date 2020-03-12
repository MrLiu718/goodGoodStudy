package com.lhh.designMode;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/11 15:55
 * 单例模式
 */

/**
 * 双检索
 */
public class Standlone {
    //私有的静态变量
    private static Standlone standlone;
    //私有的构造方法
    private Standlone(){}
    //共公的方法
    public Standlone getStandlone(){
        if(null == standlone){
            synchronized (Standlone.class){
                if(null == standlone){
                    standlone = new Standlone();
                }
            }
        }
        return standlone;
    }
}

/**
 * 饿汉模式
 */
class Standlone2{
   private final static Standlone2 standlone2 = new Standlone2();

   private Standlone2(){}

   public Standlone2 getStandlone2(){
       return  standlone2;
   }
}

/**
 * 静态内部类
 */

class Standlone3{
    private Standlone3(){}

    private static class getStandlone3{
        private final static Standlone3 standlone3 = new Standlone3();
    }

    public Standlone3 getOneStandlone3(){
        return getStandlone3.standlone3;
    }
}
