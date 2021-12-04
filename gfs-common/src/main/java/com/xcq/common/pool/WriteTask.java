package com.xcq.common.pool;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

@Log4j2
public class WriteTask implements Callable {
    private RandomAccessFile raf;
    private long start;
    private long len;
    private byte[] content;

    public WriteTask(long start,
                     long len,
                     byte[] content,
                     RandomAccessFile raf) {
        this.start = start;
        this.len = len;
        this.content = content;
        this.raf = raf;
    }

    @Override
    public Object call() throws Exception {
        try {
            raf.seek(start);
            raf.write(content, 0, (int) len);
            raf.close();
            log.info("写入成功");
            return true;
        } catch (IOException e) {
            log.error("写入失败");
            log.error(e.getMessage());
        }
        return false;
    }
}
