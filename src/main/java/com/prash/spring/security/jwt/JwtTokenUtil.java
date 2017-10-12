package com.prash.spring.security.jwt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    
    final Logger log = LoggerFactory.getLogger(this.getClass());

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_AUDIENCE = "audience";
    static final String CLAIM_KEY_CREATED = "created";
    static final String CLAIM_KEY_ROLES = "roles";


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
        	log.error("", e);
            username = null;
            throw e;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
        	log.error("", e);
            created = null;
            throw e;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
        	log.error("", e);
            expiration = null;
            throw e;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            final Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
        	log.error("", e);
            audience = null;
            throw e;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
        	log.error("", e);
            claims = null;
            throw e;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    public Collection<GrantedAuthority> getAuthorities(String token)	{
    	 String roles;
         try {
             final Claims claims = getClaimsFromToken(token);
             roles = (String) claims.get(CLAIM_KEY_ROLES);
         } catch (Exception e) {
         	log.error("", e);
        	 roles = null;
             throw e;
         }
         
         Collection<GrantedAuthority> authorities = Arrays.stream(roles.split("\\|")).map(m -> new SimpleGrantedAuthority(m)).collect(Collectors.toList());
         return authorities;
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(Authentication authentication) {
    	String roles = authentication.getAuthorities().stream().map(m -> m.getAuthority()).collect(Collectors.joining("|"));
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, authentication.getName());
        claims.put(CLAIM_KEY_AUDIENCE, "web");
        claims.put(CLAIM_KEY_ROLES, roles);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
        	log.error("", e);
            refreshedToken = null;
            throw e;
        }
        return refreshedToken;
    }

 
    public Boolean validateToken(String token) {
    	log.debug("Testing debug from JWTTokenUtil");
    	log.info("Testing info from JWTTokenUtil");
        final String username = getUsernameFromToken(token);
        return (username !=null &&
               !isTokenExpired(token));
    }
}