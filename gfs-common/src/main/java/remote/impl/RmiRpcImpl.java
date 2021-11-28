package remote.impl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IRpc;
import remote.Request;
import remote.Response;
import utils.Reflect;

public class RmiRpcImpl implements IRpc {

    /**
     * 执行rmi的rpc远程调用命令
     */
    @Override
    public Response send(String serverName, Request request) {
        try {
            Registry registry = LocateRegistry.getRegistry(serverName);
            Object obj = registry.lookup(request.getClassName());
            return (Response) Reflect.invoke(obj, request.getMethodName(), request.getParams());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
