package com.richotaru.authenticationapi.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
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
    public String generateToken( UserDetails userDetails){
        Map<String, Object> claim = new HashMap<>();
        return createToken(claim, userDetails.getUsername());

    }
    private String createToken(Map<String, Object> claim, String subject){
        return Jwts.builder().setClaims(claim).setSubject(subject).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 *60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey).compact();
    }
    private Boolean validateToken(String token, UserDetails userDetails){
        final String username = userDetails.getUsername();
        return (username.equalsIgnoreCase(userDetails.getUsername())) && !isTokenExpired(token);

    }
}
