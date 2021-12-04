package com.xcq.common.utils.balance;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;

import java.util.List;

public interface ILoadBalance {
    ServerInfo selection(FileInfo fileInfo, List<ServerInfo> serverInfoList);

    List<ServerInfo> selectionCopyServers(FileInfo fileInfo, List<ServerInfo> serverInfoList, int count);
}
