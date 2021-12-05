package com.xcq.client.model.response;

/**
 * 错误代码
 *
 * @author vip
 * @date 2019/11/27 17:05
 */
public enum ErrorCode {
    /**
     *
     */
    NO_ERROR(0,"成功"),
    DB_ERROR(1, "数据库错误"),
    INVALID_PARAMETER(3, "参数错误"),
    DATA_EXIST(4, "数据已存在"),
    LOGIN_EXCEPTION(5, "登录出错"),
    SYSTEM_ERROR(6, "系统错误"),
    LOGIC_OBEY(8, "逻辑提示"),
    FEIGIN_ERROR(7, "内部调用错误"),
    DATA_NONE(9, "数据不存在"),
    NO_PERMISSION(11,"无权访问"),
    FILE_UPLOAD(12, "文件上传错误"),
    FILE_DOWNLOAD(13, "文件下载错误"),
    FILE_ERROR(14, "文件错误"),
    FILE_MISS_CHUNKS(16, "文件部分模块上传错误"),
    WORKFLOW_ERROR(15, "工作流错误"),
    ACCESS_TOKEN_INVALID(1001,"access_token无效"),
    REFRESH_TOKEN_INVALID(1002, "refresh_token无效"),
    INSUFFICIENT_PERMISSIONS(1003,"该用户权限不足以访问该资源接口"),
    UNAUTHORIZED(1004,"访问此资源需要完全的身份验证");

    private Integer code;

    private String description;

    ErrorCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
