package remote;

public interface IChunkRpc {
    /**
     * 心跳
     * @param serverName
     * @return
     */
    Response heartBeat(Request request);
}
