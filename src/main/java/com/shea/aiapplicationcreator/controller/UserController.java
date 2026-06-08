package com.shea.aiapplicationcreator.controller;

import com.mybatisflex.core.paginate.Page;
import com.shea.aiapplicationcreator.annotation.AuthCheck;
import com.shea.aiapplicationcreator.common.DeleteRequest;
import com.shea.aiapplicationcreator.common.Result;
import com.shea.aiapplicationcreator.constant.UserConstant;
import com.shea.aiapplicationcreator.exception.BusinessException;
import com.shea.aiapplicationcreator.exception.ErrorCode;
import com.shea.aiapplicationcreator.model.dto.user.UserAddDTO;
import com.shea.aiapplicationcreator.model.dto.user.UserQueryDTO;
import com.shea.aiapplicationcreator.model.dto.user.UserRegisterDTO;
import com.shea.aiapplicationcreator.model.dto.user.UserUpdateDTO;
import com.shea.aiapplicationcreator.model.entity.User;
import com.shea.aiapplicationcreator.model.vo.LoginUserVO;
import com.shea.aiapplicationcreator.model.vo.UserVO;
import com.shea.aiapplicationcreator.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shea.aiapplicationcreator.utils.ThrowUtils.throwIf;

/**
 * 用户 控制层。
 *
 * @author Shea
 * @since 2026-06-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 保存用户。
     *
     * @param user 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> save(@RequestBody UserAddDTO dto) {
        throwIf(dto == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean saved = userService.save(user);
        throwIf(!saved,ErrorCode.OPERATION_ERROR);
        return Result.success(true);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> remove(@RequestBody DeleteRequest request) {
        if (request == null || request.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return Result.success(userService.removeById(request.getId()));
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Boolean> update(@RequestBody UserUpdateDTO dto) {
        if (dto == null || dto.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        boolean b = userService.updateById(user);
        throwIf(!b, ErrorCode.OPERATION_ERROR);
        return Result.success(true);
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryDTO dto) {
        throwIf(dto == null, ErrorCode.PARAMS_ERROR);
        int page = dto.getPage();
        int size = dto.getSize();
        Page<User> userPage = userService.page(Page.of(page, size), userService.getQueryWrapper(dto));
        Page<UserVO> voPage = new Page<>(page,size,userPage.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        voPage.setRecords(userVOList);
        return Result.success(voPage);
    }

    /**
     * 用户注册
     * @param dto 用户注册参数
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody UserRegisterDTO dto) {
        throwIf(dto == null, ErrorCode.PARAMS_ERROR);
        String userAccount = dto.getUserAccount();
        String userPassword = dto.getUserPassword();
        String checkPassword = dto.getCheckPassword();
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return Result.success(result);
    }

    /**
     * 用户登录
     * @param dto 用户登录参数
     * @param request HttpServletRequest
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<LoginUserVO> login(@RequestBody UserRegisterDTO dto, HttpServletRequest request) {
        throwIf(dto == null, ErrorCode.PARAMS_ERROR);
        String userAccount = dto.getUserAccount();
        String userPassword = dto.getUserPassword();
        LoginUserVO vo = userService.userLogin(userAccount, userPassword,request);
        return Result.success(vo);
    }

    /**
     * 获取当前登录用户
     * @param request HttpServletRequest
     * @return 登录用户
     */
    @GetMapping("/get/login")
    public Result<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return Result.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 用户注销
     * @param request HttpServletRequest
     * @return 注销结果
     */
    @PostMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        return Result.success(userService.userLogout(request));
    }
}
