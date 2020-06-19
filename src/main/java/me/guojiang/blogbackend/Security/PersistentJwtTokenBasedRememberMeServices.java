package me.guojiang.blogbackend.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

public class PersistentJwtTokenBasedRememberMeServices extends AbstractRememberMeServices {

    private static final Logger LOG = LoggerFactory.getLogger(PersistentJwtTokenBasedRememberMeServices.class);

    private PersistentTokenRepository tokenRepository;

    private SecureRandom random;

    private int seriesLength = PersistentTokenBasedRememberMeServices.DEFAULT_SERIES_LENGTH;
    private int tokenLength = PersistentTokenBasedRememberMeServices.DEFAULT_TOKEN_LENGTH;
    private static final String DEFAULT_PARAMETER = "rememberMe";

    private String parameter = DEFAULT_PARAMETER;

    public PersistentJwtTokenBasedRememberMeServices(
            String key,
            UserDetailsService userDetailsService,
            PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService);
        random = new SecureRandom();
        this.tokenRepository = tokenRepository;
    }


    /*
    @Value("${security.remember-me-security-key}")
    private String rememberMeSecurityKey;

   //use PersistentTokenBasedRememberMeServices default implementation
    @Override
    protected String[] decodeCookie(String cookieValue) throws InvalidCookieException {
        var encodeKey = getEncodeKey(rememberMeSecurityKey);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(encodeKey)
                    .parseClaimsJws(cookieValue)
                    .getBody();
            return new String[]{claims.getId(), claims.getSubject()};
        } catch (JwtException e) {
            LOG.warn(e.getMessage());
            throw new InvalidCookieException(e.getMessage());
        }
    }

    @Override
    protected String encodeCookie(String[] cookieTokens) {
        var keyBytes = Decoders.BASE64.decode(rememberMeSecurityKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.claims()
                .setId(cookieTokens[0])
                .setSubject(cookieTokens[1])
                .setExpiration(new Date(System.currentTimeMillis() + getTokenValiditySeconds() * 1000L))
                .setIssuedAt(new Date());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key)
                .compact();
    }
    */

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        var persistentRememberMeToken = new PersistentRememberMeToken(
                successfulAuthentication.getName(), generateSeriesData(), generateTokenData(), new Date());
        tokenRepository.createNewToken(persistentRememberMeToken);
        addCookie(persistentRememberMeToken, request, response);
    }

    @Override
    protected void onLoginFail(HttpServletRequest request, HttpServletResponse response) {
        super.onLoginFail(request, response);
    }

    @Override
    protected boolean rememberMeRequested(HttpServletRequest request, String parameter) {
        var isRememberMe = false;
        try {
            isRememberMe = Boolean.parseBoolean((String) request.getAttribute("rememberMe"));
            LOG.debug("is-RememberMe: " + isRememberMe);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return isRememberMe;
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {

        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2
                    + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        var token = tokenRepository.getTokenForSeries(presentedSeries);

        if (token == null) {
            throw new RememberMeAuthenticationException(
                    "No persistent token found for series id: " + presentedSeries);
        }

        // We have a match for this user/series combination
        if (!presentedToken.equals(token.getTokenValue())) {
            // Token doesn't match series value. Delete all logins for this user and throw
            // an exception to warn them.
            tokenRepository.removeUserTokens(token.getUsername());

            throw new CookieTheftException(
                    messages.getMessage(
                            "PersistentTokenBasedRememberMeServices.cookieStolen",
                            "Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack."));
        }

        if (token.getDate().getTime() + getTokenValiditySeconds() * 1000L < System
                .currentTimeMillis()) {
            throw new RememberMeAuthenticationException("Remember-me login has expired");
        }

        // Token also matches, so login is valid. Update the token value, keeping the
        // *same* series number.
        if (logger.isDebugEnabled()) {
            logger.debug("Refreshing persistent login token for user '"
                    + token.getUsername() + "', series '" + token.getSeries() + "'");
        }

        PersistentRememberMeToken newToken = new PersistentRememberMeToken(
                token.getUsername(), token.getSeries(), generateTokenData(), new Date());

        try {
            tokenRepository.updateToken(newToken.getSeries(), newToken.getTokenValue(),
                    newToken.getDate());
            addCookie(newToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException(
                    "Auto login failed due to data access problem");
        }

        return getUserDetailsService().loadUserByUsername(token.getUsername());
    }


    @Override
    public void setParameter(String parameter) {
        super.setParameter(parameter);
    }

    protected String generateSeriesData() {
        byte[] newSeries = new byte[seriesLength];
        random.nextBytes(newSeries);
        return new String(Base64.getEncoder().encode(newSeries));
    }

    protected String generateTokenData() {
        byte[] newToken = new byte[tokenLength];
        random.nextBytes(newToken);
        return new String(Base64.getEncoder().encode(newToken));
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request,
                           HttpServletResponse response) {
        setCookie(new String[]{token.getSeries(), token.getTokenValue()},
                getTokenValiditySeconds(), request, response);
    }

    private String getEncodeKey(String rawKey) {
        return Base64.getEncoder().encodeToString(rawKey.getBytes());
    }

    protected JSONObject getRequestJsonObject(HttpServletRequest request)
            throws IOException, JSONException {
        String rawJson = request.getReader().lines().collect(Collectors.joining());
        return new JSONObject(rawJson);
    }
}
