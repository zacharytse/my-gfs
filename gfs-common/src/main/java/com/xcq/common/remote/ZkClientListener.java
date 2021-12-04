package com.xcq.common.remote;

import lombok.extern.log4j.Log4j2;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;

@Log4j2
public class ZkClientListener {

    /**
     * 子节点的监听
     */
    static PathChildrenCacheListener plis = new PathChildrenCacheListener() {
        public void childEvent(CuratorFramework client,
                               PathChildrenCacheEvent event) throws Exception {
            switch (event.getType()) {
                case CHILD_ADDED: {
                    log.info("Node added: "
                            + ZKPaths.getNodeFromPath(event.getData()
                            .getPath()));
                    break;
                }
                case CHILD_UPDATED: {
                    log.info("Node changed: "
                            + ZKPaths.getNodeFromPath(event.getData()
                            .getPath()));
                    break;
                }
                case CHILD_REMOVED: {
                    log.info("Node removed: "
                            + ZKPaths.getNodeFromPath(event.getData()
                            .getPath()));
                    break;
                }
            }

        }
    };
    /**
     * 所有子节点的监听
     */
    static TreeCacheListener treeCacheListener = new TreeCacheListener() {
        public void childEvent(CuratorFramework client, TreeCacheEvent event)
                throws Exception {
            // TODO Auto-generated method stub
            switch (event.getType()) {
                case NODE_ADDED:
                    log.info("TreeNode added: " + event.getData()
                            .getPath() + " , data: " + new String(event.getData().getData()));
                    break;
                case NODE_UPDATED:
                    log.info("TreeNode updated: " + event.getData()
                            .getPath() + " , data: " + new String(event.getData().getData()));
                    break;
                case NODE_REMOVED:
                    log.info("TreeNode removed: " + event.getData()
                            .getPath());
                    break;
                default:
                    break;
            }
        }
    };
    private static CuratorFramework client = null;
    private NodeCache nodeCache;
    private PathChildrenCache pathChildrenCache;
    private TreeCache treeCache;
    private String zookeeperServer;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private int baseSleepTimeMs;
    private int maxRetries;
    private String authstr;

    // 对path进行监听配置
    public static void watcherPath(String path)
            throws Exception {
        //子节点的监听
        PathChildrenCache cache = new PathChildrenCache(client, path, false);
        cache.start();
//       注册监听
        cache.getListenable().addListener(plis);
        //对path路径下所有孩子节点的监听
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.start();
        treeCache.getListenable().addListener(treeCacheListener);

    }

    public String getAuthstr() {
        return authstr;
    }

    public void setAuthstr(String authstr) {
        this.authstr = authstr;
    }

    public String getZookeeperServer() {
        return zookeeperServer;
    }

    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void init() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        client = CuratorFrameworkFactory.builder().connectString(zookeeperServer).retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).build();
        client.start();
        try {
            watcherPath("/dubbo");
        } catch (Exception e) {
            log.error("zk listener初始化失败------------");
            log.error(e.getMessage());
        }

    }

    public void stop() {
        if (client != null) CloseableUtils.closeQuietly(client);
        if (pathChildrenCache != null) CloseableUtils.closeQuietly(pathChildrenCache);
        if (nodeCache != null) CloseableUtils.closeQuietly(nodeCache);
        if (treeCache != null) CloseableUtils.closeQuietly(treeCache);
    }
}  