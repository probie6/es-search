package org.yfh.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.yfh.common.EsConstant;
import org.yfh.common.exception.EsException;
import org.yfh.common.utils.EsCovertUtils;
import org.yfh.config.ElasticSearchConfig;
import org.yfh.controller.request.SearchParam;
import org.yfh.model.Sku;
import org.yfh.service.dto.SkuEsDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkuEsService {
  private final RestHighLevelClient restHighLevelClient;

  /**
   * 商品上架
   * @param skuList sku列表
   */
  public void onSale(List<SkuEsDTO> skuList) {

    try {
      for(SkuEsDTO skuEsDTO : skuList) {
        IndexRequest request = new IndexRequest(EsConstant.PRODUCT_INDEX);
        Sku sku = skuEsDTO.covert();
        request.source(new Gson().toJson(sku), XContentType.JSON);
        restHighLevelClient.index(request, ElasticSearchConfig.COMMON_OPTIONS);
      }
    } catch (IOException e) {
      throw new EsException("ElasticSearch保存商品异常");
    }
  }

  /**
   * 根据skuTitle或spuName检索
   * @param keyword 检索关键词
   * @return 商品列表
   */
  public List<Sku> search(String keyword) {
    SearchRequest searchRequest = new SearchRequest(EsConstant.PRODUCT_INDEX);
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, "skuTitle", "spuName"));
    sourceBuilder.from(0);
    sourceBuilder.size(5);
    searchRequest.source(sourceBuilder);
    try {
      SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig
          .COMMON_OPTIONS);
      return EsCovertUtils.covert(response.getHits().getHits(), Sku.class);
    } catch (IOException e) {
      throw new EsException("ElasticSearch检索商品异常");
    }
  }

  public List<Sku> advanceSearch(SearchParam param) {
    SearchRequest searchRequest = new SearchRequest(EsConstant.PRODUCT_INDEX);
    SearchSourceBuilder sourceBuilder = buildSearchSourceBuilder(param);
    searchRequest.source(sourceBuilder);
    try {
      SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig
          .COMMON_OPTIONS);
      return EsCovertUtils.covert(response.getHits().getHits(), Sku.class);
    } catch (IOException e) {
      throw new EsException("ElasticSearch检索商品异常");
    }
  }

  private SearchSourceBuilder buildSearchSourceBuilder(SearchParam param) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    // 关键字
    if(StringUtils.isNotEmpty(param.getKeyword())) {
      boolQueryBuilder.must(QueryBuilders.multiMatchQuery(param.getKeyword(), "skuTitle", "spuName"));
    }
    // 品牌ID 匹配多个
    if(CollectionUtils.isNotEmpty(param.getBrandIds())) {
      boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandIds()));
    }
    // 分类ID
    if(StringUtils.isNotEmpty(param.getCatalogId())) {
      // filter不进行分值计算 性能更快
      boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalogId()));
    }
    // 价格区间
    if(StringUtils.isNotEmpty(param.getSkuPriceStart()) || StringUtils.isNotEmpty(param.getSkuPriceEnd())) {
      RangeQueryBuilder priceRangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
      if (StringUtils.isNotEmpty(param.getSkuPriceStart())) {
        priceRangeQueryBuilder.gte(param.getSkuPriceStart());
      }
      if (StringUtils.isNotEmpty(param.getSkuPriceEnd())) {
        priceRangeQueryBuilder.lte(param.getSkuPriceEnd());
      }
      boolQueryBuilder.filter(priceRangeQueryBuilder);
    }

    // 上架日期区间
    if(StringUtils.isNotEmpty(param.getStartTime()) || StringUtils.isNotEmpty(param.getEndTime())) {
      RangeQueryBuilder dateRangeQueryBuilder = QueryBuilders.rangeQuery("onSaleDate");
      if(StringUtils.isNotEmpty(param.getStartTime())) {
        dateRangeQueryBuilder.gte(param.getStartTime());
      }
      if(StringUtils.isNotEmpty(param.getEndTime())) {
        dateRangeQueryBuilder.lte(param.getEndTime());
      }
      boolQueryBuilder.filter(dateRangeQueryBuilder);
    }

    // 属性nested
    if(CollectionUtils.isNotEmpty(param.getAttrs())) {
      for(String attr : param.getAttrs()) {
        String[] attrArr = attr.split("_");
        String attrId = attrArr[0];
        String attrValue = attrArr[1];
        BoolQueryBuilder attrsBoolQueryBuilder = QueryBuilders.boolQuery();
        attrsBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrId", Long.valueOf(attrId)));
        attrsBoolQueryBuilder.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));
        NestedQueryBuilder attrNestedQueryBuilder = QueryBuilders.nestedQuery("attrs", attrsBoolQueryBuilder,
            ScoreMode.None);
        boolQueryBuilder.filter(attrNestedQueryBuilder);
      }
    }
    sourceBuilder.query(boolQueryBuilder);
    // 分页
    sourceBuilder.from(param.getPageNum());
    sourceBuilder.size(param.getPageSize());
    log.info("构建的DSL：{}", sourceBuilder.toString()); // 可以直接复制到kibana-Dev ToolS使用
    return sourceBuilder;
  }
}
