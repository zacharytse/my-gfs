package remote;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.Chunk;
import utils.Constant;

public class ChunkServerRpcImpl extends UnicastRemoteObject implements IChunkServerRpc {

    public ChunkServerRpcImpl() throws RemoteException {
        super();
    }

    @Override
    public Response heartBeat(Request request) throws RemoteException {
        Response response = Response.builder().build();
        response.getBody().put(Constant.HEART_BEAT, null);
        System.out.println("收到master心跳信息...");
        return response;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response upload(Request request) throws RemoteException {
        Map<String, Object> body = request.getBody();
        if (!body.containsKey(Constant.FILE_NAME) ||
                !body.containsKey(Constant.CHUNK_LIST)) {
            // 字段包含不完整
            return Response.builder().code(Constant.FAIL).build();
        }
        String filename = (String) body.get(Constant.FILE_NAME);
        List<Chunk> chunkList = (List<Chunk>) body.get(Constant.CHUNK_LIST);
        boolean res = write(filename, chunkList);
        if (res) {
            System.out.println("filename:" + filename + "上传成功");
            return Response.builder().build();
        } else {
            System.out.println("filename:" + filename + "上传失败");
            return Response.builder().code(Constant.FAIL).build();
        }
    }

    private boolean write(String filename, List<Chunk> chunkList) {
        FileOutputStream writer = null;
        try {
            File file = getRealFile(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileOutputStream(file);
            for (Chunk chunk : chunkList) {
                writer.write(chunk.getBody());
            }
            writer.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private File getRealFile(String filename) {
        return new File(Constant.PREFIX, filename);
    }

    @Override
    public Response donwload(Request request) throws RemoteException {
        Map<String, Object> body = request.getBody();
        if (!body.containsKey(Constant.FILE_NAME)) {
            System.out.println("请求中缺少文件名参数");
            return Response.builder().code(Constant.FAIL).build();
        }
        String filename = (String) body.get(Constant.FILE_NAME);
        List<Chunk> list = read(filename);
        if (list == null) {
            return Response.builder().code(Constant.FAIL).build();
        }
        Response rsp = Response.builder().build();
        rsp.getBody().put(Constant.CHUNK_LIST, list);
        return rsp;
    }

    private List<Chunk> read(String filename) {
        List<Chunk> chunkList = new LinkedList<Chunk>();
        File file = getRealFile(filename);
        if (!file.exists()) {
            return null;
        }
        InputStream inputStream = null;
        try {
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

}
