package com.solar.userbackend.Service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author solar_rain
 */
public interface OSSFileService {
    /**
     * 阿里云OSS文件上传
     *
     * @param file
     * @return
     */
    String uploadAvatar(MultipartFile file, Long userId);
}
