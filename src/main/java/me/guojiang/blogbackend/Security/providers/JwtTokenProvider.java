package me.guojiang.blogbackend.Security.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import me.guojiang.blogbackend.Exceptions.InvalidJwtAuthenticationException;
import me.guojiang.blogbackend.Security.JwtProperties;
import me.guojiang.blogbackend.Services.UserDetailsImplService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsImplService userDetailsService;

    private String secretKey;

    @Value("${verify-email.validity-in-ms}")
    private long verifyExpirationTime;

    public JwtTokenProvider(JwtProperties jwtProperties, UserDetailsImplService userDetailsService) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        var key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", authorities);

//        var now = new Date();
        var expirationDate = new Date(System.currentTimeMillis() + jwtProperties.getValidityInMs());

        return buildToken(key, claims, expirationDate);

    }

    public String generateVerifyToken(String username, String email) {
        //TODO use different secret key
        var keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("email", email);

        var expirationDate = new Date(System.currentTimeMillis() + verifyExpirationTime);

        return buildToken(key, claims, expirationDate);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public String resolveToken(String headerStr) {
        if (headerStr != null && headerStr.startsWith("Bearer ")) {
            return headerStr.substring(7, headerStr.length());
        }
        return null;
    }

    public boolean validateToken(String token) throws InvalidJwtAuthenticationException {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }

    public String buildToken(SecretKey key, Claims claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();
    }

    public <T> T getClaimValue(String token, String key, Class<T> requiredType) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(key, requiredType);
    }
}
