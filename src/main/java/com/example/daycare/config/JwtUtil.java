package com.example.daycare.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtil {
    private  String jwtSingingKey="secret";

    public  String extractUserName(String token){
        return  extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final  Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSingingKey).parseClaimsJws(token).getBody();
    }

    public  String generateToken(UserDetails userDetails)
    {
        Map<String ,Object> claims=new HashMap<>();
        return  generateToken(userDetails,claims);
    }
    public  String generateToken(UserDetails userDetails, Map<String ,Object> claims)
    {
        String userEmail=userDetails.getUsername();

        return  Jwts.builder().setClaims(claims)
                .setSubject(userEmail)
                .claim("authorities",userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.HOURS.toMillis(24)))
                .signWith(SignatureAlgorithm.HS512,jwtSingingKey).compact();

    }
    public  boolean isTokenValid(String token,UserDetails userDetails){
        final  String userName=extractUserName(token);
        boolean user= userName.equals(userDetails.getUsername()) ;
        boolean date =isTokenExpired(token);
        return  user&& !date;
    }
    private Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    private boolean isTokenExpired(String token) {
        return  extractExpiration(token).before(new Date());
    }
}
