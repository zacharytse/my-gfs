package com.xcq.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流工具类
 *
 * @author wgb
 * @date 2020/6/2 10:04
 */
public class InputStreamUtils {
    /**
     * InputStream转 byte数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 1024)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
