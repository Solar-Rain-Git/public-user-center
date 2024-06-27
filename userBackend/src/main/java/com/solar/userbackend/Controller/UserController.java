package com.solar.userbackend.Controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.solar.userbackend.Entity.Request.UserLoginRequest;
import com.solar.userbackend.Entity.Request.UserRegisterRequest;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) { // 使用封装对象来接受请求参数
        if (userRegisterRequest == null) {
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) { // 使用封装对象来接受请求参数
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            return null;
        }
        long userId = currentUser.getId();
        // todo 校验用户状态与合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }

    @PostMapping("/update")
    public boolean updateUser(@RequestBody User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", user.getUserAccount())
                .or()
                .eq("phone", user.getPhone());

        List<User> userList = userService.list(queryWrapper);
        for (User value : userList) {
            if (Objects.equals(value.getUserAccount(), user.getUserAccount()) && !Objects.equals(value.getId(), user.getId())) {
                return false;
            }
            if (Objects.equals(value.getPhone(), user.getPhone()) && !Objects.equals(value.getId(), user.getId())) {
                return false;
            }
        }
        return userService.updateById(user);
    }

    @PostMapping("/search")
    public List<User> searchUsers(@RequestBody User user, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return new ArrayList<>();
        }
        String username = user.getUsername();
        String userAccount = user.getUserAccount();
        Integer gender = user.getGender();
        String phone = user.getPhone();
        String email = user.getEmail();
        Integer userStatus = user.getUserStatus();
        Integer userRole = user.getUserRole();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("userName", username);
        }
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
        }
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        if (userStatus != null) {
            queryWrapper.eq("userStatus", userStatus);
        }
        if (userRole != null) {
            queryWrapper.eq("userRole", userRole);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(users -> userService.getSafetyUser(users)).collect(Collectors.toList());
    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long userId, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return false;
        }
        if (userId <= 0) {
            return false;
        }

        // 禁止删除管理员
        User user = userService.getById(userId);
        if (user.getUserRole() == ADMIN_ROLE) {
            return false;
        }

        return userService.removeById(userId);
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
