package com.shea.aiapplicationcreator.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户更新请求
 * @author : Shea.
 * @since : 2026/6/7 10:22
 */
@Data
public class UserUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;
}
