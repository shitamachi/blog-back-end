package me.guojiang.blogbackend.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties()
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private String rememberMeSecurityKey;

    public String getRememberMeSecurityKey() {
        return rememberMeSecurityKey;
    }

    public void setRememberMeSecurityKey(String rememberMeSecurityKey) {
        this.rememberMeSecurityKey = rememberMeSecurityKey;
    }
}
