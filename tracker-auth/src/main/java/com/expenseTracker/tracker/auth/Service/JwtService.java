package com.expenseTracker.tracker.auth.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.Subject;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey = "";


    // this generate a new key for every run
//    public JwtService() {
//       try {
//           KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//           SecretKey skey = keyGenerator.generateKey();
//           secretKey = Base64.getEncoder().encodeToString(skey.getEncoded());
//       } catch (NoSuchAlgorithmException e) {
//           throw new RuntimeException(e);
//       }
//    }

    public  boolean validateToken(String token, UserDetails userDetails)  {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpiration(token));
    }

    public String extractUsername(String token) {
        return  extractClaims(token, Claims::getSubject);
    }

    private boolean isTokenExpiration(String token) {
        return  extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return (Date) extractClaims(token,Claims::getExpiration);
    }

    private  <T>T extractClaims(String token, Function<Claims, T> ClaimResolver ) {
        final Claims claims = extractAllClaims(token);
        return ClaimResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();
    }


//    // username is only subject here
//    public String  generateToken(String username) {
//        Map<String, Object> claims = new HashMap<>();
//        return Jwts.builder()
//                .claims()
//                // .addClaims()
//                .subject(username)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis()+30*60*1000))
//                . and()
//                .signWith(getKey())
//                .compact();
//    }

    // username or email from google is subject here
    public String generateToken(Map<String, Object> extraClaims, String subject) {
        return Jwts.builder()
                .claims()
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+30*60*100))
                .and()
                .signWith(getKey())
                .compact();
    }

    public SecretKey getKey() {
        //byte[] keyBytes = Base64.Decoders.decode(secretKey);// wrong way sha
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
