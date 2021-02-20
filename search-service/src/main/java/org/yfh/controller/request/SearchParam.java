package org.yfh.controller.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class SearchParam {
  // 匹配skuTitle或spuName
  private String keyword;
  // 品牌ID
  private List<String> brandIds;
  // 分类ID
  private String catalogId;

  // 价格区间
  private String skuPriceStart;
  private String skuPriceEnd;
  // 上架日期区间
  private String startTime;
  private String endTime;

  // 属性 1_红色，2_5寸 表示ID为1（颜色）的属性值等于红色，ID为2（尺寸）的属性值等于5寸
  private List<String> attrs;

  private Integer pageNum;
  private Integer pageSize;
}
