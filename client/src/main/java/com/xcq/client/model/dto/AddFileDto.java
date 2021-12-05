package com.xcq.client.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取文件信息DTO
 *
 * @author wgb
 * @date 2020/6/9 10:13
 */
@Data
@Accessors(chain = true)
public class AddFileDto {
    /**
     * 文件ID
     */
    private String id;
    /**
     * 目标对象ID
     */
    private String targetId;
    /**
     * 文件位置
     */
    private String filePath;
    /**
     * 原始文件名
     */
    private String fileName;
    /**
     * 文件后缀
     */
    private String suffix;
}
