package cn.jpzpers.exe;

/**
 * Created by admin on 2018-6-25. process message exception
 */
public class ProcessingMessageException extends RuntimeException {

  public ProcessingMessageException(String message) {
    super(message);
  }

  public ProcessingMessageException(Throwable cause) {
    super(cause);
  }


  public ProcessingMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}