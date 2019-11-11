package me.guojiang.blogbackend.Security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /**
     * secretKey must have 258bits length at least,if using HMAC-SHA algorithms to encode
     */
    private String secretKey = "guojiang";

    /**
     * validity in milliseconds
     * 1 equals 1ms 1000 == 1s default 1h
     */
    private long validityInMs = 3600000; // 1h; 1000 == 1ms

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public long getValidityInMs() {
        return validityInMs;
    }

    public void setValidityInMs(long validityInMs) {
        this.validityInMs = validityInMs;
    }
}
