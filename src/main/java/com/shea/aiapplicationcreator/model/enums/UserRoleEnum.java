package com.shea.aiapplicationcreator.model.enums;

import lombok.Getter;

/**
 * 用户角色枚举
 * @author : Shea.
 * @since : 2026/6/6 21:40
 */
@Getter
public enum UserRoleEnum {

    USER("用户","user"),
    ADMIN("管理员","admin");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据值获取枚举
     * @param value 值
     * @return 枚举
     */
    public static UserRoleEnum getByValue(String value) {
        if (value == null) {
            return null;
        }
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (role.text.equals(value)) {
                return role;
            }
        }
        return null;
    }
}
