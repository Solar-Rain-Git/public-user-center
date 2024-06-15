package com.solar.userbackend.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.solar.userbackend.Entity.User;

/**
 * @author Solar_Rain
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-06-14 12:39:09
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 检验密码
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);
}
