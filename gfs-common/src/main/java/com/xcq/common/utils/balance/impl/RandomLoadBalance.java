package com.xcq.common.utils.balance.impl;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;
import com.xcq.common.utils.balance.ILoadBalance;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 负载均衡模块 存储的server格式为ip:port
 */
@Log4j2
public class RandomLoadBalance implements ILoadBalance {

    @Override
    public ServerInfo selection(FileInfo fileInfo, List<ServerInfo> serverInfoList) {
        if (serverInfoList.size() == 0) {
            return null;
        }
        Random random = new Random();
        int randomIdx = random.nextInt(serverInfoList.size());
        return serverInfoList.get(randomIdx);
    }

    @Override
    public List<ServerInfo> selectionCopyServers(FileInfo fileInfo, List<ServerInfo> serverInfoList,
                                                 int count) {
        List<ServerInfo> ans = new ArrayList<>();
        // 采用副本方便删除需要排除的服务器
        List<ServerInfo> tempServerList = new ArrayList<>(serverInfoList);
        if (fileInfo.getPrimaryServer() == null) {
            log.error("请先分配主服务器");
            return ans;
        }
        // 删除主服务器
        tempServerList.remove(fileInfo.getPrimaryServer());
        if (tempServerList.size() < count) {
            // 不够分配
            return ans;
        }
        if (tempServerList.size() == count) {
            Collections.copy(ans, tempServerList);
            return ans;
        }
        Random random = new Random();
        List<Integer> idxList = new ArrayList<>();
        for (int i = 0; i < tempServerList.size(); i++) {
            idxList.add(i);
        }
        while (count > 0) {
            for (int i = 0; i < idxList.size(); i += 2) {
                int rand = random.nextInt(2);
                if (i == idxList.size() - 1) {
                    rand = 0;
                }
                ans.add(tempServerList.get(idxList.get(i + rand)));
                --count;
                idxList.set(i + rand, -idxList.get(i + rand));
                if (count == 0) {
                    break;
                }
            }
            if (count == 0) {
                break;
            }
            // 删除元素时必须使用迭代器删除，否则会抛java.util.ConcurrentModificationException异常
            idxList.removeIf(idx -> idx < 0);
        }
        return ans;
    }
}
