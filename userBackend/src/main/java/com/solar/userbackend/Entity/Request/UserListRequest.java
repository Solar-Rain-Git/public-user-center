package com.solar.userbackend.Entity.Request;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户查询请求体
 *
 * @author solar_rain
 */
@Data
public class UserListRequest implements Serializable {
    private static final long serialVersionUID = 5764655307139233124L;
    private String username;
    private String userAccount;
    private Integer gender;
    private String userPassword;
    private String phone;
    private String email;
    private Integer userStatus;
    private Integer userRole;
    private Integer current;
    private Integer pageSize;
}
