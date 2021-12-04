package com.xcq.chunkserver.service;

import com.xcq.common.dto.Chunk;
import com.xcq.common.dto.FileInfo;
import com.xcq.common.pool.IFileThreadPool;
import com.xcq.common.remote.IMasterRpc;
import com.xcq.common.rpc.IChunkServerRpc;
import com.xcq.common.rpc.Request;
import com.xcq.common.rpc.Response;
import com.xcq.common.utils.Constant;
import com.xcq.common.utils.FileUtil;
import com.xcq.common.utils.NetUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@DubboService
@Log4j2
public class ChunkServerRpcImpl implements IChunkServerRpc {

    @Value("${dubbo.protocol.port}")
    private Integer port;
    @DubboReference
    private IMasterRpc masterRpc;
    @Autowired
    private IFileThreadPool fileThreadPool;

    @PostConstruct
    public void regist() {
        Request req = Request.builder().ip(NetUtils.getIp()).port(port).build();
        Response rsp = masterRpc.registChunkServer(req);
        if (rsp != null && rsp.getCode().longValue() == Constant.SUCCESS) {
            log.info(NetUtils.getIp() + ":" + port + "注册成功");
        } else {
            log.error(NetUtils.getIp() + ":" + port + "注册失败");
        }
    }

    @Override
    public Response heartBeat(Request request) {
        Response response = Response.builder().build();
        response.getBody().put(Constant.HEART_BEAT, null);
        response.setIp(NetUtils.getIp());
        response.setPort(port);
        log.info("收到" + request.getIp() + ":" + request.getPort() + "的心跳请求");
        return response;
    }

    /**
     * @param request 包含FileInfo以及文件内容chunkList
     * @return 读取成功
     */

    @Override
    public Response upload(Request request) {
        FileInfo fileInfo = (FileInfo) request.getBody().get(Constant.FILE_INFO);
        @SuppressWarnings("unchecked")
        List<Chunk> chunkList = (List<Chunk>) request.getBody().get(Constant.CHUNK_LIST);
        return upload(chunkList, fileInfo, 0);
    }

    private Response upload(List<Chunk> chunkList, FileInfo fileInfo, long offset) {
        log.info("上传" + fileInfo.getFilename());
        if (fileThreadPool.write(chunkList, fileInfo.getFilename(), 0)) {
            log.info("文件" + fileInfo.getFilename() + "上传成功");
            return Response.builder().ip(NetUtils.getIp()).port(port).code(Constant.SUCCESS).build();
        } else {
            // TODO 需要处理文件上传失败的情况
            log.info("文件" + fileInfo.getFilename() + "上传失败");
            return Response.builder().ip(NetUtils.getIp()).port(port).code(Constant.FAIL).build();
        }
    }

    /**
     * @param request 下载文件，只需要读取到服务器上的文件内容，然后返回给客户端
     * @return 读取成功码以及文件内容
     */
    @Override
    public Response download(Request request) {
        FileInfo fileInfo = (FileInfo) request.getBody().get(Constant.FILE_INFO);
        List<Chunk> chunkList = fileThreadPool.read(fileInfo.getFilename());
        if (chunkList != null) {
            log.info("文件" + fileInfo.getFilename() + "读取成功");
            Response rsp = Response.builder().ip(NetUtils.getIp()).port(port)
                    .code(Constant.SUCCESS).build();
            rsp.getBody().put(Constant.CHUNK_LIST, chunkList);
            return rsp;
        } else {
            // TODO 需要处理文件读取失败的情况
            log.info("文件" + fileInfo.getFilename() + "读取失败");
            Response rsp = Response.builder().ip(NetUtils.getIp()).port(port)
                    .code(Constant.FAIL).build();
            return rsp;
        }
    }

    /**
     * @param request 包含FileInfo以及文件内容chunkList
     * @return 读取成功
     */
    @Override
    public Response append(Request request) {
        FileInfo fileInfo = (FileInfo) request.getBody().get(Constant.FILE_INFO);
        @SuppressWarnings("unchecked")
        List<Chunk> chunkList = (List<Chunk>) request.getBody().get(Constant.CHUNK_LIST);
        log.info("追加文件" + fileInfo.getFilename());
        return upload(chunkList, fileInfo, FileUtil.getFileLength(fileInfo.getFilename()));
    }
}
