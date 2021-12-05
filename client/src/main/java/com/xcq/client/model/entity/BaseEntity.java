package com.xcq.client.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类：用于自动生成数据库表实体的公共字段
 *
 * @author wgb
 * @date 2020/3/26 11:47
 */
@Getter
@Setter
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private String id;
    /**
     * 创建时间，插入数据时自动填充
     */
    private LocalDateTime createdTime;
    /**
     * 修改时间，插入、更新数据时自动填充
     */
    private LocalDateTime modifiedTime;
    /**
     * 删除状态：插入数据时自动填充
     */
    private boolean deleteStatus;

}
