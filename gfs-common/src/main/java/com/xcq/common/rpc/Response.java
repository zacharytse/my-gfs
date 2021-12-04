package com.xcq.common.rpc;

import com.xcq.common.utils.Constant;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Response implements Serializable {
    private static final long serialVersionUID = -2132291230622843335L;

    /**
     * 对reqId做响应
     */
    private String reqId;
    private Long reqSeq;
    @Builder.Default
    private Long code = Constant.SUCCESS;

    private String ip;
    private Integer port;
    /**
     * 响应id
     */
    private String rspId;
    /**
     * 消息体
     */
    @Builder.Default
    private Map<String, Object> body = new HashMap<>();
}
