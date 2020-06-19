package me.guojiang.blogbackend.Security.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProceedingRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProceedingRememberMeAuthenticationFilter.class);

    private AuthenticationSuccessHandler successHandler;

    public ProceedingRememberMeAuthenticationFilter(
            AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {

        super(authenticationManager, rememberMeServices);
    }

    @Override
    public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }


    @Override
    protected void onSuccessfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

        if (successHandler == null) {
            logger.debug("null");
            return;
        }

        try {
            successHandler.onAuthenticationSuccess(request, response, authResult);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    protected void onUnsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) {
//        super.onUnsuccessfulAuthentication(request, response, failed);
        logger.warn(failed.getMessage());
    }
}
