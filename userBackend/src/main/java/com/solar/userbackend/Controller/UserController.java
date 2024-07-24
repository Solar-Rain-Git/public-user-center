package com.solar.userbackend.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.solar.userbackend.Common.BaseResponse;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Common.ResultUtils;
import com.solar.userbackend.Common.UserPageResponse;
import com.solar.userbackend.Entity.Email;
import com.solar.userbackend.Entity.Request.UserListRequest;
import com.solar.userbackend.Entity.Request.UserLoginRequest;
import com.solar.userbackend.Entity.Request.UserRegisterRequest;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Service.EmailService;
import com.solar.userbackend.Service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.solar.userbackend.Constant.UserConstant.*;

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
    @Resource
    private EmailService emailService;

    @PostMapping("/register")
    public BaseResponse<String> userRegister(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) { // 使用封装对象来接受请求参数
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.params_error, "请求参数为空");
        }
        String result = String.valueOf(userService.userRegister(userRegisterRequest, request));
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
        if (currentUser.getUserStatus().equals(BAN_STATUS)) {
            throw new BusinessException(ErrorCode.no_auth, "当前用户已禁用");
        }
        User user = userService.getById(userId);
        User safeUser = userService.getSafetyUser(user);
        return ResultUtils.success(safeUser);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody User user, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.not_login, "未检测到登录用户");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", user.getUserAccount())
                .or()
                .eq("phone", user.getPhone())
                .or()
                .eq("email", user.getEmail());

        List<User> userList = userService.list(queryWrapper);
        for (User value : userList) {
            if (Objects.equals(value.getUserAccount(), user.getUserAccount()) && !Objects.equals(value.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.params_error, "请求参数错误,账户已存在");
            }
            if (Objects.equals(value.getPhone(), user.getPhone()) && !Objects.equals(value.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.params_error, "请求参数错误,号码已存在");
            }
            if (Objects.equals(value.getEmail(), user.getEmail()) && !Objects.equals(value.getId(), user.getId())) {
                throw new BusinessException(ErrorCode.params_error, "请求参数错误,邮箱已存在");
            }
        }

        User defaultUser = userService.getById(user.getId());

        if (!defaultUser.getUserRole().equals(ADMIN_ROLE) && !defaultUser.getUserRole().equals(user.getUserRole())) {
            checkUserWeightToEmail("账户升级通知", "#FFD364", "已升级为⭐管理员", "，请履行好管理员的职责！", user.getEmail());
        }

        if (!defaultUser.getUserStatus().equals(BAN_STATUS) && !defaultUser.getUserStatus().equals(user.getUserStatus())) {
            checkUserWeightToEmail("账户禁用通知", "#E10602", "已被禁用", "，了解具体原因请联系官方邮箱！", user.getEmail());
        }

        if (!defaultUser.getUserStatus().equals(DEFAULT_STATUS) && !defaultUser.getUserStatus().equals(user.getUserStatus())) {
            checkUserWeightToEmail("账户解禁通知", "#66B86F", "已解禁", "，可继续享用平台服务！", user.getEmail());
        }

        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }

    @PostMapping("/search")
    public BaseResponse<UserPageResponse> searchUsers(@RequestBody UserListRequest userListRequest, HttpServletRequest request) {
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.no_auth, "没有权限");
        }
        Page<User> userPage = userService.searchUsers(userListRequest);
        List<User> userList = userPage.getRecords().stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        UserPageResponse response = new UserPageResponse(userPage.getCurrent(), userPage.getSize(), userPage.getTotal(), userList);
        return ResultUtils.success(response);
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
        if (user.getUserRole().equals(ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.no_auth, "没有权限删除管理员");
        }
        checkUserWeightToEmail("账户移除通知", "#E10602", "已被系统移除", "，了解具体原因请联系官方邮箱！", user.getEmail());
        boolean result = userService.removeById(userId);
        return ResultUtils.success(result);
    }

    @PostMapping("/change_pwd")
    public BaseResponse<Boolean> updatePassword(@RequestBody UserRegisterRequest userRegisterRequest, HttpServletRequest request) { // 使用封装对象来接受请求参数
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser != null) {
            throw new BusinessException(ErrorCode.params_error, "请先退出登录");
        }
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.params_error, "请求参数为空");
        }
        boolean result = userService.updatePassword(userRegisterRequest, request);
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
        return user != null && user.getUserRole().equals(ADMIN_ROLE);
    }

    /**
     * 检查用户权重发送对应邮件
     *
     * @param theme     邮件主题
     * @param color     邮件不同主题不同颜色
     * @param content   邮件主要内容
     * @param other     邮件其他内容
     * @param emailInfo 邮件接收方
     */
    private void checkUserWeightToEmail(String theme, String color, String content, String other, String emailInfo) {
        Email email = new Email();
        email.setSubject(theme);
        email.setContent(emailService.tag(color, content) + other);
        email.setEmail(emailInfo);
        boolean banRes = emailService.banInfoToEmail(email);
        if (!banRes) {
            throw new BusinessException(ErrorCode.system_error, theme + "失败！");
        }
    }
}
