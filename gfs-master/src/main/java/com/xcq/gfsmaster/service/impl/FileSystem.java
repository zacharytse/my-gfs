package com.xcq.gfsmaster.service.impl;

import com.xcq.gfsmaster.service.IFileSystem;
import dto.ChunkInfo;
import entity.NameNode;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.xml.soap.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件系统的实现 后面需要实现持久化
 * 采用树形结构
 * 根节点为/
 */
@Component
public class FileSystem implements IFileSystem {

    private Logger logger = LogManager.getLogger(FileSystem.class);


    // 树的根节点信息
    private NameNode root;
    // 方便进行索引
    private Map<String, NameNode> dir;

    public FileSystem() {
        root = NameNode.builder().filename("/").build();
        dir = new HashMap<>();
        dir.put(root.getFilename(), root);
    }

    private boolean addNameNode(String filename, NameNode node) {
        String[] paths = parsePath(filename);
        if (paths == null) {
            return false;
        }
        if (paths.length == 1) {
            logger.info("You are trying add root!");
            return false;
        }
        NameNode parent = root;
        boolean exist = true;
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        for (String path : paths) {
            if (path.equals("/")) {
                continue;
            }
            sb.append(path);
            if (dir.containsKey(sb.toString())) {
                // 已经包含该路径
                parent = dir.get(sb.toString());
            } else {
                // 添加新的路径
                node.setParent(parent);
                // 不会添加根路径，所以parent不会为空
                parent.addChildren(node);
                dir.put(sb.toString(), node);
                parent = node;
                logger.info("add new file: " + sb.toString());
                exist = false;
            }
            sb.append("/");
        }
        if (exist) {
            logger.warn("file: " + filename + " has existed!");
            return false;
        }
        return true;
    }

    @Override
    public boolean addNameNode(String filename) {
        NameNode nameNode = NameNode.builder().filename(filename).build();
        return addNameNode(filename, nameNode);
    }

    /**
     * 对filename的路径信息进行解析
     * 正确的路径:
     * /xxx/xx/xx
     * 这里也可以约定一些文件名称规则（未完成)
     *
     * @param filename
     * @return
     */
    private String[] parsePath(String filename) {
        if (filename == null || filename.length() == 0) {
            logger.warn("path is null");
            return null;
        }
        if (filename.charAt(0) != '/') {
            logger.warn("path start character is not root /");
            return null;
        }
        String[] paths = filename.split("/");
        if (paths == null) {
            logger.error("file path is invalid,please check your path!");
        }
        paths[0] = "/";
        return paths;
    }

    /**
     * 执行delete操作时需要更新上层chunkinfo的信息
     *
     * @param filename
     */
    @Override
    public boolean deleteNameNode(String filename) {
        System.out.println(root.getChunkInfo().getSize());
        String[] paths = parsePath(filename);
        if (paths == null) {
            return false;
        }
        NameNode cur = root;
        NameNode pre = null;
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        for (String path : paths) {
            if (path.equals("/")) {
                continue;
            }
            sb.append(path);
            if (!dir.containsKey(sb.toString())) {
                logger.info("file: " + filename + " is not exist,delete fail!");
                return false;
            } else {
                pre = cur;
                cur = dir.get(sb.toString());
            }
            sb.append("/");
        }
        sb.setLength(sb.length() - 1);
        if (pre != null) {
            // 文件大小要去掉删除节点的文件大小
            pre.removeChildren(cur);
            logger.info("file: " + filename + " delete success");
            dir.remove(sb.toString());
            return true;
        } else {
            // 请求删除根节点，非法操作应该拒绝
            logger.warn("You are requesting delete root.This is an invalid operation!");
            return false;
        }
    }

    @Override
    public NameNode getNameNode(String filename) {
        String[] paths = parsePath(filename);
        if (paths == null) {
            return null;
        }
        NameNode cur = null;
        for (String path : paths) {
            if (dir.containsKey(path)) {
                cur = dir.get(path);
            } else {
                logger.info("file: " + filename + " is not exist");
                return null;
            }
        }
        return cur;
    }

    /**
     * 根据chunkinfo的信息重新更新node,不允许操作根目录
     *
     * @param preFilename
     * @param chunkInfo
     */
    @Override
    public boolean updateNameNode(String preFilename, ChunkInfo chunkInfo) {
        NameNode node = NameNode.builder().chunkInfo(chunkInfo).build();
        return deleteNameNode(preFilename) && addNameNode(chunkInfo.getFilename(),node);
    }

    @Override
    public List<NameNode> nameNodeList(String filename) {
        NameNode cur = getNameNode(filename);
        if(cur == null) {
            return null;
        }
        return cur.getChildren();
    }
}
