package com.solar.userbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;


import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserBackendApplicationTests {
    /**
     * Spring加密工具类
     * @throws NoSuchAlgorithmException
     */
    @Test
    void testPassword() throws NoSuchAlgorithmException {
        String password = DigestUtils.md5DigestAsHex(("ABCD" + "myPassword").getBytes());
        System.out.println(password);
    }

    @Test
    void contextLoads() {
    }

}
