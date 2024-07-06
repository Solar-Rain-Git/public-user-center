package com.solar.userbackend.Controller;

import com.solar.userbackend.Common.BaseResponse;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Common.ResultUtils;
import com.solar.userbackend.Entity.Email;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.solar.userbackend.Constant.UserConstant.VERCODE;

/**
 * @author solar_rain
 */
@Slf4j
@RestController
@RequestMapping("/email")
public class EmailController {
    @Resource
    private EmailService emailService;

    @PostMapping("/send_email")
    public BaseResponse<String> sendEmail(@RequestBody Email email, HttpServletRequest request) {
        String verCode = emailService.sendEmail(email);
        return emailMethod(request, verCode);
    }

    @PostMapping("/update_pwd_email")
    public BaseResponse<String> updatePwdEmail(@RequestBody Email email, HttpServletRequest request) {
        String verCode = emailService.updatePwdEmail(email);
        return emailMethod(request, verCode);
    }

    private BaseResponse<String> emailMethod(HttpServletRequest request, String verCode) {
        request.getSession().removeAttribute(VERCODE);
        if (verCode == null || verCode.length() != 6) {
            throw new BusinessException(ErrorCode.params_error, "发送邮件失败，请稍后尝试");
        }
        request.getSession().setAttribute(VERCODE, verCode);
        request.getSession().setMaxInactiveInterval(180);
        return ResultUtils.success("验证码已发送，请注意查看邮箱");
    }
}

