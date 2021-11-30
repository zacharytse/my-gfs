package com.xcq;

import java.rmi.RemoteException;
import java.util.Map;

import com.xcq.annotation.Config;
import com.xcq.remote.ChunkServerRpcImpl;
import com.xcq.remote.IChunkServerRpc;
import com.xcq.remote.IMasterRpc;
import com.xcq.remote.IRpc;
import com.xcq.remote.Request;
import com.xcq.remote.Response;
import com.xcq.remote.impl.RmiRpcImpl;
import com.xcq.utils.Constant;
import com.xcq.utils.RemoteUtil;
import com.xcq.utils.SingletonBase;

@Config(path = "/root/workspace/my-gfs/gfs-chunk-server/src/main/res/chunk.properties")
public class ChunkServer extends SingletonBase {
    // 指定当前chunk server使用的端口号
    private int port;
    private IChunkServerRpc chunkServerRpc;
    private String server;
    private int serverPort;
    private IRpc rpc;

    public ChunkServer() throws RemoteException {
        super(ChunkServer.class);
        chunkServerRpc = new ChunkServerRpcImpl();
        server = getConfig("server.ip");
        serverPort = Integer.valueOf(getConfig("server.port"));
        port = Integer.valueOf(getConfig("port"));
        rpc = new RmiRpcImpl();
        registService();
    }

    private void registService() {
        String ip = RemoteUtil.getIpAddress();
        String key = ip + ":" + String.valueOf(port) + ":" + IChunkServerRpc.class.getName().toString();
        RemoteUtil.registService(port, chunkServerRpc, key);

    }

    public static void main(String[] args) {
        try {
            ChunkServer chunkServer = (ChunkServer) SingletonBase.getInstance(ChunkServer.class);
            // ChunkServer chunkServer = new ChunkServer();
            if (!chunkServer.regist()) {
                return;
            }
            System.out.println("regist success");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将当前的chunk服务器注册给master节点保存
     * 
     * @return
     */
    public boolean regist() {
        String ip = RemoteUtil.getIpAddress();
        if (ip.length() == 0) {
            return false;
        }
        Request request = Request.builder().targetServer(server).targetPort(serverPort)
                .className(IMasterRpc.class.getName())
                .methodName(Constant.REGIST_CHUNK_SERVER).build();
        request.setParams(new Object[] { request });
        Map<String, Object> body = request.getBody();
        body.put(Constant.SEVER_NAME, ip);
        body.put(Constant.PORT, port);
        Response rsp = rpc.send(server, request);
        if (rsp != null && rsp.getCode().longValue() == Constant.SUCCESS.longValue()) {
            System.out.println("chunk server:" + ip + ":" + Integer.toString(port) + "注册master成功");
            return true;
        } else {
            System.out.println("chunk server:" + ip + ":" + Integer.toString(port) + "注册master失败");
            return false;
        }
    }
}