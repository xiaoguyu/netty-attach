package com.wjw.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author wjw
 * @description: properties加载
 * @title: PropertiesLoader
 * @date 2022/4/2 10:30
 */
public abstract class PropertiesLoader {

    protected static Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

    protected static Properties prop;

    public void load() {
        try {
            prop = new Properties();
            prop.load(PropertiesLoader.class.getClassLoader().getResourceAsStream(getFileName()));
        } catch (Exception e) {
            LOGGER.error(String.format("netty-attach：加载配件文件[%s]出错，将使用默认配置!", getFileName()), e);
        }
    }

    public String getProperty(String key, String defaultValue) {
        if (prop == null) {
            return defaultValue;
        }
        return prop.getProperty(key, defaultValue);
    }

    public abstract String getFileName();
}