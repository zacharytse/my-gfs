package com.xcq.file;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import dto.ChunkInfo;
import entity.NameNode;
import utils.Constant;

/**
 * 管理master的文件系统
 */
public class FileSystem {
    // 文件系统的起始节点
    private NameNode root;
    private static final String[] rootPath = Constant.PREFIX.split("/");
    private ReadWriteLock lock;
    public FileSystem() {
        root = NameNode.builder().filename(Constant.PREFIX).build();
        lock = new ReentrantReadWriteLock();
    }

    /**
     * 为文件系统增加节点
     * 
     * @param filename
     * @return
     */
    public boolean addNode(String filename, ChunkInfo chunkInfo) {
        lock.writeLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            System.out.println("路径不合法，添加节点失败");
            return false;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                if (idx == parts.length - 1) {
                    nameNode = nameNode.addChildren(parts[idx], chunkInfo);
                    lock.writeLock().unlock();
                    System.out.println("添加节点" + filename + "成功");
                    return true;
                } else {
                    nameNode = nameNode.getChild(parts[idx]);
                }
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        System.out.println("添加节点" + filename + "失败");
        return false;
    }

    public boolean deleteNode(String filename) {
        lock.writeLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            System.out.println("路径不合法，删除节点失败");
            return false;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                System.out.println("尝试删除不存在的节点");
                return false;
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        boolean res = nameNode.removeSelf();
        lock.writeLock().unlock();
        if (res) {
            System.out.println("节点删除成功");
            return true;
        } else {
            System.out.println("节点删除失败");
            return false;
        }
    }

    public ChunkInfo getNodeInfo(String filename) {
        lock.readLock().lock();
        String[] parts = parsePath(filename);
        if (parts == null) {
            System.out.println("路径不合法，删除信息失败");
            return null;
        }
        int idx = rootPath.length;
        NameNode nameNode = root;
        while (idx < parts.length) {
            if (!nameNode.containsChild(parts[idx])) {
                System.out.println("尝试获取不存在的节点");
                return null;
            } else {
                nameNode = nameNode.getChild(parts[idx]);
            }
            ++idx;
        }
        lock.readLock().unlock();
        return nameNode.getChunkInfo();
    }

    public boolean updateNode(String filename, ChunkInfo chunkInfo) {
        return deleteNode(filename) && addNode(filename,chunkInfo);
    }

    private String[] parsePath(String path) {
        String[] parts = path.split("/");
        if (parts.length <= rootPath.length) {
            return null;
        }
        return parts;
    }
}
