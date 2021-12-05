package com.xcq.client.service.impl;

import com.xcq.client.service.IOperationService;
import com.xcq.common.dto.Chunk;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
public class OperationServiceImpl implements IOperationService {
//    @DubboReference
//    private IMasterRpc masterRpc;

    @Override
    public void upload(String filename, HttpServletResponse response) {

    }

    @Override
    public void download(String path, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(path);
            response.reset();
            response.setContentType("application/octet-stream");
            String filename = new File(path).getName();
            response.addHeader("Content-Disposition", "attachment; filename=" +
                    URLEncoder.encode(filename, "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    @Override
    public void append(String filename, List<Chunk> chunkList) {

    }
}
