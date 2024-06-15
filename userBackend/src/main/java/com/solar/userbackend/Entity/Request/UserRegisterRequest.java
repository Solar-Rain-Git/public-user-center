package com.solar.userbackend.Entity.Request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author solar_rain
 */
@Data
public class UserRegisterRequest implements Serializable {
    private static final long serialVersionUID = -8230696502081440180L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
