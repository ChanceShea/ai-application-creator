package com.shea.aiapplicationcreator.utils;

import com.shea.aiapplicationcreator.exception.BusinessException;
import com.shea.aiapplicationcreator.exception.ErrorCode;

/**
 * @author : Shea.
 * @since : 2026/6/6 22:42
 */
public class ThrowUtils {

    public static void throwIf(boolean condition,ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new BusinessException(errorCode, message);
        }
    }
}
