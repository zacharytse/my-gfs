package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChunkServerRpc extends UnicastRemoteObject implements IChunkRpc {

    public ChunkServerRpc() throws RemoteException {
        super();
    }

    @Override
    public Response heartBeat(Request request) throws RemoteException {
        Response response = Response.builder().build();
        response.getMap().put("heartbeat", null);
        return response;
    }

}
