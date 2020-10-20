package com.lg.base;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Random;

/**
 * 自定义数据源
 */
public class MyStreamingSource implements SourceFunction<MyStreamingSource.Item> {

    private Boolean isRunning = true;

    public void run(SourceContext<Item> sourceContext) throws Exception {

        while (isRunning){

            Item item = getItem();
            sourceContext.collect(item);
            Thread.sleep(1000);
        }
    }

    public void cancel() {

        isRunning = false;
    }
    private Item getItem(){
        int nextInt = new Random().nextInt(100);
        String name = "name" + nextInt;
        Item item = new Item();
        item.setId(nextInt);
        item.setName(name);
        return item;
    }

    class Item{
        private String name;
        private Integer id;

        Item() {
        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        private Integer getId() {
            return id;
        }

        void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
