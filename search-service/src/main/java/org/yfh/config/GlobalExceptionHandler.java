package org.yfh.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.yfh.common.Result;
import org.yfh.common.exception.EsException;

/**
 * 全局异常处理
 *
 * @author Stephen
 * @since 2018/10/9
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(EsException.class)
  public Result<String> notReadable(EsException e) {
    log.error(e.getMessage());
    return Result.failed(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
