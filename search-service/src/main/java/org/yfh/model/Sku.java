package org.yfh.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Sku {

  private Long skuId;
  private Long spuId;
  private String spuName;
  private String skuTitle;
  private BigDecimal skuPrice;
  private String skuImg;
  private Long saleCount;
  private Boolean hasStock;
  private Long hotScore;
  private Long brandId;
  private Long catalogId;
  private String brandName;
  private String catalogName;
  private String onSaleDate;
  private List<Attribute> attrs;

  @Data
  public static class Attribute {

    private Long attrId;
    private String attrName;
    private String attrValue;
  }
}
