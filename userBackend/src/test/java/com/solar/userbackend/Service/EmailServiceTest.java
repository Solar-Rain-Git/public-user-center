package com.solar.userbackend.Service;

import com.solar.userbackend.Entity.Email;
import com.solar.userbackend.Service.impl.EmailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailServiceTest {

    @Autowired
    private EmailServiceImpl emailService;

    @Test
    void sendEmail() {
        Email email = new Email();
        email.setEmail("936648031@qq.com");
        email.setContent("已被禁用，了解具体原因请联系官方邮箱!");
        email.setSubject("Solar-Rain 用户中心 账户禁用通知");
        Boolean verCode = emailService.banInfoToEmail(email);
        System.out.println("Verification code sent: " + verCode);
    }
}
