package com.shea.aiapplicationcreator.model.dto.user;

import com.shea.aiapplicationcreator.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户查询请求
 * @author : Shea.
 * @since : 2026/6/7 10:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends PageRequest implements Serializable {

    private Long id;

    private String userName;

    private String userAccount;

    private String userProfile;

    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}
