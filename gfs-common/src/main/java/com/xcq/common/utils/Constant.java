package com.xcq.common.utils;

/**
 * 定义一些常量
 */
public class Constant {

    public static final String PORT = "port";

    public static final String HEART_BEAT = "heart-beat";

    public static final String SERVER_INFO = "server-info";

    public static final Integer COPY_SERVER_COUNT = 2;

    public static final String SEVER_NAME = "server-name";

    public static final String CHUNK_SERVER_RPC = "chunk-server-rpc";

    public static final Long SUCCESS = 200L;

    public static final Long FAIL = 500L;

    public static final String REGIST_CHUNK_SERVER = "registChunkServer";

    // 64MB
    public static final Integer MAX_CHUNK_SIZE = 64 * 1024 * 1024;

    public static final String FILE_NAME = "file-name";

    public static final String CHUNK_LIST = "chunk-list";

    public static final String PREFIX = "/root/gfs-data";

    public static final String FILE_INFO = "file-info";

    public static final String TARGET_CHUNK_SERVER = "target-chunk-server";

    public static final String DOWN_MACHINE = "down-machine";

    /**
     * 线程池参数
     */
    public static final Integer CORE_POOL_SIZE = 5; // 定义为cpu核心数加1
    public static final Integer MAX_POOL_SIZE = 8; // 定义为cpu核心数两倍
    public static final Integer LIVE_TIME = 200;
    public static final Integer POOL_CAPACITY = 5;

}
