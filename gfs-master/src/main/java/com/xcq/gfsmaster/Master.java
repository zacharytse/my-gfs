package com.xcq.gfsmaster;

import com.xcq.gfsmaster.service.impl.FileSystem;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import remote.IChunkRpc;
import remote.Response;

/**
 * master节点
 */
@SpringBootApplication
public class Master implements CommandLineRunner {
    @Autowired
    private FileSystem fileSystem;

    @Reference
    private IChunkRpc chunkRpc;

    public static void main(String[] args) {
        SpringApplication.run(Master.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("running.....");
        Response rsp = chunkRpc.heartBeat(null);
        Object obj = rsp.getMap().get("HeartBeat");
        if(obj != null) {
            System.out.println("发送心跳成功");
        }
    }
}
