package com.xcq.common.remote;

import com.xcq.common.dto.ServerInfo;

/**
 * 服务监控
 */
public interface IServiceMonitor {
    void addListener(String path);

    void errorHandle(ServerInfo serverInfo);
}
