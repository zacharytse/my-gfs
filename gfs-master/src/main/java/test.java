import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IChunkServerRpc;
import remote.Response;

public class test {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost");
            IChunkServerRpc rpc = (IChunkServerRpc) registry.lookup("test");
            Response rsp = rpc.heartBeat(null);
            System.out.println(rsp.getMap().containsKey("heartbeat"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
