package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChunkServerRpc extends Remote{
    /**
     * 心跳
     * @param serverName
     * @return
     */
    Response heartBeat(Request request) throws RemoteException;
}
