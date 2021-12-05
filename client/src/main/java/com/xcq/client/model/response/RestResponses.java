package com.xcq.client.model.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 统一响应实体构建类
 *
 * @author vip
 * @date 2019/11/27 16:44
 */
public class RestResponses {
    private static final Logger logger = LoggerFactory.getLogger(RestResponses.class);

    /**
     * 创建一个Http请求的返回结果对象
     *
     * @param errorCode   错误代码 没有错误则为0
     * @param description 请求结果描述
     * @param data        数据,数量为0时,可以为null
     * @param extra       额外的数据,主要用于分页查询,返回的总页数,可以为null
     * @return 结果对象
     */
    private static <T> RestResponse<T> newResponse(Integer errorCode, String description, T data, Object extra) {
        RestResponse<T> re = new RestResponse<>();
        re.setData(data);
        int dataSize = dataSize(data);
        re.setDataSize(dataSize);
        re.setErrorCode(errorCode);
        re.setDescription(description);
        re.setExtra(extra);
        return re;
    }

    /**
     * 创建一个Http请求操作成功的返回结果对象
     *
     * @param description 请求结果描述
     * @param data        数据
     * @param extra       额外的数据,主要用于分页查询,返回的总页数,可以为null
     * @return 结果对象
     */
    public static <T> RestResponse<T> newSuccessResponse(String description, T data, Object extra) {
        return newResponse(ErrorCode.NO_ERROR.getCode(), description, data, extra);
    }

    /**
     * 创建一个Http请求,操作成功的返回结果对象
     *
     * @param description 请求结果描述
     * @param data        数据
     * @return 结果对象
     */
    public static <T> RestResponse<T> newSuccessResponse(String description, T data) {
        return newResponse(ErrorCode.NO_ERROR.getCode(), description, data, null);
    }

    public static <T> RestResponse<T> newSuccessResponse(T data) {
        return newResponse(ErrorCode.NO_ERROR.getCode(), "", data, null);
    }

    /**
     * 创建一个Http请求,操作失败的返回结果对象
     *
     * @param description 请求结果描述
     * @param errorCode   错误代码
     * @return
     */
    public static <T> RestResponse<T> newFailResponse(ErrorCode errorCode, String description) {
        return newResponse(errorCode.getCode(), description, null, null);
    }

    /**
     * 创建一个Http请求,操作失败的返回结果对象
     *
     * @param description 请求结果描述
     * @param errorCode   错误代码
     * @return
     */
    public static <T> RestResponse<T> newFailResponse(ErrorCode errorCode, T data, String description) {
        return newResponse(errorCode.getCode(), description, data, null);
    }

    /**
     * 创建一个Http请求,操作失败的返回结果对象
     *
     * @param description 请求结果描述
     * @param errorCode   错误代码
     * @return
     */
    public static <T> RestResponse<T> newFailResponse(String description, ErrorCode errorCode) {
        return newResponse(errorCode.getCode(), description, null, null);
    }

    /**
     * 创建一个Http请求,操作失败的返回结果对象
     *
     * @param errorCode 错误代码
     * @return
     */
    public static RestResponse<String> newFailResponse(ErrorCode errorCode) {
        return newResponse(errorCode.getCode(), errorCode.getDescription(), null, null);
    }

    /**
     * @param string
     * @return
     */
    public static RestResponse<Void> newSuccessResponse(String string) {
        return newSuccessResponse(string, null);
    }

    public static RestResponse<Void> newSuccessResponse() {
        return newSuccessResponse("");
    }

    /**
     * 计算数据大小，数组，集合，map
     *
     * @param <T>
     * @param data
     * @return
     * @since 0.0.3
     */
    private static <T> int dataSize(T data) {
        if (data == null) {
            return 0;
        }
        if (data.getClass().isArray()) {
            return Array.getLength(data);
        }
        if (data instanceof Collection<?>) {
            return ((Collection<?>) data).size();
        }
        if (data instanceof Map<?, ?>) {
            return ((Map<?, ?>) data).size();
        }
        return 1;
    }

    public static <T> RestResponse<T> newResponseFromResult(Result<T> result) {
        if (result.isSuccess()) {
            return newSuccessResponse(result.getDescription(), result.getData(), result.getExtra());
        } else {
            return newFailResponse(result.getErrorCode(), result.getData(), result.getDescription());
        }
    }
}
