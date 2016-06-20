package me.lin.amn.repository;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * config java file, mainly for spring mvc.
 * Created by Lin on 6/20/16.
 */
public class ServerConfig {
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public ServletFileUpload fileUploadServlet() {
        return new ServletFileUpload();
    }
}
