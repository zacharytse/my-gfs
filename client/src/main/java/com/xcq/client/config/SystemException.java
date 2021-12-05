package com.xcq.client.config;

/**
 * vip自定义运行时异常实体，用户捕获异常后返回信息
 *
 * @author wgb
 * @date 2019/12/19/16:32
 */
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 异常状态码
     */
    private Integer errorCode;

    public SystemException(Integer errorCode, String description) {
        super(description);
        this.errorCode = errorCode;
    }

    public SystemException(Integer errorCode, String description, Throwable cause) {
        super(description, cause);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
