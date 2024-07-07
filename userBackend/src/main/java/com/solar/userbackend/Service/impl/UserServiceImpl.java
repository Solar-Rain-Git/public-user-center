package com.solar.userbackend.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Entity.Request.UserLoginRequest;
import com.solar.userbackend.Entity.Request.UserRegisterRequest;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Mapper.UserMapper;
import com.solar.userbackend.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.solar.userbackend.Constant.UserConstant.*;

/**
 * @author Solar_Rain
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-06-14 12:39:09
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 盐值混淆密码
     */
    private static final String SALT = "solar";

    /**
     * 用户名过滤特殊字符
     */
    private static final String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\s]";
    private static final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        // 1.检验（使用apache common utils依赖里的方法StringUtils）
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String verCode = userRegisterRequest.getVerCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, email, verCode)) {
            throw new BusinessException(ErrorCode.params_error, "请求参数不完整");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.params_error, "用户账户过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.params_error, "用户密码过短");
        }
        // 2.账户不包含特殊字符
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.params_error, "账户不能包含特殊字符");
        }
        // 3.密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.params_error, "密码和校验密码不匹配");
        }
        // 5.账户不能重复
        if (userMapper.selectCount(new QueryWrapper<User>().eq("userAccount", userAccount)) > 0) {
            throw new BusinessException(ErrorCode.params_error, "该账户已注册");
        }
        if (userMapper.selectCount(new QueryWrapper<User>().eq("email", email)) > 0) {
            throw new BusinessException(ErrorCode.params_error, "该邮箱已注册");
        }
        String verCodeInfo = (String) request.getSession().getAttribute(VERCODE);
        if (verCodeInfo == null) {
            throw new BusinessException(ErrorCode.params_error, "验证码错误，请稍后尝试");
        }
        if (!verCodeInfo.equals(verCode)) {
            throw new BusinessException(ErrorCode.params_error, "验证码错误，请在有效时长内再次输入");
        }
        // 6.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 7.插入注册数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setEmail(email);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.params_error, "用户注册失败");
        }
        request.getSession().removeAttribute(VERCODE);
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.检验（使用apache common utils依赖里的方法StringUtils）
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.params_error, "请输入正确的账户/邮箱和密码");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.params_error, "请输入正确的账户/邮箱");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.params_error, "用户密码过短");
        }
        // 2.账户不包含特殊字符
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        Matcher emailMatcher = Pattern.compile(emailPattern).matcher(userAccount);
        // 如果不是有效的电子邮件地址并且包含特殊字符，则抛出异常
        if (!emailMatcher.find() && matcher.find()) {
            throw new BusinessException(ErrorCode.params_error, "账户不能包含特殊字符");
        }
        // 3.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 4.查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount).or().eq("email", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("User login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.null_error, "当前用户不存在");
        }
        if (user.getUserStatus().equals(BAN_STATUS)) {
            log.info("User login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.no_auth, "当前用户已禁用");
        }
        // 5.用户脱敏
        User safetyUser = getSafetyUser(user);
        // 6.用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public List<User> searchUsers(User user) {
        String username = user.getUsername();
        String userAccount = user.getUserAccount();
        Integer gender = user.getGender();
        String phone = user.getPhone();
        String email = user.getEmail();
        Integer userStatus = user.getUserStatus();
        Integer userRole = user.getUserRole();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("userName", username);
        }
        if (StringUtils.isNotBlank(userAccount)) {
            queryWrapper.like("userAccount", userAccount);
        }
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.like("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.like("email", email);
        }
        if (userStatus != null) {
            queryWrapper.eq("userStatus", userStatus);
        }
        if (userRole != null) {
            queryWrapper.eq("userRole", userRole);
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 移除登录态
     *
     * @param request
     */
    @Override
    public int outLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    @Override
    public Boolean updatePassword(UserRegisterRequest userRegisterRequest, HttpServletRequest request) {
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String email = userRegisterRequest.getEmail();
        String verCode = userRegisterRequest.getVerCode();
        if (StringUtils.isAnyBlank(userPassword, checkPassword, email, verCode)) {
            throw new BusinessException(ErrorCode.params_error, "请求参数不完整");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.params_error, "密码和校验密码不匹配");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.null_error, "当前邮箱账户不存在");
        }
        String verCodeInfo = (String) request.getSession().getAttribute(VERCODE);
        if (verCodeInfo == null) {
            throw new BusinessException(ErrorCode.params_error, "验证码不存在或已过期，请填写完整邮箱发送验证码");
        }
        if (!verCodeInfo.equals(verCode)) {
            throw new BusinessException(ErrorCode.params_error, "验证码错误，请在有效时长内再次输入");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = this.updateById(user);
        request.getSession().removeAttribute(VERCODE);
        return result;
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        safetyUser.setUserRole(originUser.getUserRole());
        return safetyUser;
    }
}




