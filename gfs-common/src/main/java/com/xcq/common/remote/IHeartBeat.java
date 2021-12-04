package com.xcq.common.remote;

import com.xcq.common.dto.ServerInfo;

public interface IHeartBeat {
    void addServer(ServerInfo serverInfo);

    void deleteServer(ServerInfo serverInfo);
}
