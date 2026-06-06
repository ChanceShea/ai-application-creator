package com.shea.ioc.aiapplicationcreator.common;

import lombok.Data;

/**
 * 分页请求类
 * @author : Shea.
 * @since : 2026/6/6 14:46
 */
@Data
public class PageRequest {

    /**
     * 当前页码
     */
    private int page;
    /**
     * 每页大小
     */
    private int size;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序顺序
     */
    private String sortOrder = "asc";
}
