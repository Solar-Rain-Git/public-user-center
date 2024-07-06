package com.solar.userbackend.Service.impl;

import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.solar.userbackend.Common.ErrorCode;
import com.solar.userbackend.Entity.OSSConfig;
import com.solar.userbackend.Exception.BusinessException;
import com.solar.userbackend.Service.OSSFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传业务类
 *
 * @author zr 2024/2/29
 */
@Service
@Slf4j
public class OSSFileServiceImpl implements OSSFileService {
    @Resource
    private OSSConfig ossConfig;
    private final static Integer FILE_SIZE = 2;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）

    /**
     * 阿里云OSS文件上传
     *
     * @param file
     * @return
     */
    @Override
    public String uploadAvatar(MultipartFile file, Long userId) {
        //获取相关配置
        String bucketName = ossConfig.getBucketName();
        String endPoint = ossConfig.getEndPoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();

        //创建OSS对象
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);

        boolean flag = checkFileSize(file.getSize(), FILE_SIZE, FILE_UNIT);
        if (!flag) {
            throw new BusinessException(ErrorCode.params_error, "上传文件大小超出限制");
        }

        //获取原生文件名
        String originalFilename = file.getOriginalFilename();
        //JDK8的日期格式
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dft = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        //拼装OSS上存储的路径
        //String folder = dft.format(time)
        String folder = "userID-" + userId;
        String fileName = generateUUID();
        String extension = null;
        if (originalFilename != null) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        //在OSS上bucket下的文件名
        String uploadFileName = "userCenterAvatarUrl/" + folder + "/" + fileName + extension;
        //在OSS上bucket下已经存在的文件夹
        String existUpload = "userCenterAvatarUrl/" + folder + "/";

        try {
            //先清空在OSS上bucket下的用户头像文件
            ObjectListing objectListing = ossClient.listObjects(bucketName, existUpload);
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                String key = objectSummary.getKey();
                // 在此处可以打印或处理每个文件的key
                ossClient.deleteObject(bucketName, key);
            }
            //再重新上传文件
            PutObjectResult result = ossClient.putObject(bucketName, uploadFileName, file.getInputStream());
            //拼装返回路径
            if (result != null) {
                return "https://" + bucketName + "." + endPoint + "/" + uploadFileName;
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}", e.getMessage());
            throw new BusinessException(ErrorCode.params_error, "上传头像失败");
        } finally {
            //OSS关闭服务，不然会造成OOM
            ossClient.shutdown();
        }
        throw new BusinessException(ErrorCode.system_error, "上传头像失败");
    }

    /**
     * 获取随机字符串
     *
     * @return
     */
    private String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    /**
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @描述 判断文件大小
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        return !(fileSize > size);
    }
}
