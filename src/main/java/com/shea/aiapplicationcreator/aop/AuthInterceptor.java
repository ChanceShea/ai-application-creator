package com.shea.aiapplicationcreator.aop;

import com.shea.aiapplicationcreator.annotation.AuthCheck;
import com.shea.aiapplicationcreator.exception.BusinessException;
import com.shea.aiapplicationcreator.exception.ErrorCode;
import com.shea.aiapplicationcreator.model.entity.User;
import com.shea.aiapplicationcreator.model.enums.UserRoleEnum;
import com.shea.aiapplicationcreator.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 权限拦截器
 * @author : Shea.
 * @since : 2026/6/7 10:05
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    public Object doInterceptor(ProceedingJoinPoint pjp, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum userRoleEnum = UserRoleEnum.getByValue(loginUser.getUserRole());
        UserRoleEnum mustRoleEnum = UserRoleEnum.getByValue(mustRole);
        // 不需要权限，直接放行
        if (mustRoleEnum == null) {
            return pjp.proceed();
        }
        // 用户无权限，拒绝
        if (userRoleEnum == null) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 当前接口需要管理员权限，但是用户没有管理员权限，拒绝
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 用户有权限，放行
        return pjp.proceed();
    }
}
