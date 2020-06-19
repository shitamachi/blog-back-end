package me.guojiang.blogbackend.Security.providers;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

public class JwtAuthenticationProvider extends DaoAuthenticationProvider {
//    @Override
//    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//        if (authentication.getCredentials() == null) {
//            logger.debug("Authentication failed: no credentials provided");
//
//            throw new BadCredentialsException(messages.getMessage(
//                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
//                    "Bad credentials"));
//        }
//
//        String presentedPassword = authentication.getCredentials().toString();
//    }
}
