package com.xcq.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.xcq.dto.Chunk;

public class FileOperation {
    /**
     * 获取文件的大小
     * 
     * @param path
     * @return
     */
    public static Long getFileSize(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            System.out.println("非法的文件路径");
            return 0l;
        }
    }

    /**
     * 判断当前路径是否是合法的master的路径
     * 
     * @param path
     * @return
     */
    public static boolean checkIsServerPath(String path) {
        String regex = Constant.PREFIX + "/.+";
        return Pattern.matches(regex, path);

    }

    public static String getServerPath(String path) {
        return Constant.PREFIX + "/" + path;
    }

    public static void main(String[] args) {
        System.out.println(checkIsServerPath("/root/gfs-data/test"));
    }

    public static List<Chunk> readFile(String filename) {
        List<Chunk> chunkList = new ArrayList<Chunk>();
        InputStream inputStream = null;
        int idx = 0;
        try {
            File file = new File(filename);
            inputStream = new FileInputStream(file);
            byte[] content = new byte[Constant.MAX_CHUNK_SIZE];
            while (inputStream.read(content) != -1) {
                byte[] bytes = Arrays.copyOf(content, content.length);
                Chunk chunk = Chunk.builder().body(bytes)
                        .chunkId(filename + "-" + String.valueOf(idx++)).build();
                chunkList.add(chunk);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chunkList;
    }

    public static boolean writeFile(String filename, List<Chunk> chunkList) {
        File file = new File(filename);
        FileOutputStream out = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            for (Chunk chunk : chunkList) {
                out.write(chunk.getBody());
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        System.out.println("写入文件" + filename + "成功");
        return true;
    }
}
