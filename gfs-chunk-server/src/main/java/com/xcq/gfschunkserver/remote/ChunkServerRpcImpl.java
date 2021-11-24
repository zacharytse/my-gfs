package com.xcq.gfschunkserver.remote;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import remote.IChunkRpc;
import remote.Request;
import remote.Response;

@Service
@Component
public class ChunkServerRpcImpl implements IChunkRpc {
    @Override
    public Response heartBeat(Request request) {
        Response response = Response.builder().build();
        // 设置心跳信息
        response.getMap().put("HeartBeat",null);
        return response;
    }
}
