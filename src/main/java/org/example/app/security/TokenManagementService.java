package org.example.app.security;

import io.jsonwebtoken.*;
import org.example.app.exeptions.JwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

@Component
public class TokenManagementService {
    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.expire}")
    private String expireTimeAccessToken;

//    @Value("${expireTimeRefreshToken}")
//    private String expireTimeRefreshToken;

    private UserPrincipalDetailsService userPrincipalDetailsService;

    @Autowired
    public TokenManagementService(UserPrincipalDetailsService userPrincipalDetailsService) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateTokenPair(String email) {
        long nowMillis = System.currentTimeMillis();
        long expirationTime = Long.parseLong(expireTimeAccessToken);
        Date expiryDate = new Date(nowMillis + expirationTime);

        return Jwts.builder()//
                .setSubject(email)//
                .setIssuedAt(new Date())//
                .setExpiration(expiryDate)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }

    }

    public String resolveAccessToken(HttpServletRequest request) {
        String AUTH_HEADER_PREFIX = "Bearer ";
        String AUTHORIZATION_HEADER = "Authorization";
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (Objects.nonNull(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userPrincipalDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
