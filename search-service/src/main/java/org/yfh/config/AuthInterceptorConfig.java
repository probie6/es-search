package org.yfh.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * All rights Reserved, Designed By www.baozun.com
 *
 * @title: LogInterceptorConfig
 * @package: com.baozun.design.ranking.log
 * @description: (用一句话描述该文件做什么)
 * @author: xugejun(giorno.xu @ baozun.com)
 * @date: 2020-03-06 11:34
 * @version: v1.0
 * @copyright: 2020 www.baozun.com Inc. All rights reserved. 注意：本内容仅限于上海宝尊电商内部传阅，禁止外泄以及用于其他的商业目的
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthInterceptorConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/**").addResourceLocations(
        "classpath:/static/");
    registry.addResourceHandler("swagger-ui.html").addResourceLocations(
        "classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations(
        "classpath:/META-INF/resources/webjars/");

  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedHeaders("*")
        .allowedMethods("*")
        .allowedOrigins("*")
        .allowCredentials(true);
  }
}
