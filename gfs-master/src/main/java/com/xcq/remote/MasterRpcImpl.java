package com.xcq.remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xcq.Master;
import com.xcq.balance.LoadBalance;
import com.xcq.dto.ChunkInfo;
import com.xcq.dto.ServerInfo;
import com.xcq.file.FileSystem;
import com.xcq.utils.Constant;
import com.xcq.utils.SingletonBase;

public class MasterRpcImpl extends UnicastRemoteObject implements IMasterRpc {

    private FileSystem fileSystem;
    private Map<String, List<ChunkInfo>> chunkServer;
    private Set<String> serverList;
    private LoadBalance loadBalance;

    public MasterRpcImpl(FileSystem fileSystem,
            Map<String, List<ChunkInfo>> chunkServer,
            Set<String> serverList, LoadBalance loadBalance) throws RemoteException {
        super();
        this.fileSystem = fileSystem;
        this.chunkServer = chunkServer;
        this.serverList = serverList;
        this.loadBalance = loadBalance;
    }

    /**
     * request内容有ip,port
     * Response中只有success成功码
     */
    @Override
    public Response registChunkServer(Request request) throws RemoteException {
        try {
            Master master = (Master) SingletonBase.getInstance(Master.class);
            String serverName = (String) request.getBody().get(Constant.SEVER_NAME);
            int port = (Integer) request.getBody().get(Constant.PORT);
            master.registChunkServer(Master.getChunkServerKey(serverName, port));
            return Response.builder().code(Constant.SUCCESS).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.builder().code(Constant.FAIL).build();
    }

    @Override
    public Response upload(Request request) throws RemoteException {
        Map<String, Object> body = request.getBody();
        if (!body.containsKey(Constant.FILE_NAME) ||
                !body.containsKey(Constant.FILE_INFO)) {
            System.out.println("缺少文件参数,上传失败");
            return Response.builder().code(Constant.FAIL).build();
        }
        String fileName = (String) body.get(Constant.FILE_NAME);
        ChunkInfo chunkInfo = (ChunkInfo) body.get(Constant.FILE_INFO);
        if (fileSystem.addNode(fileName, chunkInfo)) {
            System.out.println("文件目录上传成功");
            Response response = Response.builder().code(Constant.SUCCESS).build();
            body = response.getBody();
            String selection = loadBalance.selection();
            String[] parts = selection.split(":");
            ServerInfo info = ServerInfo.builder().ip(parts[0]).port(Integer.valueOf(parts[1])).build();
            body.put(Constant.TARGET_CHUNK_SERVER, info);
            return response;
        } else {
            System.out.println("文件目录上传失败");
            return Response.builder().code(Constant.FAIL).build();
        }
    }

    @Override
    public Response download(Request request) throws RemoteException {
        // TODO 下载操作
        return null;
    }

}
