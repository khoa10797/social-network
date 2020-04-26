package com.dangkhoa.socialnetwork.services

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.MACSigner
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService {

    @Value('${spring.jwt.secret-key}')
    private String SECRET_KEY
    @Value('${spring.jwt.expired-time}')
    private Integer EXPIRE_TIME

    public static final String USERNAME = "user_name"

    String generateToken(String userName) {
        JWSSigner jwsSigner = new MACSigner(SECRET_KEY.getBytes())
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
        builder.claim(USERNAME, userName)
        builder.expirationTime(generateExpirationDate())

        JWTClaimsSet jWTClaimsSet = builder.build()
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jWTClaimsSet)
        signedJWT.sign(jwsSigner)

        return signedJWT.serialize()
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME)
    }

    private JWTClaimsSet getClaimsFromToken(String token) {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
        if (signedJWT.verify(verifier)) {
            return signedJWT.getJWTClaimsSet();
        }
        return null
    }


    private Date getExpirationDateFromToken(String token) {
        JWTClaimsSet claims = getClaimsFromToken(token)
        return claims.getExpirationTime()
    }

    String getUsernameFromToken(String token) {
        JWTClaimsSet claims = getClaimsFromToken(token)
        return claims.getStringClaim(USERNAME)
    }

    private Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token)
        return expiration.before(new Date())
    }

    Boolean validateTokenLogin(String token) {
        if (token == null || token.trim().length() == 0) {
            return false
        }
        String username = getUsernameFromToken(token)
        if (username == null || username.isEmpty()) {
            return false
        }
        if (isTokenExpired(token)) {
            return false
        }
        return true
    }
}
