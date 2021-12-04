package com.xcq.master.service;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;
import com.xcq.common.remote.IHeartBeat;
import com.xcq.common.remote.IMasterRpc;
import com.xcq.common.rpc.Request;
import com.xcq.common.rpc.Response;
import com.xcq.common.utils.Constant;
import com.xcq.common.utils.NetUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@DubboService
@Log4j2
public class MasterServiceImpl implements IMasterRpc {

    private Set<ServerInfo> serverList = new HashSet<>();
    @Value("${dubbo.protocol.port}")
    private int port;
    @Autowired
    private FileService fileService;
    @Autowired
    private IHeartBeat heartBeat;

    @Override
    public Response registChunkServer(Request request) {
        ServerInfo serverInfo = ServerInfo.builder().ip(request.getIp()).port(request.getPort()).build();
        log.info(serverInfo.getIp() + ":" + serverInfo.getPort() + "进行了注册");
        serverList.add(serverInfo);
        heartBeat.addServer(serverInfo);
        return Response.builder().ip(NetUtils.getIp()).port(port).code(Constant.SUCCESS).build();
    }

    /**
     * 1、从request中获取到文件信息
     * 2、根据文件信息更新文件节点
     * 3、并指定文件存储的服务器
     * 4、响应分配的文件服务器信息
     *
     * @param request
     * @return
     */
    @Override
    public Response upload(Request request) {
        Map<String, Object> body = request.getBody();
        // 获取文件信息
        FileInfo fileInfo = (FileInfo) body.get(Constant.FILE_INFO);
        // 更新文件节点
        if (!fileService.addNode(fileInfo)) {
            return Response.builder().code(Constant.FAIL).build();
        }
        // 获取到刚刚分配到的服务器信息
        ServerInfo serverInfo = fileInfo.getPrimaryServer();
        // 将服务器信息响应给客户端
        Response rsp = Response.builder().ip(NetUtils.getIp()).port(port).code(Constant.SUCCESS).reqId(request.getReqId()).build();
        rsp.getBody().put(Constant.SERVER_INFO, serverInfo);
        return rsp;
    }

    /**
     * 1、获取要下载的文件信息
     * 2、响应该文件所在的主服务信息
     *
     * @param request
     * @return
     */
    @Override
    public Response download(Request request) {
        Map<String, Object> body = request.getBody();
        FileInfo fileInfo = (FileInfo) body.get(Constant.FILE_INFO);
        fileInfo = fileService.getNodeInfo(fileInfo);
        if (fileInfo == null) {
            log.error("获取文件信息失败");
            return Response.builder().code(Constant.FAIL).build();
        }
        Response rsp = Response.builder().ip(NetUtils.getIp()).port(port).
                code(Constant.SUCCESS).build();
        rsp.getBody().put(Constant.FILE_INFO, fileInfo);
        return rsp;
    }
}
