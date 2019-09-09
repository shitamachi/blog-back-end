package me.guojiang.blogbackend;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("me.guojiang.blogbackend.Repositories")
public class ApplicationConfiguration {


}
