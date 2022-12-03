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
import javax.ws.rs.core.MediaType;
import java.text.ParseException;

@Path("rides/{rideId}/requests")
public class RequestEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RequestFacade REQUEST_FACADE = RequestFacade.getInstance(EMF);
    @PathParam("rideId")
    private int rideId;

    @POST
    //@Consumes(MediaType.APPLICATION_JSON)
    public void requestSeat(@HeaderParam("x-access-token") String token/*, String jsonString*/) throws AuthenticationException, ParseException, JOSEException {
        SignedJWT signedJWT = Token.getVerifiedToken(token);
        int userId = Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject());
        String status = "pending";
        REQUEST_FACADE.sendRequest(rideId, userId, status);
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void respondToRequest(@PathParam("id") int id, String jsonString) throws AuthenticationException, ParseException, JOSEException {
        String status = JsonParser.parseString(jsonString).getAsJsonObject().get("status").getAsString();
        REQUEST_FACADE.updateRequest(id, status);
    }
}
