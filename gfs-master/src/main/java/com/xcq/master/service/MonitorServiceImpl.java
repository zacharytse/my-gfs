package com.xcq.master.service;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;
import com.xcq.common.monitor.IMonitorService;
import com.xcq.common.utils.Constant;
import com.xcq.common.utils.balance.ILoadBalance;
import com.xcq.common.utils.balance.impl.RandomLoadBalance;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
public class MonitorServiceImpl implements IMonitorService {
    private final List<ServerInfo> serverInfoList = new ArrayList<>();
    private final Map<String, ServerInfo> serverMap = new HashMap<>();
    private final ILoadBalance loadBalance = new RandomLoadBalance();

    /**
     * 添加服务器
     *
     * @param serverInfo
     */
    @Override
    public void addServer(ServerInfo serverInfo) {
        if (serverMap.containsKey(serverInfo.toString())) {
            log.warn("尝试添加重复的服务器...");
            return;
        }
        serverInfoList.add(serverInfo);
        serverMap.put(serverInfo.toString(), serverInfo);
    }

    /**
     * 服务器分配
     *
     * @param fileInfo
     * @return
     */
    @Override
    public ServerInfo assignServer(FileInfo fileInfo) {
        ServerInfo serverInfo = loadBalance.selection(fileInfo, serverInfoList);
        if (serverInfo != null) {
            log.info("服务器" + serverInfo + "分配成功");
            return serverInfo;
        } else {
            log.error("没有合适的服务器分配");
            return null;
        }
    }

    /**
     * 分配副本服务器
     *
     * @param fileInfo
     * @return
     */
    @Override
    public List<ServerInfo> assignCopyServer(FileInfo fileInfo) {
        List<ServerInfo> servers = loadBalance.selectionCopyServers(fileInfo, serverInfoList,
                Constant.COPY_SERVER_COUNT);
        if (servers.size() == 0) {
            log.warn("副本服务器分配失败");
        }
        log.info("副本服务器分配成功");
        return servers;
    }

    @Override
    public void removeServer(ServerInfo serverInfo) {
        if (!serverMap.containsKey(serverInfo.toString())) {
            log.warn("服务器不存在,删除服务器失败");
            return;
        }
        serverMap.remove(serverInfo.toString());
        serverInfoList.remove(serverInfo);
    }
}
