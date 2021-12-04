package com.xcq.common.rpc;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * rpc发送的消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Request implements Serializable {
    private static final long serialVersionUID = -4753666713171127424L;

    /**
     * 请求的id
     */
    private String reqId;
    private Long seq;
    private String ip;
    private Integer port;
    private String targetServer;
    private int targetPort;
    /**
     * 需要调用的服务名称
     */
    private String className;
    /**
     * 指定需要调用方法的名称
     */
    private String methodName;

    /**
     * 执行方法所需的参数
     */
    private Object[] params;
    /**
     * 消息体
     */
    @Builder.Default
    private Map<String, Object> body = new HashMap<String, Object>();
}
