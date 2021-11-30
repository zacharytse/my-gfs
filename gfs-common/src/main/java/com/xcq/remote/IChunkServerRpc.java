package com.xcq.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChunkServerRpc extends Remote{
    /**
     * 心跳
     * @param serverName
     * @return
     */
    Response heartBeat(Request request) throws RemoteException;
     // 文件上传api
     Response upload(Request request) throws RemoteException;

     // 文件下载api
     Response donwload(Request request) throws RemoteException;
}
