package org.yfh.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {
  public static final String PATTERN = "yyyy-MM-dd HH:m:ss";

  public static String LocalDateToStr(LocalDateTime localDateTime) {
    if(Objects.isNull(localDateTime)) {
      return "";
    }
    DateTimeFormatter df = DateTimeFormatter.ofPattern(PATTERN);
    return df.format(localDateTime);
  }
}
