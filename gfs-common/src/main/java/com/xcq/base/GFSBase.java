package com.xcq.base;

import com.xcq.annotation.Config;
import com.xcq.config.IConfig;
import com.xcq.config.impl.PropertiesConfigImpl;

@Config(path = "dadsa")
public class GFSBase {
    private Config configAnno;
    private IConfig config;
    // 指定包名

    public GFSBase(Class<?> clazz) {
        System.out.println(clazz.getName());
        configAnno = clazz.getAnnotation(Config.class);
        config = new PropertiesConfigImpl();
        config.read(configAnno.path());
    }

    public String getConfig(String key) {
        if (config == null) {
            return "";
        }
        return config.get(key);
    }

    public static void main(String[] args) {
        GFS gfs = new GFS();
    }
}

@Config(path = "gfs-common/src/main/java/res/test.properties")
class GFS extends GFSBase {
    private int port;

    public GFS() {
        super(GFS.class);
        port = Integer.valueOf(getConfig("test.port"));
        System.out.println(port);
    }
}
