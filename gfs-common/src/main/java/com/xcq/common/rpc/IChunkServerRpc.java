package com.xcq.common.rpc;

public interface IChunkServerRpc {
    /**
     * 心跳
     *
     * @param request
     * @return
     */
    Response heartBeat(Request request);

    // 文件上传api
    Response upload(Request request);

    // 文件下载api
    Response download(Request request);

    // 对文件执行append操作
    Response append(Request request);
}
