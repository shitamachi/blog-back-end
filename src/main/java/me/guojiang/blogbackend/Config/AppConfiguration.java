package me.guojiang.blogbackend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfiguration implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    public String uploadFileDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        allow all for develop
        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
                .allowedOrigins("*")
//                .allowedHeaders("Origin", "Content-Length", "Content-Type", "Authorization", "Accept")
//                .allowedHeaders("Origin", "Content-Length", "Content-Type", "Authorization", "Accept", "X-Requested-With", "x-auth-token")
                .allowedHeaders("*")
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("img/**").addResourceLocations("file:" + uploadFileDir + "/");
    }
}
