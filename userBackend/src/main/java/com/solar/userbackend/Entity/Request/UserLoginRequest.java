package com.solar.userbackend.Entity.Request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author solar_rain
 */
@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = -3324080377059900475L;
    private String userAccount;
    private String userPassword;
}
