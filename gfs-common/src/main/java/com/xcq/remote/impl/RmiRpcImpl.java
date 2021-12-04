package com.xcq.remote.impl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

import com.xcq.remote.IRpc;
import com.xcq.remote.Request;
import com.xcq.remote.Response;
import com.xcq.utils.Constant;
import com.xcq.utils.FileOperation;
import com.xcq.utils.Reflect;

public class RmiRpcImpl implements IRpc {

    /**
     * 执行rmi的rpc远程调用命令
     */
    @Override
    public Response send(String serverName, Request request) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverName);
            preHandle(request);
            Object obj = registry.lookup(request.getClassName());
            return (Response) Reflect.invoke(obj, request.getMethodName(), request.getParams());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 预处理
     * 目前需要对传过来的文件路径做处理
     * 
     * @param request
     */
    private void preHandle(Request request) {
        String className = request.getClassName();
        String key = request.getTargetServer() + ":" +
                String.valueOf(request.getTargetPort()) + ":" + className;
        request.setClassName(key);
        Map<String, Object> body = request.getBody();
        if (!body.containsKey(Constant.FILE_NAME)) {
            return;
        }
        String filename = (String) body.get(Constant.FILE_NAME);
        if (FileOperation.checkIsServerPath(filename)) {
            return;
        } else {
            body.put(Constant.FILE_NAME, FileOperation.getServerPath(filename));
        }
    }
}
