package com.xcq.client.dao;

import com.xcq.common.dto.FileInfo;

import java.util.List;

public interface IFileMapper {
    List<FileInfo> getFileList(String parent);
}
