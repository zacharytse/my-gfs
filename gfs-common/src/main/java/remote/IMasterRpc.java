package remote;

/**
 * 远程传输需要实现的接口
 */
public interface IMasterRpc {

    /**
     * 心跳
     * @param serverName
     * @return
     */
    Response heartBeat(String serverName);

    /**
     * 发送命令
     * @param serverName
     * @return
     */
    Response sendCommand(String serverName,Request request);

    /**
     * 发送数据
     */
    Response sendData(String serverName,Request request);
}
