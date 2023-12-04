package com.example.diplom.security;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.exception.AuthTokenException;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {

    private final CacheManager cacheManager;

    @Value("${jwt.secret.access}")
    private String jwtSecret;

    public String generateJwtToken(AuthRequest authRequest) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusHours(1).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        String tokenUser = Jwts.builder().setSubject((authRequest.getLogin())).setIssuedAt(new Date())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        log.info("Create token");
        return tokenUser;
    }

    public boolean validateJwtToken(String jwt) {
        if (checkCacheToken(jwt)) {
            log.error("User logout token");
            throw new AuthTokenException("Invalid Jwt token. User logout");
        }
        try {
            log.info("Validation token");
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt);
            log.info("Token valid");
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid Jwt token", e);
            throw new AuthTokenException("Invalid Jwt token");
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
            throw new AuthTokenException("Token expired");
        } catch (SignatureException e) {
            log.error("Error with authorized signature does not match", e);
            throw new AuthTokenException("Error with authorized");
        } catch (
                Exception e) {
            log.error("Failed authorized", e);
        }
        return false;
    }

    public String getUserNameFromJwtToken(String jwt) {
        log.info("Get login from token");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }
    public boolean checkCacheToken(String jwt){
        if(cacheManager.getCache("jwtCache").get(jwt) != null) {
            return true;
        }
        return false;
    }
}