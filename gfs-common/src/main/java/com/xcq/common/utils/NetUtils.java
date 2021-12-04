package com.xcq.common.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.rpc.RpcContext;

@Log4j2
public class NetUtils {

    public static String getIp() {
        return RpcContext.getContext().getLocalHost();
    }
}
