package me.guojiang.blogbackend.Security.filters;

import me.guojiang.blogbackend.Exceptions.InvalidJwtAuthenticationException;
import me.guojiang.blogbackend.Security.JwtAuthenticationEntryPoint;
import me.guojiang.blogbackend.Security.providers.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            var token = jwtTokenProvider.resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug(authentication.toString());
                }

            }
        } catch (InvalidJwtAuthenticationException e) {
            logger.error(e);
            SecurityContextHolder.clearContext();
            getAuthenticationEntryPoint().commence(request, response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthorizationTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}
