package com.xcq.common.pool;

import com.xcq.common.dto.Chunk;
import com.xcq.common.utils.Constant;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;

/**
 * 定义线程池的任务
 */
@Log4j2
public class ReadTask implements Callable {
    private final long start;
    private final long end;
    private final RandomAccessFile raf;
    private final Chunk chunk;

    public ReadTask(long start, RandomAccessFile raf, Chunk chunk) {
        this.start = start;
        this.end = start + chunk.getSize() + 1;
        this.raf = raf;
        this.chunk = chunk;
    }

    @Override
    public Object call() throws Exception {
        try {
            raf.seek(start);
            long contentLen = end - start;
            byte[] buff = new byte[Constant.MAX_CHUNK_SIZE];
            int hasRead = 0;
            String result = null;
            //之前SEEK指定了起始位置，这里读入指定字节组长度的内容，read方法返回的是下一个开始读的position
            hasRead = raf.read(buff);
            //如果读取的字节数小于0，则退出循环！ （到了字节数组的末尾）
            if (hasRead < 0) {
                return null;
            }
            chunk.setBody(buff);
            raf.close();
        } catch (IOException e) {
            log.error("文件读取失败");
            log.error(e.getMessage());
        }
        return chunk;
    }
}
