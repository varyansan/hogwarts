package com.example.shcool_hogwarts.controller;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final ServerProperties serverProperties;

    public InfoController(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @GetMapping("/port")
    public int getPort() {
        return serverProperties.getPort();
    }
}
