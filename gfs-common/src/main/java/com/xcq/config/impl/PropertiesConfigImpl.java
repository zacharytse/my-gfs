package com.xcq.config.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.xcq.config.IConfig;

public class PropertiesConfigImpl implements IConfig {

    private Properties properties;

    @Override
    public void read(String configPath) {
        // TODO Auto-generated method stub
        properties = new Properties();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configPath));
            properties.load(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(String configOption) {
        return (String) properties.get(configOption);
    }

    public static void main(String[] args) {
        IConfig config = new PropertiesConfigImpl();
        config.read("/root/workspace/my-gfs/gfs-common/src/main/java/res/test.properties");
        System.out
                .println(config.get("test.option"));
    }

}
