package com.solar.userbackend.Controller;

import com.solar.userbackend.Common.BaseResponse;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Common.ResultUtils;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Service.OSSFileService;
import com.solar.userbackend.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.solar.userbackend.Constant.UserConstant.BAN_STATUS;
import static com.solar.userbackend.Constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author solar_rain
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class OSSFileController {
    @Resource
    private OSSFileService ossfileService;
    @Resource
    private UserService userService;

    /**
     * 文件上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadAvatar")
    public BaseResponse<Boolean> upload(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.not_login, "未检测到登录用户");
        }
        long userId = currentUser.getId();
        if (currentUser.getUserStatus() == BAN_STATUS) {
            throw new BusinessException(ErrorCode.no_auth, "当前用户已禁用");
        }
        User user = userService.getById(userId);
        String imgFileStr = ossfileService.uploadAvatar(file, userId);
        if (imgFileStr == null || "".equals(imgFileStr)) {
            throw new BusinessException(ErrorCode.params_error, "上传头像失败");
        }
        user.setAvatarUrl(imgFileStr);
        boolean result = userService.updateById(user);
        return ResultUtils.success(result);
    }
}

