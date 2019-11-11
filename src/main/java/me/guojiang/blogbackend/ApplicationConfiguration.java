package me.guojiang.blogbackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableJpaRepositories("me.guojiang.blogbackend.Repositories")
public class ApplicationConfiguration implements WebMvcConfigurer {

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
}
