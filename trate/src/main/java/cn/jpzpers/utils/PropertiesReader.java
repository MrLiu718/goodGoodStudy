package cn.jpzpers.utils;



import cn.jpzpers.exe.JavaExceptionHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**
 * 读取配置文件类
 */
public class PropertiesReader {

  private Properties properties;

  public PropertiesReader(String fileName) {
    properties = new Properties();
    try {
      properties.load(getClass().getClassLoader().getResourceAsStream(fileName));
    } catch (IOException e) {
      JavaExceptionHandler.handleEx(e);
    }
  }

  public String getValue(String key) {
    if (properties != null && key != null && !key.equals("")) {
      return properties.getProperty(key);
    } else {
      return null;
    }
  }

  public Collection getValues() {
    if (properties != null) {
      return properties.values();
    } else {
      return null;
    }
  }


  public Set getKeys() {
    if (properties != null) {

      return properties.keySet();
    } else {
      return null;
    }
  }


}
