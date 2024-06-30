package com.solar.userbackend.Common;

/**
 * 全局错误码
 *
 * @author soalr_rain
 */
public enum ErrorCode {
    success(0, "success", ""),
    params_error(40000, "请求参数错误", ""),
    null_error(40001, "数据不存在", ""),
    no_auth(40101, "无权限", ""),
    system_error(50000, "系统内部异常", ""),
    not_login(40100, "未登录", "");

    private final int code;

    private final String message;

    private final String description;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }
}
