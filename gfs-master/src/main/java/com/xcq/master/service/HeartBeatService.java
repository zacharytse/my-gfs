package com.xcq.master.service;

import com.xcq.common.dto.ServerInfo;
import com.xcq.common.remote.IHeartBeat;
import com.xcq.common.rpc.IChunkServerRpc;
import com.xcq.common.rpc.Request;
import com.xcq.common.rpc.Response;
import com.xcq.common.utils.Constant;
import com.xcq.common.utils.NetUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.router.address.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 发送心跳类
 */
@Service
@Log4j2
public class HeartBeatService implements IHeartBeat {

    private List<ServerInfo> serverList;
    @DubboReference(parameters = {"router", "address"})
    private IChunkServerRpc chunkServerRpc;
    @Value("${dubbo.protocol.port}")
    private int port;

    @PostConstruct
    public void init() {
        Timer timer = new Timer(Constant.HEART_BEAT);
        timer.schedule(new TimeTask(), 2000, 3000);
        serverList = new ArrayList<>();
    }

    @Override
    public void addServer(ServerInfo serverInfo) {
        serverList.add(serverInfo);
    }

    @Override
    public void deleteServer(ServerInfo serverInfo) {
        serverList.remove(serverInfo);
    }

    class TimeTask extends TimerTask {

        @Override
        public void run() {
            for (ServerInfo server : serverList) {
                Request req = Request.builder().ip(NetUtils.getIp()).port(port).build();
                Address address = new Address(server.getIp(), server.getPort());
                RpcContext.getContext().setObjectAttachment("address", address);
                try {
                    Response rsp = chunkServerRpc.heartBeat(req);
                    if (rsp != null && rsp.getCode().longValue() == Constant.SUCCESS.longValue()) {
                        log.info(req.getIp() + ":" + req.getPort() + "在线");
                    }
                } catch (RpcException e) {
                    log.error("maybe this server" + server.getIp() + ":" + server.getPort() +
                            " not provide,it need time");
                }

            }
        }

    }
}
