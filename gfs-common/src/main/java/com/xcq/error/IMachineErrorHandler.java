package com.xcq.error;

import java.util.Map;

/**
 * 处理机器宕机
 */
public interface IMachineErrorHandler {
    /**
     * 机器宕机时的处理
     */
    void handleError(Map<String, Object> content);
}
