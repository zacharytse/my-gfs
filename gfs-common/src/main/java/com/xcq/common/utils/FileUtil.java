package com.xcq.common.utils;

import lombok.extern.log4j.Log4j2;

import java.io.File;

/**
 * 文件读写
 */
@Log4j2
public class FileUtil {

    /**
     * 获取文件大小，单位B
     *
     * @param path
     * @return
     */
    public static Long getFileLength(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            log.error("非法的文件路径");
            return 0L;
        }
    }

    public static void read(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {

        } else {
            log.error("非法的文件路径");
            return;
        }
    }

    public static void main(String[] args) {
        System.out.println(getFileLength("D:\\testmmmmm.txt"));
    }
}
