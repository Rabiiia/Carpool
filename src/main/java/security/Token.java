package security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dtos.UserDTO;
import security.errorhandling.AuthenticationException;

import java.text.ParseException;
import java.util.Date;

public class Token {
    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min

    public static SignedJWT createToken(UserDTO user) throws JOSEException {
        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date now = new Date();
        //Gson gson = new GsonBuilder().create();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getId()))
                .claim("name", user.getName())
                .claim("username", user.getUsername())
                //.claim("address", gson.toJsonTree(user.getAddress()).getAsJsonObject().)
                //.claim("school", gson.toJsonTree(user.getSchool()))
                .claim("role", user.getRole())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        System.out.println(claimsSet);
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }

    public static SignedJWT getVerifiedToken(String jwtString) throws ParseException, JOSEException, AuthenticationException {
        SignedJWT signedJWT = SignedJWT.parse(jwtString);
        JWSVerifier verifier = new MACVerifier(SharedSecret.getSharedKey());
        if (signedJWT.verify(verifier)) {
            if (new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                throw new AuthenticationException("The provided token is not valid");
            }
            System.out.println("Token is valid");
        }
        return signedJWT;
    }
}
