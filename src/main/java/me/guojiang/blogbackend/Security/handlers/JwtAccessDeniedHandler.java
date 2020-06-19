package me.guojiang.blogbackend.Security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.guojiang.blogbackend.Models.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.debug("JwtAccessDeniedHandler method executed!");
        System.out.println("JwtAccessDeniedHandler method executed!");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        var out = response.getWriter();
        var mapper = new ObjectMapper();
        var obj = new HashMap<String, Object>();
        obj.put("token", "");
        obj.put("username", "");
        var repStr = mapper.writeValueAsString(
                new JsonResult<>(obj)
                        .setMessage("Access Denied!")
                        .setStatus(response.getStatus()));
        log.debug(repStr);
        out.write(repStr);
        out.flush();
        out.close();
    }
}
