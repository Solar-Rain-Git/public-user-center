package com.solar.userbackend.Constant;

/**
 * 用户常量
 *
 * @author solar-rain
 */
public interface UserConstant {
    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 用户登录态键
     */
    String VERCODE = "verCode";

    /**
     * 用户角色常量(0: 普通用户, 1: 管理员)
     */
    int DEFAULT_ROLE = 0;
    int ADMIN_ROLE = 1;
    /**
     * 用户状态常量(0: 正常, 1: 禁用)
     */
    int DEFAULT_STATUS = 0;
    int BAN_STATUS = 1;

}
