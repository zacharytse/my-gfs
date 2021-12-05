package com.xcq.client.service;

import com.xcq.common.dto.Chunk;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IOperationService {
    void upload(String filename, HttpServletResponse response);

    void download(String filename, HttpServletResponse response);

    void append(String filename, List<Chunk> chunkList);
}
