package me.lin.amn.repository.controller;

import me.lin.amn.repository.ServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Created by Lin on 6/27/16.
 */
@EnableAutoConfiguration
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(ServerConfig.class, args);
    }
}
