package com.solar.userbackend;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class SampleTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.isTrue(0 == userList.size(), "");
        userList.forEach(System.out::println);
    }

}