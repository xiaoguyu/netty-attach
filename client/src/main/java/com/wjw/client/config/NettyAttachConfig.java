package com.wjw.client.config;

import com.wjw.config.PropertiesLoader;

/**
 * @author wjw
 * @description: 配置文件
 * @title: NettyAttachConfig
 * @date 2022/4/2 10:17
 */
public class NettyAttachConfig extends PropertiesLoader {

    public final static String FILENAME = "nettyattach.properties";

    @Override
    public String getFileName() {
        return FILENAME;
    }

    public volatile static NettyAttachConfig instance;

    /**
     * 构造方法，加载properties
     *
     * @return
     * @date 2022/4/2 10:36
     */
    public NettyAttachConfig() {
        super.load();
    }

    /**
     * 获取连接地址
     *
     * @return
     * @date 2022/4/8 14:59
     */
    public static String getHost() {
        return getInstance().getProperty("host", "");
    }

    /**
     * 获取端口
     *
     * @return
     * @date 2022/4/8 14:58
     */
    public static int getPort() {
        String portStr = getInstance().getProperty("port", "8088");
        return Integer.parseInt(portStr);
    }


    /**
     * 单例模式
     *
     * @return
     * @date 2022/4/2 10:37
     */
    public static NettyAttachConfig getInstance() {
        if (instance == null) {
            synchronized (NettyAttachConfig.class) {
                if (instance == null) {
                    instance = new NettyAttachConfig();
                }
            }
        }
        return instance;
    }
}
