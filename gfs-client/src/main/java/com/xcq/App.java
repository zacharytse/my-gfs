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

import dto.Chunk;
import dto.ChunkInfo;
import remote.IChunkServerRpc;
import remote.IMasterRpc;
import remote.IRpc;
import remote.Request;
import remote.Response;
import remote.impl.RmiRpcImpl;
import utils.Constant;

public class App {
    public static void main(String[] args) {
        App app = new App();
        app.upload("/root/test", "test");
        //app.download("test", "/root/abc");
    }

    private void upload(String origin, String target) {
        List<Chunk> chunks = readFile(origin);
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
        Request request = Request.builder().className("127.0.1.1:1099:" +
                IMasterRpc.class.getName().toString())
                .methodName("upload").build();
        request.setParams(new Object[] { request });
        Map<String, Object> body = request.getBody();
        body.put(Constant.FILE_NAME, Constant.PREFIX + "/test");
        ChunkInfo chunkInfo = ChunkInfo.builder().size(11l).build();
        body.put(Constant.FILE_INFO, chunkInfo);
        Response response = rpc.send("127.0.1.1", request);
        if (response != null && response.getCode().longValue() == Constant.SUCCESS) {
            System.out.println("文件上传成功");
        } else {
            System.out.println("文件上传失败");
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
