package com.xcq.gfsmaster.remote;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import remote.IMasterRpc;
import remote.Request;
import remote.Response;

@Service
@Component
public class MasterRpcImpl implements IMasterRpc {
    /**
     * 向serverName发送心跳信息
     * @param serverName
     */
    @Override
    public Response heartBeat(String serverName) {
        return null;
    }

    @Override
    public Response sendCommand(String serverName, Request request) {
        return null;
    }

    @Override
    public Response sendData(String serverName, Request request) {
        return null;
    }
}
