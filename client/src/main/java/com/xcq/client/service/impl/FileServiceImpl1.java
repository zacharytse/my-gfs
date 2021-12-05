package com.xcq.client.service.impl;

import com.xcq.client.dao.IFileMapper;
import com.xcq.client.service.IFileService1;
import com.xcq.common.dto.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl1 implements IFileService1 {
    @Autowired
    private IFileMapper fileMapper;

    @Override
    public List<FileInfo> getFileList(String parent) {
        return fileMapper.getFileList(parent);
    }
}
