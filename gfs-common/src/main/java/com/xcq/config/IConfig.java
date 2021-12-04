package com.xcq.config;

/**
 * 配置文件读取接口
 */
public interface IConfig {
    
    // 读取配置文件
    void read(String configPath);

    // 获取配置项
    String get(String configOption);
}
