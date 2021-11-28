import java.rmi.RemoteException;
import java.util.Map;

import remote.ChunkServerRpc;
import remote.IChunkServerRpc;
import remote.IMasterRpc;
import remote.IRpc;
import remote.Request;
import remote.Response;
import remote.impl.RmiRpcImpl;
import utils.Constant;
import utils.RemoteUtil;

public class ChunkServer {
    // 指定当前chunk server使用的端口号
    private int port = 9000;
    private IChunkServerRpc chunkServerRpc;
    private String server = "localhost";
    private IRpc rpc;

    public ChunkServer() throws RemoteException {
        chunkServerRpc = new ChunkServerRpc();
        registService();
        rpc = new RmiRpcImpl();
    }

    private void registService() {
        String ip = RemoteUtil.getIpAddress();
        String key = ip + ":" + String.valueOf(port) + ":" + IChunkServerRpc.class.getName().toString();
        RemoteUtil.registService(port, chunkServerRpc, key);

    }

    public static void main(String[] args) {
        try {
            ChunkServer chunkServer = new ChunkServer();
            if (!chunkServer.regist()) {
                return;
            }
            System.out.println("regist success");

        } catch (RemoteException e) {
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
        Request request = Request.builder().className(IMasterRpc.class.getName())
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