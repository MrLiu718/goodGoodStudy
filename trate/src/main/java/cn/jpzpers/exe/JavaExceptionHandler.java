package cn.jpzpers.exe;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class JavaExceptionHandler {
    // 1 调试 0 生产
    private static final Boolean isDebug;
    private static final String exBrokers;
    private static final String exTopic;
    //private static final KafkaMessageProducer producer;
    static {
        Config conf = ConfigFactory.load("application.conf");
        isDebug = conf.getBoolean("isDebug");
        exBrokers = conf.getString("exBrokers");
        exTopic = conf.getString("exTopic");
        if (!isDebug) {
            //producer = new KafkaMessageProducer(exBrokers, exTopic);
        } else {
            //producer = null;
        }
    }

    public static void main(String[] args) {
        try {
            throw new RuntimeException(new IOException("Error"));
        } catch (Exception e) {
            handleEx(e);
        }
    }

    public static void handleEx(Exception e) {
        if (isDebug){
            throw new RuntimeException(e);
        } else {
            //producer.sendMessage(getPrintTrace(e));
        }
    }

    private static String getPrintTrace(Exception e) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        e.printStackTrace(printStream);
        printStream.flush();
        byte[] buffer = byteArrayOutputStream.toByteArray();
        String message = new String(buffer);
        printStream.close();
        try {
            byteArrayOutputStream.close();
        } catch (Exception e1) {
            handleEx(e1);
        }
        return message;
    }
}
