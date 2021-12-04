package com.xcq.balance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.xcq.error.IMachineErrorHandler;
import com.xcq.utils.Constant;

/**
 * 负载均衡模块 存储的server格式为ip:port
 */
public class LoadBalance implements IMachineErrorHandler {
    // TODO random selection lru
    private List<String> serverList;
    private Set<String> serverSet;
    private ReadWriteLock lock;

    public LoadBalance(Set<String> serverSet) {
        this.serverSet = serverSet;
        this.serverList = new ArrayList<String>(serverSet);
        lock = new ReentrantReadWriteLock();
    }

    public String selection() {
        lock.readLock().lock();
        if (serverList.size() == 0) {
            return "";
        }
        Random random = new Random();
        int randomIdx = random.nextInt(serverList.size());
        String server = serverList.get(randomIdx);
        lock.readLock().unlock();
        return server;
    }

    /**
     * master需要注册addServer和removeServer
     * 
     * @param server
     */
    public void addServer(String server) {
        lock.writeLock().lock();
        System.out.println("添加机器" + server);
        serverSet.add(server);
        serverList.add(server);
        lock.writeLock().unlock();
    }

    public void removeServer(String server) {
        lock.writeLock().lock();
        System.out.println("移除机器" + server);
        serverList.remove(server);
        serverSet.remove(server);
        lock.writeLock().unlock();
    }

    @Override
    public void handleError(Map<String, Object> content) {
        String server = (String) content.get(Constant.DOWN_MACHINE);
        removeServer(server);
    }

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("127.0.1.1:9000");
        set.add("127.0.1.1:9001");
        set.add("127.0.1.1:9002");
        LoadBalance loadBalance = new LoadBalance(set);
        int[] count = new int[3];
        for (int i = 0; i < 10000; ++i) {
            String server = loadBalance.selection();
            if (server.contains("9000")) {
                count[0]++;
            }
            if (server.contains("9001")) {
                count[1]++;
            }
            if (server.contains("9002")) {
                count[2]++;
            }
        }
        for (int i = 0; i < 3; ++i) {
            System.out.println(count[i]);
        }
    }
}
