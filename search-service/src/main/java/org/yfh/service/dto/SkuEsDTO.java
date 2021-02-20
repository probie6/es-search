package org.yfh.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.yfh.common.utils.DateUtils;
import org.yfh.model.Sku;
import org.yfh.service.enums.AttrEnum;

@Data
public class SkuEsDTO {

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
  private LocalDateTime onSaleDate = LocalDateTime.now();
  private List<AttributeDTO> attributeDTOList;

  @Data
  public static class AttributeDTO {

    private AttrEnum attrEnum;
    private String attrValue;
  }

  public Sku covert() {
    Sku sku = new Sku();
    BeanUtils.copyProperties(this, sku);
    sku.setOnSaleDate(DateUtils.LocalDateToStr(this.onSaleDate));
    List<Sku.Attribute> attributes = Lists.newArrayList();
    for (AttributeDTO attributeDTO : this.attributeDTOList) {
      Sku.Attribute attr = new Sku.Attribute();
      attr.setAttrId(attributeDTO.getAttrEnum().getId());
      attr.setAttrName(attributeDTO.getAttrEnum().getName());
      attr.setAttrValue(attributeDTO.getAttrValue());
      attributes.add(attr);
    }
    sku.setAttrs(attributes);
    return sku;
  }
}
