package rest;

import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import facades.RequestFacade;
import security.Token;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.text.ParseException;

@Path("requests")
public class RequestEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RequestFacade REQUEST_FACADE = RequestFacade.getInstance(EMF);

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestSeat(@PathParam("id") int id, @HeaderParam("x-access-token") String token, String jsonString) throws AuthenticationException, ParseException, JOSEException {
        SignedJWT signedJWT = Token.getVerifiedToken(token);
        String status = "pending";
        //String status = JsonParser.parseString(jsonString).getAsJsonObject().get("status").getAsString();
        REQUEST_FACADE.sendRequest(id, Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject()), status);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void acceptPassenger(@PathParam("id") int id, @HeaderParam("x-access-token") String token, String jsonString) throws AuthenticationException, ParseException, JOSEException {
        SignedJWT signedJWT = Token.getVerifiedToken(token);
        String status = JsonParser.parseString(jsonString).getAsJsonObject().get("status").getAsString();
        REQUEST_FACADE.updateRequest(id, Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject()), status);
    }
}
