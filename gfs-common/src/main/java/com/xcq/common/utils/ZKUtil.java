package com.xcq.common.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

@Log4j2
public class ZKUtil {
    public static List<String> getChildren(CuratorFramework client, boolean watch, String path) {
        try {
            return watch ? client.getChildren().watched().forPath(path) : client.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("zookeeper获取孩子失败----------------");
            log.error(e.getMessage());
        }
        return null;
    }
}
