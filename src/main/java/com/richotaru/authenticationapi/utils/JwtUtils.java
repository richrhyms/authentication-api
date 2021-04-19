package com.richotaru.authenticationapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class JwtUtils {

    @Value("${SECRET_KEY:SSBhbSBSaWNoT3RhcnUgYSBzZWFzb25lZCBzb2Z0d2FyZSBkZXZlbG9wZXI=}")
    private String secretKey;
    private static final String AUTHORITIES_KEY = "roles";
    long tokenValidityInSeconds = 1800; // 30 minutes
    long tokenValidityInMinutes = 15; // 15 minutes
    long tokenValidityInDays = 30; // 30 days
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

    }
    public Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private Boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username){
        Map<String, Object> claim = new HashMap<>(); // TODO implement role based authentication
        return createToken(claim, username);

    }
    private String createToken(Map<String, Object> claim, String subject){
        LocalDateTime now = LocalDateTime.now();
        return Jwts.builder().setClaims(claim).setSubject(subject).setIssuedAt(Timestamp.valueOf(now))
                .setExpiration(Timestamp.valueOf(now.plusMinutes(tokenValidityInMinutes)))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    public Boolean validateToken(String token, String providedUsername){
        final String validatedUsername = extractUsername(token);
        return (validatedUsername.equalsIgnoreCase(providedUsername)) && validateToken(token);

    }
    public Boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            logger.info("Invalid JWT token.");
            logger.trace("Invalid JWT token trace.", e);
        }
        return false;
    }
}
