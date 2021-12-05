package com.xcq.client.model.response;

/**
 * 统一Service层返回数据对象构建类
 *
 * @author vip
 * @date 2019/11/27 16:44
 */
public class Results {
    /**
     * 创建一个成功的返回结果
     * @param <T>
     * @param data 返回数据
     * @return
     */
    public static <T> Result<T> newSuccessResult(T data) {
        return newSuccessResult(data, null);
    }
    /**
     * 创建一个成功的返回结果
     * @param <T>
     * @param data 数据
     * @param description 描述
     * @return
     */
    public static <T> Result<T> newSuccessResult(T data, String description) {
        Result<T> r = new Result<T>();
        r.setErrorCode(ErrorCode.NO_ERROR);
        r.setData(data);
        r.setDescription(description);
        return r;
    }
    /**
     * 创建一个成功的返回结果
     * @param <T>
     * @param data 数据
     * @param description 描述
     * @return
     */
    public static <T> Result<T> newSuccessResult(T data, String description, Object extra) {
        Result<T> r = new Result<T>();
        r.setErrorCode(ErrorCode.NO_ERROR);
        r.setData(data);
        r.setDescription(description);
        r.setExtra(extra);
        return r;
    }
    /**
     * 创建一个失败的返回结果
     * @param <T>
     * @param errorCode 错误码
     * @param description 描述
     * @return
     */
    public static <T> Result<T> newFailResult(ErrorCode errorCode, String description) {
        Result<T> r = new Result<T>();
        r.setDescription(description);
        r.setErrorCode(errorCode);
        return r;
    }

    /**
     * 创建一个失败的返回结果
     * @param <T>
     * @param errorCode 错误码
     * @param description 描述
     * @return
     */
    public static <T> Result<T> newFailResult(ErrorCode errorCode, T data, String description) {
        Result<T> r = new Result<T>();
        r.setDescription(description);
        r.setErrorCode(errorCode);
        r.setData(data);
        return r;
    }

    @SuppressWarnings("rawtypes")
    public static final Result   DB_ERROR=Results.newFailResult(ErrorCode.DB_ERROR, "数据库错误");
    @SuppressWarnings("rawtypes")
    public static final Result   INVALID_PARAMETER=Results.newFailResult(ErrorCode.INVALID_PARAMETER, "非法的请求参数");
    @SuppressWarnings("rawtypes")
    public static final Result   NO_ERROR = Results.newSuccessResult(null);

}

