package main;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import remote.IChunkServerRpc;
import remote.IRpc;
import remote.Request;
import remote.Response;
import utils.Constant;

/**
 * 发送心跳类
 */
public class HeartBeat {

    private Set<String> serverList;
    private IRpc rpc;
    private Timer timer;

    public HeartBeat(Set<String> serverList, IRpc rpc) {
        this.serverList = serverList;
        this.rpc = rpc;
        timer = new Timer(Constant.HEART_BEAT);
        timer.schedule(new TimeTask(), 2000, 3000);
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
            System.out.println("发送心跳...");
            for (String server : serverList) {
                String ip = server.split(":")[0];
                Request req = Request.builder().className(server + ":" + IChunkServerRpc.class.getName())
                        .methodName("heartBeat")
                        .build();
                req.setParams(new Object[] { req });
                Response rsp = rpc.send(ip, req);
                if (rsp != null && rsp.getCode().longValue() == Constant.SUCCESS.longValue()) {
                    System.out.println("发送心跳:" + server + "成功");
                } else {
                    System.out.println("chunk server:" + server + "下线");
                }
            }
        }

    }
}
