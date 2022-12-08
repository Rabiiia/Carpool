package security;

import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import entities.User;
import facades.UserFacade;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.Date;

@Path("verify")
public class TokenEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);

    //http://localhost:8080/api/verify GET
    //when GETTING with token: "bcrypt" it should get admin and admins roles or token is not valid
    //få useren til at blive logget ud efter 30 min , skal vi gøre her i denne ressource
    //token har bare username og expiration date

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyToken(@HeaderParam("x-access-token") String token) throws AuthenticationException {
        // The token is given as the header "x-access-token" in the request, so we get it with a @HeaderParam annotation
        System.out.println("Token: " + token);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SharedSecret.getSharedKey());
            if (signedJWT.verify(verifier)) {
                if (new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                    throw new AuthenticationException("Token is no longer valid");
                }
            }
            System.out.println("Token is valid");
            String username = signedJWT.getJWTClaimsSet().getClaim("username").toString();
            User user = USER_FACADE.getUser(username);
            SignedJWT renewedToken = Token.createToken(user);
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", user.getUsername());
            responseJson.addProperty("token", renewedToken.serialize());
            return Response.ok(responseJson.toString()).build();
        } catch (ParseException e) {
            System.out.println("ParseException: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            System.out.println("JOSEException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
