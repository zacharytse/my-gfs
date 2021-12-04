package com.xcq;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import com.xcq.dto.ChunkInfo;
import com.xcq.file.FileSystem;
import com.xcq.utils.Constant;

import org.junit.Test;

public class TestFileSystem {
    @Test
    public void testAddFileSystem() throws Exception {
        final FileSystem fileSystem = new FileSystem();
        final ChunkInfo chunkInfo = ChunkInfo.builder().build();
        // assertEquals(true, fileSystem.addNode(getRealPath("test"), chunkInfo));
        // assertEquals(false, fileSystem.addNode(getRealPath("test"), chunkInfo));
        // 验证线程安全
        final AtomicInteger atom = new AtomicInteger(0);
        for (int i = 0; i < 10; ++i) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (fileSystem.addNode(getRealPath("test"), chunkInfo)) {
                        atom.incrementAndGet();
                    }
                }

            }).start();
            ;
        }
        assertEquals(1, atom.get());
    }

    @Test
    public void testDeleteFileSystem() {
        final FileSystem fileSystem = new FileSystem();
        final ChunkInfo chunkInfo = ChunkInfo.builder().build();
        fileSystem.addNode(getRealPath("test"), chunkInfo);
        assertEquals(true,fileSystem.deleteNode(getRealPath("test")));
        assertEquals(false,fileSystem.deleteNode(getRealPath("test")));

    }

    private String getRealPath(String filename) {
        return Constant.PREFIX + "/" + filename;
    }
}
