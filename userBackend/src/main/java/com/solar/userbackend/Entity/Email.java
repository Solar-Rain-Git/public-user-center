package com.solar.userbackend.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 邮箱验证码实体类
 *
 * @author Solar_Rain
 * <br>CreateDate 2024/7/2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {
    //    邮件接收方
    private String email;
    //    邮件主题
    private String subject;
    //    邮件内容
    private String content;
}


