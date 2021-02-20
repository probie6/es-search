package org.yfh.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Result<T> {
  private int code;
  private boolean success;
  private String message;
  private T data;

  public static <T> Result<T> success() {
    Result<T> result = new Result<>();
    result.setCode(HttpStatus.OK.value());
    result.setMessage(HttpStatus.OK.getReasonPhrase());
    result.setSuccess(true);
    result.setData(null);
    return result;
  }

  public static <T> Result<T> success(T data) {
    Result<T> result = new Result<>();
    result.setCode(HttpStatus.OK.value());
    result.setMessage(HttpStatus.OK.getReasonPhrase());
    result.setSuccess(true);
    result.setData(data);
    return result;
  }

  public static <T> Result<T> failed() {
    Result<T> result = new Result<>();
    result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    result.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    result.setSuccess(false);
    return result;
  }

  public static <T> Result<T> failed(HttpStatus status) {
    Result<T> result = new Result<>();
    result.setCode(status.value());
    result.setMessage(status.getReasonPhrase());
    result.setSuccess(false);
    return result;
  }
}
