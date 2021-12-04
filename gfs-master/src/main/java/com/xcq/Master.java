package com.xcq;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.xcq.annotation.Config;
import com.xcq.balance.LoadBalance;
import com.xcq.dto.ChunkInfo;
import com.xcq.error.IMachineErrorHandler;
import com.xcq.file.FileSystem;
import com.xcq.remote.IMasterRpc;
import com.xcq.remote.IRpc;
import com.xcq.remote.MasterRpcImpl;
import com.xcq.remote.impl.RmiRpcImpl;
import com.xcq.utils.RemoteUtil;
import com.xcq.utils.SingletonBase;

/**
 * 定义master节点
 */
@Config(path="/root/workspace/my-gfs/gfs-master/src/main/res/master.properties")
public class Master extends SingletonBase {

    /**
     * key:ip-port value chunkServer-rpc
     */
    private Map<String, List<ChunkInfo>> chunkServer;
    private Set<String> serverList;
    private IMasterRpc masterRpc;
    private FileSystem fileSystem;
    private int port;
    private HeartBeat heartBeat;
    private IRpc rpc;
    private LoadBalance loadBalance;

    public Master() throws RemoteException {
        super(Master.class);
        chunkServer = new ConcurrentHashMap<>();
        fileSystem = new FileSystem();
        serverList = new HashSet<>();
        loadBalance = new LoadBalance(serverList);
        masterRpc = new MasterRpcImpl(fileSystem, chunkServer, serverList, loadBalance);
        port = Integer.valueOf(getConfig("port"));
        String ip = RemoteUtil.getIpAddress();
        RemoteUtil.registService(port, masterRpc, ip +
                ":" +
                String.valueOf(port) + ":" +
                IMasterRpc.class.getName().toString());
        rpc = new RmiRpcImpl();
        initHeartBeat();
    }

    private void initHeartBeat() {
        heartBeat = new HeartBeat(serverList, rpc, (IMachineErrorHandler) loadBalance);
    }

    public void registChunkServer(String serverName) {
        serverList.add(serverName);
        loadBalance.addServer(serverName);
    }

    public static String getChunkServerKey(String serverName, int port) {
        return serverName + ":" + port;
    }

    public static void main(String[] args) {
        try {
            Master master = (Master) SingletonBase.getInstance(Master.class);
            System.out.println("ready...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
