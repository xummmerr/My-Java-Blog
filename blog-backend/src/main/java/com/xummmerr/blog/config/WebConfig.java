package com.xummmerr.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射本地文件目录
        // 这里的 "file:" + ... 是关键
        String path = System.getProperty("user.dir") + "/files/";

        // 意思是：访问 http://localhost:8080/files/xxx.jpg
        // 映射到：项目根目录/files/xxx.jpg
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + path);
    }
}