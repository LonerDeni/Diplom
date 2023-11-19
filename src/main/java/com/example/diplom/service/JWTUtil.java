package com.example.diplom.service;

import com.example.diplom.entity.AuthRequest;
import com.example.diplom.exception.AuthTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.stereotype.Component;

@Slf4j
@Component //Скорее всего это сервис
public class JWTUtil {

    @Value("${jwt.secret.access}")
    private String jwtSecret;

    public String generateJwtToken(AuthRequest authRequest) {
//        return Jwts.builder()
//                .setSubject(authRequest.getLogin())
//                .setExpiration(accessExpiration)
//                .signWith(jwtAccessSecret)
//                .claim("test")
//                .compact();
            final LocalDateTime now = LocalDateTime.now();
            final Instant accessExpirationInstant = now.plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant();
            final Date accessExpiration = Date.from(accessExpirationInstant);
            String tokenUser = Jwts.builder().setSubject((authRequest.getLogin())).setIssuedAt(new Date())
                    .setExpiration(accessExpiration)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
        log.info("Create token");
            return tokenUser;
    }

    public boolean validateJwtToken(String jwt) {
        try {
            log.info("Validation token");
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(jwt);
            log.info("Token valid");
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid Jwt token",e);
            throw new AuthTokenException("Invalid Jwt token");
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
            throw new AuthTokenException("Token expired");
        } catch (Exception e) {
            log.error("Failed authorized", e);
        }
        return false;
    }

    public String getUserNameFromJwtToken(String jwt) {
        log.info("Get login from token");
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getSubject();
    }
}
