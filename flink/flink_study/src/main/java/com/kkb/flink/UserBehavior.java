package com.kkb.flink;

import java.io.Serializable;

/**
 * @version 0.0.1
 * @description
 * @author: liuhh
 * @date: Created in 2020/3/30 13:01
 */
public class UserBehavior {
        public long userId;     // 用户id
        public long itemId;  // 商品id
        public long categoryId;  // 分类id
        public String behavior;  // 行为
        public long timeStamp;  // 时间戳

        public long getItemId() {
            return itemId;
        }

        public void setItemId(long itemId) {
            this.itemId = itemId;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(long categoryId) {
            this.categoryId = categoryId;
        }

        public String getBehavior() {
            return behavior;
        }

        public void setBehavior(String behavior) {
            this.behavior = behavior;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }

        public static UserBehavior of(long userId, long itemId, long categoryId, String behavior, long timeStamp) {
            UserBehavior result = new UserBehavior();
            result.itemId = itemId;
            result.userId = userId;
            result.categoryId = categoryId;
            result.behavior = behavior;
            result.timeStamp = timeStamp;
            return result;
        }
}
