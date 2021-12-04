package com.xcq.chunkserver.service.file;

import com.xcq.common.dto.Chunk;
import com.xcq.common.pool.IFileThreadPool;
import com.xcq.common.pool.ReadTask;
import com.xcq.common.pool.WriteTask;
import com.xcq.common.utils.Constant;
import com.xcq.common.utils.FileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池
 */
@Log4j2
@Component
public class FileThreadPool implements IFileThreadPool {
    private ThreadPoolExecutor threadPoolExecutor;

    @PostConstruct
    public void init() {
        threadPoolExecutor = new ThreadPoolExecutor(Constant.CORE_POOL_SIZE,
                Constant.MAX_POOL_SIZE,
                Constant.LIVE_TIME,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(Constant.POOL_CAPACITY));
    }

    @PreDestroy
    public void destory() {
        threadPoolExecutor.shutdown();
    }

    @SuppressWarnings("unchecked")
    public List<Chunk> read(String path) {
        long length = FileUtil.getFileLength(path);
        if (length == 0) {
            log.warn("文件大小为0");
            return new ArrayList<>();
        }
        List<FutureTask<Chunk>> taskList = new ArrayList<>();
        int taskCount = (int) (length / Constant.MAX_CHUNK_SIZE + 1);
        RandomAccessFile[] outArr = new RandomAccessFile[taskCount];
        // 最后一次需要读取的文件大小
        long left = length % Constant.MAX_CHUNK_SIZE;
        for (int i = 0; i < taskCount; ++i) {
            try {
                outArr[i] = new RandomAccessFile(path, "rw");
                Chunk chunk = Chunk.builder().build();
                if (i != taskCount - 1) {
                    chunk.setSize(Constant.MAX_CHUNK_SIZE);
                } else {
                    chunk.setSize((int) left);
                }
                FutureTask<Chunk> ft = new FutureTask<Chunk>(new ReadTask((long) i * Constant.MAX_CHUNK_SIZE,
                        outArr[i], chunk));
                taskList.add(ft);
                threadPoolExecutor.submit(ft);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        List<Chunk> ans = new ArrayList<>();
        for (FutureTask<Chunk> ft : taskList) {
            try {
                ans.add(ft.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ans;
    }

    @SuppressWarnings("unchecked")
    public boolean write(List<Chunk> chunkList, String path, long offset) {
        RandomAccessFile[] outArr = new RandomAccessFile[chunkList.size()];
        List<FutureTask<Boolean>> taskList = new ArrayList<>();
        for (int i = 0; i < chunkList.size(); ++i) {
            try {
                outArr[i] = new RandomAccessFile(path, "rw");
                FutureTask<Boolean> ft = new FutureTask<Boolean>(new WriteTask(offset + (long) i * Constant.MAX_CHUNK_SIZE, chunkList.get(i).getSize(),
                        chunkList.get(i).getBody(), outArr[i]));
                taskList.add(ft);
                threadPoolExecutor.submit(ft);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (FutureTask<Boolean> ft : taskList) {
            try {
                if (!ft.get()) {
                    log.error("写入失败");
                    return false;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
