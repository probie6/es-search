package org.yfh.common.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.elasticsearch.search.SearchHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class EsCovertUtils {
  public static final Gson gson = new Gson();

  public static <T> T covert(SearchHit source, Class<T> tClass) {
    String esSource = source.getSourceAsString();
    return gson.fromJson(esSource, tClass);
  }

  public static <T> List<T> covert(SearchHit[] source, Class<T> tClass) {
    List<T> result = Lists.newArrayList();
    for(SearchHit searchHit : source) {
      String esSource = searchHit.getSourceAsString();
      result.add(gson.fromJson(esSource, tClass));
    }
    return result;
  }
}
