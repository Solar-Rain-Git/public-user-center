package com.solar.userbackend.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Entity.Email;
import com.solar.userbackend.Entity.User;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Mapper.UserMapper;
import com.solar.userbackend.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Random;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Resource
    private JavaMailSender mailSender;
    @Resource
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public String sendEmail(Email email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email.getEmail());
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.params_error, "该邮箱已注册");
        }
        return commonEmail(email);
    }

    @Override
    public String updatePwdEmail(Email email) {
        return commonEmail(email);
    }

    private String commonEmail(Email email) {
        String who = email.getEmail();
        if (StringUtils.isAnyBlank(who)) {
            throw new BusinessException(ErrorCode.params_error, "请输入您的正确邮箱地址");
        }
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(email.getSubject());
            String verCode = VerCodeGenerateUtil();
            String verCodeDescription = tag("#FC5531", email.getContent() + " " + verCode) + " ，有效期3分钟，请尽快填写验证码完成验证！";
            helper.setText(buildContent(verCodeDescription), true);
            helper.setTo(who);
            helper.setFrom("Solar-Rain 用户中心 <" + from + ">");
            mailSender.send(message);
            return verCode;
        } catch (MessagingException e) {
            log.error("发送邮件失败：{}", e.getMessage());
            throw new BusinessException(ErrorCode.params_error, "发送邮件失败，请稍后尝试");
        }
    }

    @Override
    public Boolean banInfoToEmail(Email email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(email.getSubject());
            helper.setText(buildContent(email.getContent()), true);
            helper.setTo(email.getEmail());
            helper.setFrom("Solar-Rain 用户中心 <" + from + ">");
            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            log.error("发送邮件失败：{}", e.getMessage());
            throw new BusinessException(ErrorCode.params_error, "发送邮件失败，请稍后尝试");
        }
    }

    @Override
    public String tag(String color, String content) {
        return "<span style=\"color:" + color + ";font-size: 20px;font-weight: bold\">" + content + "</span>";
    }

    private String VerCodeGenerateUtil() {
        String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
        Random RANDOM = new SecureRandom();
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }

    private String buildContent(String verCodeDescription) {
        ClassPathResource resource = new ClassPathResource("Freemarker/mailTemplate.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuilder buffer = new StringBuilder();
        String line = "";
        try {
            inputStream = resource.getInputStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.error("读取邮件模板失败：{}", e.getMessage());
            throw new BusinessException(ErrorCode.params_error, "读取邮件模板失败，请稍后尝试");
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.error("关闭文件读取器失败：{}", e.getMessage());
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("关闭输入流失败：{}", e.getMessage());
                }
            }
        }
        return MessageFormat.format(buffer.toString(), verCodeDescription);
    }
}
