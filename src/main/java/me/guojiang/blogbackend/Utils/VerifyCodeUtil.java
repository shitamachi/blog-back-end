package me.guojiang.blogbackend.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class VerifyCodeUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public static String generateCode(int length) {
        var rand = new Random();
        return rand.ints(0, 10).boxed()
                .limit(length)
                .map(Objects::toString)
                .collect(Collectors.joining());
    }

    public String generateCodeAndSave(String username, String email, long expTime, int length) {
        return save(username, email, expTime, generateCode(length));
    }

    public String save(String username, String email, long expTime, String code) {
        var map = Map.of(
                "email", email,
                "code", code,
                "exp", expTime);
        redisTemplate.opsForHash().putAll(username, map);
        return code;
    }

    public boolean verifyCode(String username, String email, String code) {

        var timeStamp = RedisUtil.hGet(username, "exp");
        if (timeStamp == null || code == null) {
            return false;
        }

        var expDate = LocalDateTime.ofEpochSecond((long) timeStamp, 0, ZoneOffset.ofHours(8));
        if (LocalDateTime.now().isAfter(expDate)) return false;

        var saveCode = RedisUtil.hGet(username, "code");
        return saveCode != null && saveCode.equals(code);
    }

    public Boolean existPreviousRequest(String username) {
        return redisTemplate.hasKey(username);
    }

    public boolean deletePreRequestIfExist(String username) {
        if (existPreviousRequest(username)) {
            if (LocalTime.now().isBefore((LocalTime) redisTemplate.opsForHash().get(username, "exp"))) {
                redisTemplate.delete(username);
                return true;
            }
        }
        return false;
    }

    public Boolean discardRequest(String key) {
        return redisTemplate.delete(key);
    }

}
