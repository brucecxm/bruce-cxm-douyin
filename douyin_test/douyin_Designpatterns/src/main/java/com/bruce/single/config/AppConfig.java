package com.bruce.single.config;

import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    // 模拟配置属性
    private String appName = "MySpringBootApp";
    private String version = "1.0.0";

    // 私有构造方法，防止外部实例化
    private AppConfig() {}

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
