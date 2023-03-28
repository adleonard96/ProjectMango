package com.mango.blog.Authentication;

import com.mango.blog.User.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class JwtGenerator implements JwtGeneratorInterface{

    private final static String SECRET = "Zr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?";

    private String message = "Token generated successfully";
    @Override
    public Map<String, String> generatetoken(User user) {
        String jwtToken="";
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = Base64.getEncoder().encode(SECRET.getBytes());
        Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, signingKey);
        jwtToken = builder.compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }

    public static String decodeToken(String jwt){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretBytes = Base64.getEncoder().encode(SECRET.getBytes());
        Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());
        try{
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt).getBody().getSubject();
        }catch (Exception e){
            return null;
        }
    }
}

