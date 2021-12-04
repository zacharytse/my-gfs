package com.xcq.master.service;

import com.xcq.common.dto.FileInfo;
import com.xcq.common.dto.ServerInfo;
import com.xcq.common.entity.NameNode;
import com.xcq.common.monitor.IMonitorService;
import com.xcq.common.utils.Constant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 管理master的文件系统
 */
@Service
@Log4j2
public class FileService {
    private static final String[] rootPath = Constant.PREFIX.split("/");
    // 文件系统的起始节点
    private NameNode root;
    private ReadWriteLock lock;
    @Autowired
    private IMonitorService monitorService;

    @PostConstruct
    public void init() {
        root = NameNode.builder().filename(Constant.PREFIX).build();
        lock = new ReentrantReadWriteLock();
    }

    /**
     * 为文件系统增加节点
     *
     * @return
     */
    public boolean addNode(FileInfo fileInfo) {
        String filename = fileInfo.getFilename();
        lock.writeLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            log.warn("路径不合法，添加节点失败");
            return false;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                if (idx == parts.length - 1) {
                    /*
                      1、路径合法，为其分配一个primary和2个副本
                     */
                    nameNode = nameNode.addChildren(parts[idx], fileInfo);
                    assignServer(fileInfo);
                    fileInfo.setFilename(filename);
                    lock.writeLock().unlock();
                    log.info("添加节点" + filename + "成功");
                    return true;
                } else {
                    nameNode = nameNode.getChild(parts[idx]);
                }
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        log.warn("添加节点" + filename + "失败");
        return false;
    }

    /**
     * 分配副本节点
     */
    private void assignServer(FileInfo fileInfo) {
        ServerInfo primaryServer = monitorService.assignServer(fileInfo);
        if (primaryServer != null) {
            fileInfo.setPrimaryServer(primaryServer);
        }
        List<ServerInfo> serverInfoList = monitorService.assignCopyServer(fileInfo);
        fileInfo.setCopyServers(serverInfoList);
    }

    public boolean deleteNode(String filename) {
        lock.writeLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            log.warn("路径不合法，删除节点失败");
            return false;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                log.warn("尝试删除不存在的节点");
                return false;
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        boolean res = nameNode.removeSelf();
        lock.writeLock().unlock();
        if (res) {
            log.info("节点删除成功");
            return true;
        } else {
            log.warn("节点删除失败");
            return false;
        }
    }

    public FileInfo getNodeInfo(FileInfo fileInfo) {
        String filename = fileInfo.getFilename();
        lock.readLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            log.warn("路径不合法，删除信息失败");
            return null;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                log.warn("尝试获取不存在的节点");
                return null;
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        lock.readLock().unlock();
        return nameNode.getFileInfo();
    }

    public boolean updateNode(String filename, FileInfo fileInfo) {
        return deleteNode(filename) && addNode(fileInfo);
    }

    private String[] parsePath(String path) {
        String[] parts = path.split("/");
        if (parts.length <= rootPath.length) {
            return null;
        }
        return parts;
    }
}
