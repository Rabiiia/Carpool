package rest;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import dtos.RequestDTO;
import entities.Request;
import facades.RequestFacade;
import security.Token;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Path("rides/{rideId}/requests")
public class RequestEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RequestFacade REQUEST_FACADE = RequestFacade.getInstance(EMF);
    @PathParam("rideId")
    private int rideId;

    @POST
    //@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response requestSeat(@HeaderParam("x-access-token") String token/*, String jsonString*/) throws AuthenticationException, ParseException, JOSEException {
        SignedJWT signedJWT = Token.getVerifiedToken(token);
        int userId = Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject());
        String status = "pending";
        RequestDTO request = new RequestDTO(REQUEST_FACADE.sendRequest(rideId, userId, status));
        return Response.ok(new Gson().toJson(request)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRequests(@HeaderParam("x-access-token") String token) throws AuthenticationException, ParseException, JOSEException {
        SignedJWT signedJWT = Token.getVerifiedToken(token);
        int userId = Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject());
        List<RequestDTO> requestDTOList = new ArrayList<>();
        for (Request request : REQUEST_FACADE.getRequests(userId)) {
            requestDTOList.add(new RequestDTO(request));
        }
        return Response.ok(new Gson().toJson(requestDTOList)).build();
    }

    @PATCH
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response respondToRequest(@PathParam("id") int id, String jsonString) throws AuthenticationException, ParseException, JOSEException {
        String status = JsonParser.parseString(jsonString).getAsJsonObject().get("status").getAsString();
        RequestDTO request = new RequestDTO(REQUEST_FACADE.updateRequest(id, status));
        return Response.ok(new Gson().toJson(request)).build();
    }
}
