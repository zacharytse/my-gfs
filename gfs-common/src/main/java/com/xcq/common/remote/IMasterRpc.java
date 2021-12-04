package com.xcq.common.remote;

import com.xcq.common.rpc.Request;
import com.xcq.common.rpc.Response;


/**
 * 远程传输需要实现的接口
 */
public interface IMasterRpc {

    /**
     * 将chunkServer注册到master节点上
     */
    Response registChunkServer(Request request);

    /**
     * client向master发起上传命令
     */
    Response upload(Request request);

    /**
     * client向master发起下载命令
     */
    Response download(Request request);
}
