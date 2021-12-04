package com.xcq.common.utils.balance.impl;

/**
 * 负载均衡模块 存储的server格式为ip:port
 */
public class LoadBalance2 {

//    public String selection(List<ServerInfo> serverInfoList) {
//
//        if (serverList.size() == 0) {
//            return "";
//        }
//        Random random = new Random();
//        int randomIdx = random.nextInt(serverList.size());
//        String server = serverList.get(randomIdx);
//        lock.readLock().unlock();
//        return server;
//    }
//
//    /**
//     * 挑选conunt台服务器
//     * 采用分段策略进行随机抽取
//     */
//    public List<String> selection(int count, boolean needExclude, String exclude) {
//        lock.readLock().lock();
//        List<String> ans = new ArrayList<String>();
//        // 采用副本方便删除需要排除的服务器
//        List<String> tempServerList = new ArrayList<String>(serverList);
//        lock.readLock().unlock();
//        if (needExclude) {
//            // 先把要排除的机器删除
//            tempServerList.remove(exclude);
//        }
//        if (tempServerList.size() < count) {
//            // 不够分配
//            return null;
//        }
//        if (tempServerList.size() == count) {
//            Collections.copy(ans, tempServerList);
//            return ans;
//        }
//        Random random = new Random();
//        List<Integer> idxList = new ArrayList<Integer>();
//        for (int i = 0; i < tempServerList.size(); i++) {
//            idxList.add(i);
//        }
//        while (count > 0) {
//            for (int i = 0; i < idxList.size(); i += 2) {
//                int rand = random.nextInt(2);
//                if (i == idxList.size() - 1) {
//                    rand = 0;
//                }
//                ans.add(tempServerList.get(idxList.get(i + rand)));
//                --count;
//                idxList.set(i + rand, -idxList.get(i + rand));
//                if (count == 0) {
//                    break;
//                }
//            }
//            if (count == 0) {
//                break;
//            }
//            // 删除元素时必须使用迭代器删除，否则会抛java.util.ConcurrentModificationException异常
//            idxList.removeIf(idx -> idx < 0);
//        }
//        return ans;
//    }
//
//    /**
//     * master需要注册addServer和removeServer
//     */
//    public void addServer(String server) {
//        lock.writeLock().lock();
//        System.out.println("添加机器" + server);
//        serverSet.add(server);
//        serverList.add(server);
//        lock.writeLock().unlock();
//    }
//
//    public void removeServer(String server) {
//        lock.writeLock().lock();
//        System.out.println("移除机器" + server);
//        serverList.remove(server);
//        serverSet.remove(server);
//        lock.writeLock().unlock();
//    }
//
//    public void handleError(Map<String, Object> content) {
//        String server = (String) content.get(Constant.DOWN_MACHINE);
//        removeServer(server);
//    }
//
//    public static void main(String[] args) {
//        Set<String> set = new HashSet<>();
//        set.add("127.0.1.1:9000");
//        set.add("127.0.1.1:9001");
//        set.add("127.0.1.1:9002");
//        set.add("127.0.1.1:9003");
//
//        set.add("127.0.1.1:9004");
//        set.add("127.0.1.1:9005");
//        set.add("127.0.1.1:9006");
//
//        LoadBalance2 loadBalance = new LoadBalance2(set);
//        List<String> list = loadBalance.selection(5, true, "127.0.1.1:9000");
//        for (String str : list) {
//            System.out.println(str);
//        }
//    }

}
