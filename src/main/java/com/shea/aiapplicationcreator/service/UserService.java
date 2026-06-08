package com.shea.aiapplicationcreator.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.shea.aiapplicationcreator.model.dto.user.UserQueryDTO;
import com.shea.aiapplicationcreator.model.entity.User;
import com.shea.aiapplicationcreator.model.vo.LoginUserVO;
import com.shea.aiapplicationcreator.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author Shea
 * @since 2026-06-06
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 确认密码
     * @return 用户id
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);

    /**
     * 获取登录用户信息
     * @param user 用户
     * @return 登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 用户登录
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request HTTP请求
     * @return 登录用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     * @param request HTTP请求
     * @return 当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     * @param request HTTP请求
     * @return 是否成功
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取用户信息
     * @param user 用户
     * @return 用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取用户信息列表
     * @param userList 用户列表
     * @return 用户信息列表
     */
    List<UserVO> getUserVOList(List<User> userList);

    /**
     * 获取查询条件
     * @param dto 查询参数
     * @return 查询条件
     */
    QueryWrapper getQueryWrapper(UserQueryDTO dto);

    /**
     * 加密密码
     * @param password 密码
     * @return 加密后的密码
     */
    String getEncryptPassword(String password);
}
