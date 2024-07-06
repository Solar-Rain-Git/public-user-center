package com.solar.userbackend.Service;

import com.solar.userbackend.Entity.Email;

public interface EmailService {
    String sendEmail(Email email);

    String updatePwdEmail(Email email);

    Boolean banInfoToEmail(Email email);

    String tag(String color, String content);
}
