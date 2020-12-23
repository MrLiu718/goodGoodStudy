package cn;

import org.apache.log4j.Logger;

import java.util.Random;

public class LogCreate {

    public static void main(String[] args) {
        while (true){
            //url地址
            String[] userIds = { "1","2","3","4","5","6","7","8","9","10"};
            String[] goodsId = { "11","22","33","44","55","66","77","88","99","100"};
            String[] pageId = { "111","222","333","444","555","666","777","888","999","1000"};
            String count1 = new java.util.Random().nextBoolean() ? "1" : "0";
            String count2 = new java.util.Random().nextBoolean() ? "1" : "0";
            Logger log = Logger.getLogger(LogCreate.class);
            log.info("点击日志: "+userIds[new Random().nextInt(10)]+" "+
                    goodsId[new Random().nextInt(10)]+" "+
                    pageId[new Random().nextInt(10)]+" "+ count1+" " + count2
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
