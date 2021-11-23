package util;

public class RemoteUtil {

    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    /**
     * 基于雪花算法产生reqID
     * 雪花产生的id+server_ip+seq
     * @return
     */
    public static String generateRequestID(String serverName,long seq) {
        long id = idWorker.nextId();
        return Long.toString(id) + serverName + Long.toString(seq);
    }
}
