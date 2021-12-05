package com.xcq.client.dao.impl;

import com.xcq.client.dao.IFileMapper;
import com.xcq.common.dto.FileInfo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FileMapper implements IFileMapper {
    @Override
    public List<FileInfo> getFileList(String parent) {
        List<FileInfo> list = new ArrayList<>();
        list.add(FileInfo.builder().filename("test").build());
        list.add(FileInfo.builder().filename("abc").build());
        return list;
    }
}
