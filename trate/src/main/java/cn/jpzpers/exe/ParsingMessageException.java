package cn.jpzpers.exe;

/**
 * Created by admin on 2018-6-25. parse message exception
 */
public class ParsingMessageException extends RuntimeException {

  public ParsingMessageException(String message) {
    super(message);
  }

  public ParsingMessageException(Throwable cause) {
    super(cause);
  }

  public ParsingMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}