package security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import entities.User;

import java.util.Date;
import java.util.List;

// We have moved createdToken from LoginEndPoint into its own class
// so we can use the methods in more than just one endpoint
public class Token {
    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min

    public static SignedJWT createToken(User user) throws JOSEException {
        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date now = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .claim("name", user.getName())
                .claim("username", user.getUsername())
                .claim("role", user.getRole())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT;
    }
}
