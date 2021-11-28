package utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoteUtil {

    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    /**
     * 基于雪花算法产生reqID 雪花产生的id+server_ip+seq
     * 
     * @return
     */
    public static String generateRequestID(String serverName, long seq) {
        long id = idWorker.nextId();
        return Long.toString(id) + serverName + Long.toString(seq);
    }

    public static String getIpAddress() {
        try {
            return Inet4Address.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static boolean registService(int port, Remote service) {
        return registService(port, service,service.getClass().getName().toString());
    }

    public static boolean registService(int port, Remote service, String key) {
        try {
            LocateRegistry.createRegistry(port);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind(key, service);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }    

    static void main(String[] args) {
        System.out.println(RemoteUtil.getIpAddress());
    }
}
