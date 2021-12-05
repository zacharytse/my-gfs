package com.xcq.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileInfo implements Serializable {
    private static final long serialVersionUID = -2954329536839463630L;

    /**
     * 整个文件的大小,默认只有一个连接的大小即4B
     */
    @Builder.Default
    private Long size = 4l;

    @Builder.Default
    private Date cretateTime = new Date();
    /**
     * 文件名称
     */
    private String filename;

    /**
     * 主节点
     */
    private ServerInfo primaryServer;
    /**
     * 副节点
     */
    private List<ServerInfo> copyServers;
    /**
     * 存储所有chunk的id号
     */
    @Builder.Default
    private List<String> chunks = new LinkedList<>();
}
