package com.shea.aiapplicationcreator.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求类
 * @author : Shea.
 * @since : 2026/6/6 14:46
 */
@Data
public class DeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
}
