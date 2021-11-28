package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import main.Master;
import utils.Constant;
import utils.SingletonBase;

public class MasterRpcImpl extends UnicastRemoteObject implements IMasterRpc {

    public MasterRpcImpl() throws RemoteException {
        super();
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

}
