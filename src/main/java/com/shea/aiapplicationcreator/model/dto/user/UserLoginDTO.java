package com.shea.aiapplicationcreator.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录参数
 * @author : Shea.
 * @since : 2026/6/7 09:38
 */
@Data
public class UserLoginDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;
}
