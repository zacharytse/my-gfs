package com.xcq;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.xcq.error.IMachineErrorHandler;
import com.xcq.remote.IChunkServerRpc;
import com.xcq.remote.IRpc;
import com.xcq.remote.Request;
import com.xcq.remote.Response;
import com.xcq.utils.Constant;

/**
 * 发送心跳类
 */
public class HeartBeat {

    private Set<String> serverList;
    private IRpc rpc;
    private Timer timer;
    private IMachineErrorHandler errorHandler;

    public HeartBeat(Set<String> serverList, IRpc rpc, IMachineErrorHandler errorHandler) {
        this.serverList = serverList;
        this.rpc = rpc;
        timer = new Timer(Constant.HEART_BEAT);
        timer.schedule(new TimeTask(), 2000, 3000);
        this.errorHandler = errorHandler;
    }

    public static void main(String[] args) {
        System.out.println("yes");
        for (int i = 0; i < 10; ++i) {
            new Timer("timer - " + i).schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " run ");
                }
            }, 2000, 3000);
        }
    }

    class TimeTask extends TimerTask {

        @Override
        public void run() {
            for (String server : serverList) {
                String[] parts = server.split(":");
                Request req = Request.builder().targetServer(parts[0])
                        .targetPort(Integer.valueOf(parts[1]))
                        .className(IChunkServerRpc.class.getName())
                        .methodName("heartBeat")
                        .build();
                req.setParams(new Object[] { req });
                Response rsp = rpc.send(parts[0], req);
                if (rsp != null && rsp.getCode().longValue() == Constant.SUCCESS.longValue()) {
                    System.out.println("发送心跳:" + server + "成功");
                } else {
                    System.out.println("chunk server:" + server + "下线");
                    Map<String, Object> content = new HashMap<String, Object>();
                    content.put(Constant.DOWN_MACHINE, server);
                    errorHandler.handleError(content);
                }
            }
        }

    }
}
