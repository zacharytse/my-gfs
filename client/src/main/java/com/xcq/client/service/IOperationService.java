package com.xcq.client.service;

import com.xcq.common.dto.Chunk;

import java.util.List;

public interface IOperationService {
    void upload(String filename);

    void download(String filename);

    void append(String filename, List<Chunk> chunkList);
}
