package com.xcq.common.remote;

import com.xcq.common.rpc.Request;
import com.xcq.common.rpc.Response;

public interface IRpc {
    Response send(String serverName, Request request);
}
