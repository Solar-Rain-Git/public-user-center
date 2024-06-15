package com.solar.userbackend.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Mapper.UserMapper;
import com.solar.userbackend.Service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author Solar_Rain
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-06-14 12:39:09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




