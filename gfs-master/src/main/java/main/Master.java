package main;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import dto.ChunkInfo;
import entity.NameNode;
import remote.IMasterRpc;
import remote.IRpc;
import remote.MasterRpcImpl;
import remote.impl.RmiRpcImpl;
import utils.RemoteUtil;
import utils.SingletonBase;

/**
 * 定义master节点
 */
public class Master extends SingletonBase {

    /**
     * key:ip-port value chunkServer-rpc
     */
    private Map<String, List<ChunkInfo>> chunkServer;
    private Set<String> serverList;
    private IMasterRpc masterRpc;
    private NameNode root;
    private int port = 1099;
    private HeartBeat heartBeat;
    private IRpc rpc;

    public Master() throws RemoteException {
        chunkServer = new ConcurrentHashMap<>();
        masterRpc = new MasterRpcImpl();
        root = NameNode.builder().build();
        RemoteUtil.registService(port, masterRpc, IMasterRpc.class.getName().toString());
        serverList = new HashSet<>();
        rpc = new RmiRpcImpl();
        initHeartBeat();
    }

    private void initHeartBeat() {
        heartBeat = new HeartBeat(serverList, rpc);
    }

    public void registChunkServer(String serverName) {
        serverList.add(serverName);
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
