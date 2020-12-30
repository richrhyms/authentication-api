package com.richotaru.authenticationapi.utils;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class JwtUtils {

    @Value("${SECRET_KEY:SSBhbSBSaWNoT3RhcnUgYSBzZWFzb25lZCBzb2Z0d2FyZSBkZXZlbG9wZXI=}")
    private String secretKey;
    private static final String AUTHORITIES_KEY = "roles";
    long tokenValidityInSeconds = 1800; // 30 minutes
    long tokenValidityInSecondsForClient = 2592000; // 30 days
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

    public String generateToken(String username, boolean isClient){
        Map<String, Object> claim = new HashMap<>(); // TODO implement role based authentication
        return createToken(claim, username, isClient);

    }
    private String createToken(Map<String, Object> claim, String subject, boolean isClient){
        long now = (new Date()).getTime();
        Date validity;
        if (isClient) {
            validity = new Date(now + tokenValidityInSecondsForClient);
        } else {
            validity = new Date(now + tokenValidityInSeconds);
        }
        return Jwts.builder().setClaims(claim).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(validity)
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
