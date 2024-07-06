package com.solar.userbackend.Service;

import com.solar.userbackend.Entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * 用户服务测试
 *
 * @author Solar_Rain
 */
@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;

    @Test
    public void testUserService() {
        User user = new User();
        user.setUsername("soalr");
        user.setUserAccount("solar");
        user.setAvatarUrl("343546578900876543");
        user.setGender(0);
        user.setUserPassword("12345678654432134567");
        user.setPhone("2324567543213455");
        user.setEmail("3435657543354");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegister() {

    }
}