package com.xcq.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 远程传输需要实现的接口
 */
public interface IMasterRpc extends Remote {

    /**
     * 将chunkServer注册到master节点上
     * 
     * @param request
     * @return
     */
    Response registChunkServer(Request request) throws RemoteException;

    /**
     * client向master发起上传命令
     * @param request
     * @return
     * @throws RemoteException
     */
    Response upload(Request request) throws RemoteException;

    /**
     * client向master发起下载命令
     * @param request
     * @return
     * @throws RemoteException
     */
    Response download(Request request) throws RemoteException;
}
