package cn.jpzpers.utils;

import cn.jpzpers.exe.JavaExceptionHandler;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.lang.reflect.Field;

//json工具
public final class GsonUtil {
  private static Gson gson = null;
  static {
   // System.out.println("gson>>>>>>");
    try {
      gson = new GsonBuilder()
          .setFieldNamingStrategy(new FieldNamingStrategy() {
            public String translateName(Field arg0) {
              return arg0.getName().toUpperCase();
            }
          })
          .create();
     // System.out.println("gson>>>>>>"+gson);
    } catch (Exception e) {
      JavaExceptionHandler.handleEx(e);
    }
  }

  private GsonUtil() {
  }

  public static Gson getGson() {
    return gson;
  }
}
