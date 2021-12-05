package com.xcq.client.model.response;

import java.io.Serializable;

/**
 * 统一Service层返回数据规范类
 *
 * @author vip
 * @date 2019/11/27 16:44
 */
public class Result<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 错误码
     */
    private ErrorCode errorCode;
    /**
     * 数据
     */
    private T data;
    /**
     * 错误信息描述
     */
    private String description;

    /**
     * 拓展字段
     */
    private Object extra;

    public ErrorCode getErrorCode()
    {
        return errorCode;
    }
    public void setErrorCode(ErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }
    public T getData()
    {
        return data;
    }
    public void setData(T data)
    {
        this.data = data;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isSuccess(){
        return ErrorCode.NO_ERROR.equals(errorCode);
    }

    public Object getExtra() {
        return extra;
    }
    public void setExtra(Object extra) {
        this.extra = extra;
    }
}

