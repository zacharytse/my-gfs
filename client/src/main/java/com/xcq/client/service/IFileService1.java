package com.xcq.client.service;

import com.xcq.common.dto.FileInfo;

import java.util.List;

public interface IFileService1 {
    List<FileInfo> getFileList(String parent);
}
