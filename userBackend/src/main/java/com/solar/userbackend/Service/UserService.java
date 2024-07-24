package com.solar.userbackend.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.solar.userbackend.Entity.Request.UserListRequest;
import com.solar.userbackend.Entity.Request.UserRegisterRequest;
import com.solar.userbackend.Entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Solar_Rain
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-06-14 12:39:09
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册信息
     * @return 用户ID
     */
    Long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request);

    /**
     * @param userAccount  账户
     * @param userPassword 密码
     * @return 用户脱敏信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户动态查询
     *
     * @param userListRequest
     * @return
     */
    Page<User> searchUsers(UserListRequest userListRequest);

    /**
     * @param request
     * @return
     */
    int outLogin(HttpServletRequest request);

    /**
     * 通过邮箱修改密码
     *
     * @param
     * @return
     */
    Boolean updatePassword(UserRegisterRequest userRegisterRequest, HttpServletRequest request);
}
