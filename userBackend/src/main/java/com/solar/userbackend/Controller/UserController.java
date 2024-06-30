package com.solar.userbackend.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.solar.userbackend.Common.BaseResponse;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Common.ResultUtils;
import com.solar.userbackend.Entity.Request.UserLoginRequest;
import com.solar.userbackend.Entity.Request.UserRegisterRequest;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.solar.userbackend.Constant.UserConstant.ADMIN_ROLE;
import static com.solar.userbackend.Constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author solar_rain
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) { // 使用封装对象来接受请求参数
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.params_error, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.params_error, "请求参数不完整");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) { // 使用封装对象来接受请求参数
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.params_error, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.params_error, "请求参数不完整");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> outLogin(HttpServletRequest request) { // 使用封装对象来接受请求参数
        if (request == null) {
            throw new BusinessException(ErrorCode.params_error, "注销信息不存在");
        }
        int result = userService.outLogin(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.not_login, "当前用户未登录");
        }
        long userId = currentUser.getId();
        // todo 校验用户状态与合法
        User user = userService.getById(userId);
        User safeUser = userService.getSafetyUser(user);
        return ResultUtils.success(safeUser);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", user.getUserAccount())
                .or()
                .eq("phone", user.getPhone());

        List<User> userList = userService.list(queryWrapper);
        for (User value : userList) {
            if (Objects.equals(value.getUserAccount(), user.getUserAccount()) && !Objects.equals(value.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.params_error, "请求参数错误,账户已存在");
            }
            if (Objects.equals(value.getPhone(), user.getPhone()) && !Objects.equals(value.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.params_error, "请求参数错误,号码已存在");
            }
        }
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    @PostMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestBody User user, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.no_auth, "没有权限");
        }
        List<User> userList = userService.searchUsers(user);
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.no_auth, "没有权限");
        }
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.null_error, "请求参数错误,账户不存在");
        }

        // 禁止删除管理员
        User user = userService.getById(userId);
        if (user.getUserRole() == ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.no_auth, "没有权限删除管理员");
        }

        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     *
     * @param request http
     * @return boolean
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
