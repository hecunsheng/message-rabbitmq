package com.qccr.rabbit.util;

import java.util.ResourceBundle;

/**
 * @Author: hecs
 * @Date: 2018/11/16 15:34
 * @Description: 配置文件读取工具类
 */
public class ResourceUtil {
    private static final ResourceBundle resourceBundle;

    static{
        resourceBundle = ResourceBundle.getBundle("rabbitMQ-config");
    }
    public static String getKey(String key){
        return resourceBundle.getString(key);
    }
}
