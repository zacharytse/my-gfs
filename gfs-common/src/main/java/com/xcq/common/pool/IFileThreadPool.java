package com.xcq.common.pool;

import com.xcq.common.dto.Chunk;

import java.util.List;

public interface IFileThreadPool {
    List<Chunk> read(String path);

    boolean write(List<Chunk> chunkList, String path, long offset);
}
