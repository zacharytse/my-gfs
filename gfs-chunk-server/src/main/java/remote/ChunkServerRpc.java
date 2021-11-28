package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import utils.Constant;

public class ChunkServerRpc extends UnicastRemoteObject implements IChunkServerRpc {

    public ChunkServerRpc() throws RemoteException {
        super();
    }

    @Override
    public Response heartBeat(Request request) throws RemoteException {
        Response response = Response.builder().build();
        response.getMap().put(Constant.HEART_BEAT, null);
        System.out.println("收到master心跳信息...");
        return response;
    }

}
