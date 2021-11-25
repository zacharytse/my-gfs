import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.ChunkServerRpc;
import remote.IChunkRpc;

public class ChunkServer {
    public static void main(String[] args) {
        try {
            IChunkRpc chunkRpc = new ChunkServerRpc();
            LocateRegistry.createRegistry(1099);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("test",chunkRpc);
            System.out.println("server ready...");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
