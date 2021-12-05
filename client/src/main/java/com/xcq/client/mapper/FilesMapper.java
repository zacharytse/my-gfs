package com.xcq.client.mapper;

import com.xcq.client.model.dto.GetFileDto;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LEON
 * @since 2020-06-09
 */
public interface FilesMapper {
    /**
     * 获取文件列表
     *
     * @return
     */
    List<GetFileDto> selectFileList();

    /**
     * 判断文件是否已存在
     *
     * @param fileName
     * @return
     */
    boolean fileIsExist(String fileName);
}
