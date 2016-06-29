package me.lin.amn.repository.controller;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Lin on 6/28/16.
 */
@Configuration
@ComponentScan(basePackages = "me.lin.amn.repository.controller")
public class ControllerConfig {

    public ControllerConfig(){

    }

//    @Bean
//    ServletFileUpload servletFileUpload() {
//        DiskFileItemFactory factory = new DiskFileItemFactory();
//        ServletFileUpload fileUploadServlet = new ServletFileUpload(factory);
//        return fileUploadServlet;
//    }
}
