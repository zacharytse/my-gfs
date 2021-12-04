package com.xcq.common.remote.impl;

import com.xcq.common.dto.ServerInfo;
import com.xcq.common.remote.IServiceMonitor;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceMonitor implements IServiceMonitor {

    @Autowired
    private CuratorFramework client;

    @Override
    public void addListener(String path) {

    }

    @Override
    public void errorHandle(ServerInfo serverInfo) {

    }
}
