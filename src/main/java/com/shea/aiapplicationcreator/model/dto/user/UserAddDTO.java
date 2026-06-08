package com.shea.aiapplicationcreator.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户添加请求
 * @author : Shea.
 * @since : 2026/6/7 10:20
 */
@Data
public class UserAddDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户角色
     */
    private String userRole;
}
