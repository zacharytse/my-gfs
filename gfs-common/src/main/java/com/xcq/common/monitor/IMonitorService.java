package com.xcq.common.monitor;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;

import java.util.List;

public interface IMonitorService {
    void addServer(ServerInfo serverInfo);

    ServerInfo assignServer(FileInfo fileInfo);

    List<ServerInfo> assignCopyServer(FileInfo fileInfo);

    void removeServer(ServerInfo serverInfo);
}
