package org.yfh.controller;

import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yfh.common.Result;
import org.yfh.controller.request.SearchParam;
import org.yfh.model.Sku;
import org.yfh.service.SkuEsService;
import org.yfh.service.dto.SkuEsDTO;

@RequestMapping("sku")
@RestController
@RequiredArgsConstructor
@Api("商品")
public class SkuController {
  private final SkuEsService skuEsService;

  @PostMapping(value = "")
  public Result<Object> onSale(@RequestBody List<SkuEsDTO> skuList) {
    skuEsService.onSale(skuList);
    return Result.success();
  }

  @GetMapping("/{keyword}")
  public Result<List<Sku>> search(@PathVariable String keyword) {
    return Result.success(skuEsService.search(keyword));
  }

  @GetMapping("")
  public Result<List<Sku>> advanceSearch(SearchParam searchParam) {
    return Result.success(skuEsService.advanceSearch(searchParam));
  }
}
