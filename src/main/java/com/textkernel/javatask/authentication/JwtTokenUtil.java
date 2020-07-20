package com.textkernel.javatask.authentication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.textkernel.javatask.constants.Constants;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


/**
 * @author AKUTLU
 * This class is responsible to create JSON Web Token
 */
@Slf4j
@Component
public class JwtTokenUtil {

    /**
     * JSON Web Token is created from following parameters
     *
     * @param id        JWT Id
     * @param issuer    username
     * @param subject   password
     * @param ttlMillis Expiration time
     * @return
     */
    public String createJWT(String id, String issuer, String subject, long ttlMillis) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.JWT_API_KEY_SECRET);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }


    public static boolean isTokenExpired(String jwtString) {
        try {
            Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(Constants.JWT_API_KEY_SECRET),
                    SignatureAlgorithm.HS256.getJcaName());
                     Jwts.parser()
                    .setSigningKey(hmacKey)
                    .parseClaimsJws(jwtString);
            return false;
        } catch (ExpiredJwtException ex) {
            log.error("Token is expired");
            return true;
        }
    }


    public static <T> String convertToJSON(T t) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(t);
    }
}
