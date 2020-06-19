package me.guojiang.blogbackend.Security.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.guojiang.blogbackend.Models.JsonResult;
import me.guojiang.blogbackend.Security.providers.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationSuccessHandler.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        LOG.debug("JwtAuthenticationSuccessHandler method executed!");
        response.setStatus(HttpServletResponse.SC_OK);
        var token = tokenProvider.generateToken(authentication.getName(), authentication.getAuthorities());
        try (var out = response.getWriter()) {
            var mapper = new ObjectMapper();
            var obj = new HashMap<String, Object>();
            obj.put("token", token);
            obj.put("username", authentication.getName());
            var repStr = mapper.writeValueAsString(
                    new JsonResult<>(obj)
                            .setMessage("Authentication Success")
                            .setStatus(response.getStatus()));
            out.write(repStr);
        }
    }
}
