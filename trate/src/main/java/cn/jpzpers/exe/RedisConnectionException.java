package cn.jpzpers.exe;

/**
 * Created by admin on 2018-6-25. fetch object from redis exception
 */
public class RedisConnectionException extends RuntimeException {

  public RedisConnectionException(String message) {
    super(message);
  }

  public RedisConnectionException(Throwable cause) {
    super(cause);
  }

  public RedisConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}