package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import dtos.RideDTO;
import dtos.Waypoint;
import entities.Ride;
import errorhandling.API_Exception;
import facades.RideFacade;
import security.SharedSecret;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Path("rides")
public class RideEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RideFacade RIDE_FACADE = RideFacade.getInstance(EMF);

    @Context
    private UriInfo context;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRide(@HeaderParam("x-access-token") String jwtString, String jsonString) throws API_Exception, AuthenticationException {
        System.out.println("Given: " + jsonString);
        Waypoint origin, destination;
        float origLng, origLat, destLng, destLat;
        long arrival;
        byte seats;

        try {
            // Extract values from JSON
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            origin = new Waypoint(
                    0,//json.get("origLng").getAsFloat(),
                    0 //json.get("origLat").getAsFloat()
            );
            destination = new Waypoint(
                    0,//json.get("destLat").getAsFloat(),
                    0 //json.get("destLng").getAsFloat()
            );
            arrival = json.get("arrival").getAsLong();
            seats = json.get("seats").getAsByte();
        } catch (JsonSyntaxException e) {
            throw new API_Exception("Malformed JSON supplied", 400, e);
        }

        try {
            // Verify token
            SignedJWT signedJWT = SignedJWT.parse(jwtString);
            JWSVerifier verifier = new MACVerifier(SharedSecret.getSharedKey());
            if (signedJWT.verify(verifier)) {
                if (new Date().getTime() > signedJWT.getJWTClaimsSet().getExpirationTime().getTime()) {
                    throw new AuthenticationException("The provided token is not valid");
                }
                System.out.println("Token is valid");
            }
            // Create ride
            RideDTO ride = new RideDTO(RIDE_FACADE.createRide(Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject()),
                    origin,
                    destination,
                    arrival,
                    seats));
            System.out.println(ride);
            return Response.ok(new Gson().toJson(ride)).build();
        } catch (ParseException e) {
            throw new API_Exception("Could not parse token", 500, e);
        } catch (JOSEException e) {
            throw new API_Exception("Could not verify token", 500, e);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRides() {
        List<RideDTO> rides = RIDE_FACADE.getAllRides().stream().map(RideDTO::new).collect(Collectors.toList());
        System.out.println(rides);
        return Response.ok(new Gson().toJson(rides)).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRide(@PathParam("id") int id) {
        RideDTO ride = new RideDTO(RIDE_FACADE.getRide(id));
        System.out.println(ride);
        return Response.ok(new Gson().toJson(ride)).build();
    }
}
