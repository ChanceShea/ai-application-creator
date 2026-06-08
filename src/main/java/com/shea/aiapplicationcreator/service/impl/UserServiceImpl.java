package com.shea.aiapplicationcreator.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.shea.aiapplicationcreator.exception.BusinessException;
import com.shea.aiapplicationcreator.exception.ErrorCode;
import com.shea.aiapplicationcreator.mapper.UserMapper;
import com.shea.aiapplicationcreator.model.dto.user.UserQueryDTO;
import com.shea.aiapplicationcreator.model.entity.User;
import com.shea.aiapplicationcreator.model.enums.UserRoleEnum;
import com.shea.aiapplicationcreator.model.vo.LoginUserVO;
import com.shea.aiapplicationcreator.model.vo.UserVO;
import com.shea.aiapplicationcreator.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

import static com.shea.aiapplicationcreator.constant.UserConstant.USER_LOGIN_STATE;
import static com.shea.aiapplicationcreator.utils.ThrowUtils.throwIf;

/**
 * 用户 服务层实现。
 *
 * @author Shea
 * @since 2026-06-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度小于8");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不一致");
        }
        String encryptPassword = getEncryptPassword(userPassword);
        // 判断账号是否重复
        throwIf(existUserAccount(userAccount), ErrorCode.PARAMS_ERROR, "用户账号重复");
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userAccount);
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean isSaved = this.save(user);
        throwIf(!isSaved,ErrorCode.SYSTEM_ERROR,"注册失败");
        return user.getId();
    }

    /**
     * 获取登录用户信息
     * @param user 用户
     * @return 登录用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO vo = new LoginUserVO();
        BeanUtils.copyProperties(user,vo);
        return vo;
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度小于4");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度小于8");
        }
        // 加密密码
        String encryptPassword = getEncryptPassword(userPassword);
        // 查询用户
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("userAccount", userAccount)
                .eq("userPassword", encryptPassword);
        User user = this.mapper.selectOneByQuery(queryWrapper);
        // 判断用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号或密码错误");
        }
        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return user;
    }

    /**
     * 用户注销
     * @param request HttpServletRequest
     * @return 是否注销成功
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (attribute == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获取用户信息
     * @param user 用户
     * @return 用户信息
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取用户信息列表
     * @param userList 用户列表
     * @return 用户信息列表
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).toList();
    }

    /**
     * 获取查询条件
     * @param dto 查询条件
     * @return 查询条件
     */
    @Override
    public QueryWrapper getQueryWrapper(UserQueryDTO dto) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        QueryWrapper queryWrapper = QueryWrapper.create(dto);
        if (StrUtil.isNotBlank(dto.getSortField())) {
            queryWrapper.orderBy(dto.getSortField(), "ascend".equals(dto.getSortOrder()));
        }
        return queryWrapper;
    }


    /**
     * 用户账号是否重复
     * @param userAccount 用户账号
     * @return true 表示存在，false 表示不存在
     */
    private boolean existUserAccount(String userAccount) {
        return this.exists(QueryWrapper.create().eq("userAccount", userAccount));
    }

    /**
     * 加密密码
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    public String getEncryptPassword(String userPassword) {
        final String SALT = "Shea";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }
}
