package com.xcq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.xcq.annotation.Config;
import com.xcq.dto.Chunk;
import com.xcq.dto.ChunkInfo;
import com.xcq.dto.ServerInfo;
import com.xcq.remote.IChunkServerRpc;
import com.xcq.remote.IMasterRpc;
import com.xcq.remote.IRpc;
import com.xcq.remote.Request;
import com.xcq.remote.Response;
import com.xcq.remote.impl.RmiRpcImpl;
import com.xcq.utils.Constant;
import com.xcq.utils.FileOperation;
import com.xcq.utils.SingletonBase;

@Config(path = "/root/workspace/my-gfs/gfs-client/src/main/res/client.properties")
public class App extends SingletonBase {

    private String server;
    private int serverPort;

    public App() {
        super(App.class);
        server = getConfig("server.ip");
        serverPort = Integer.valueOf(getConfig("server.port"));
    }

    public static void main(String[] args) {
        try {
            App app = (App) SingletonBase.getInstance(App.class);
            app.upload("/root/test", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // app.download("test", "/root/abc");
    }

    private void upload(String origin, String target) {
        IRpc rpc = new RmiRpcImpl();
        // Request request = Request.builder().className("127.0.1.1:9000:" +
        // IChunkServerRpc.class.getName().toString())
        // .methodName("upload").build();
        // request.setParams(new Object[] { request });
        // Map<String, Object> body = request.getBody();
        // body.put(Constant.CHUNK_LIST, chunks);
        // body.put(Constant.FILE_NAME, target);
        // Response response = rpc.send("127.0.1.1", request);
        // if (response != null && response.getCode().longValue() == Constant.SUCCESS) {
        // System.out.println("文件上传成功");
        // } else {
        // System.out.println("文件上传失败");
        // }
        Request request = Request.builder().targetServer(server).targetPort(serverPort)
                .className(IMasterRpc.class.getName())
                .methodName("upload").build();
        request.setParams(new Object[] { request });
        Map<String, Object> body = request.getBody();
        body.put(Constant.FILE_NAME, target);
        ChunkInfo chunkInfo = ChunkInfo.builder().size(FileOperation.getFileSize(origin)).build();
        body.put(Constant.FILE_INFO, chunkInfo);
        Response response = rpc.send("127.0.1.1", request);
        if (response != null && response.getCode().longValue() == Constant.SUCCESS) {
            System.out.println("文件目录上传成功");
            System.out.println("开始上传文件");
            body = response.getBody();
            if (!body.containsKey(Constant.TARGET_CHUNK_SERVER)) {
                System.out.println("缺少目标服务器信息...");
                return;
            }
            ServerInfo serverInfo = (ServerInfo) body.get(Constant.TARGET_CHUNK_SERVER);
            request.setTargetServer(serverInfo.getIp());
            request.setTargetPort(serverInfo.getPort());
            request.setClassName(IChunkServerRpc.class.getName());
            request.setMethodName("upload");
            List<Chunk> chunks = FileOperation.readFile(origin);
            body = request.getBody();
            body.put(Constant.CHUNK_LIST, chunks);
            body.put(Constant.FILE_NAME, target);
            response = rpc.send("127.0.1.1", request);
            if (response != null && response.getCode().longValue() == Constant.SUCCESS) {
                System.out.println("文件上传成功");
            } else {
                System.out.println("文件上传失败");
            }
        } else {
            System.out.println("文件目录上传失败");
        }
    }

    @SuppressWarnings("unchecked")
    private void download(String origin, String target) {
        Request request = Request.builder().className("127.0.1.1:9000:" + IChunkServerRpc.class.getName().toString())
                .methodName("donwload").build();
        request.setParams(new Object[] { request });
        IRpc rpc = new RmiRpcImpl();
        Map<String, Object> body = request.getBody();
        body.put(Constant.FILE_NAME, origin);
        Response response = rpc.send("127.0.1.1", request);
        if (response != null && response.getCode().longValue() == Constant.SUCCESS) {
            body = response.getBody();
            List<Chunk> chunks = (List<Chunk>) body.get(Constant.CHUNK_LIST);
            writeFile(target, chunks);
            System.out.println("文件下载成功");
        } else {
            System.out.println("文件下载失败");
        }
    }

    private List<Chunk> readFile(String filename) {
        List<Chunk> chunkList = new ArrayList<Chunk>();
        InputStream inputStream = null;
        try {
            File file = new File(filename);
            inputStream = new FileInputStream(file);
            byte[] content = new byte[Constant.MAX_CHUNK_SIZE];
            while (inputStream.read(content) != -1) {
                byte[] bytes = Arrays.copyOf(content, content.length);
                Chunk chunk = Chunk.builder().body(bytes).build();
                chunkList.add(chunk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chunkList;
    }

    private void writeFile(String filename, List<Chunk> chunkList) {
        File file = new File(filename);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            for (Chunk chunk : chunkList) {
                out.write(chunk.getBody());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
