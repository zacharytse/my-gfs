package com.xcq.gfsmaster.service;

import dto.ChunkInfo;
import entity.NameNode;

import java.util.List;


public interface IFileSystem {
    boolean addNameNode(String filename);
    boolean deleteNameNode(String filename);
    NameNode getNameNode(String filename);
    boolean updateNameNode(String preFilename, ChunkInfo chunkInfo);
    List<NameNode> nameNodeList(String filename);
}
