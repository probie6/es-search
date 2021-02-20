package org.yfh.service.enums;

public enum AttrEnum {
  COLOR(1L, "颜色"),
  SIZE(2L, "尺寸"),
  CPU(3L, "处理器"),
  MEMORY(4L, "内存");
  private Long id;
  private String name;

  AttrEnum(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

}
