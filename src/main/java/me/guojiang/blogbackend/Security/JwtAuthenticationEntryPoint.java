package me.guojiang.blogbackend.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import me.guojiang.blogbackend.Models.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException {
//        log.debug(httpServletRequest.getUserPrincipal().getName());
        log.debug(String.valueOf(e));
        log.debug("Jwt authentication failed");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        if (e instanceof BadCredentialsException) {
            System.out.println("BadCredentialsException");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            newUnAuthenticatedErrorResponse(httpServletResponse, "wrong user or password");
        } else if (e instanceof InvalidJwtAuthenticationException) {
            log.error("token过期");
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            newUnAuthenticatedErrorResponse(httpServletResponse, "expired or invalid JWT token");
        }

        log.debug("执行");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        newUnAuthenticatedErrorResponse(httpServletResponse, "JwtAuthenticationEntryPoint");
    }

    //TODO username should return the request one
    private void newUnAuthenticatedErrorResponse(HttpServletResponse httpServletResponse,
                                                 String message)
            throws IOException {
        var out = httpServletResponse.getWriter();
        var mapper = new ObjectMapper();
        var obj = new HashMap<String, Object>();
        obj.put("token", "");
        obj.put("username", "");
        var repStr = mapper.writeValueAsString(
                new JsonResult<>(obj).setMessage(message).setStatus(httpServletResponse.getStatus()));
        log.debug(repStr);
        out.write(repStr);
        out.flush();
        out.close();
    }
}
