package com.bruce.single.controller;

import com.bruce.single.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    @Autowired
    private AppConfig appConfig;

    @GetMapping("/config")
    public String getConfig() {
        return "App Name: " + appConfig.getAppName() + ", Version: " + appConfig.getVersion();
    }
}
