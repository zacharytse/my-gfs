package com.xcq.client.model.response;

/**
 * 统一响应规范类
 *
 * @author vip
 * @date 2019/11/27 16:44
 */
public class RestResponse<T> {
    /**
     * 附加属性
     */
    private Object extra;
    /**
     *请求结果描述
     */
    private String description;
    /**
     *请求结果代码
     */
    private Integer errorCode;
    /**
     *返回数据的数量
     */
    private int dataSize;
    /**
     *数据
     */
    private T data;
    /**
     * 调用是否成功,true表示成功,false表示失败
     */
    private Boolean success;
    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
        if(ErrorCode.NO_ERROR.getCode().equals(errorCode)) {
            this.success=true;
        }else {
            this.success=false;
        }
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public int getDataSize() {
        return dataSize;
    }
    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public Boolean getSuccess() {
        return success;
    }
    public Boolean isSuccess() {
        return success;
    }
    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
