package rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import dtos.RideDTO;
import dtos.Waypoint;
import errorhandling.API_Exception;
import facades.RideFacade;
import security.Token;
import security.errorhandling.AuthenticationException;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

@Path("rides")
public class RideEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final RideFacade RIDE_FACADE = RideFacade.getInstance(EMF);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRide(@HeaderParam("x-access-token") String jwtString, String jsonString) throws API_Exception, AuthenticationException {
        System.out.println("Given: " + jsonString);
        Waypoint origin, destination;
        long arrival;
        byte seats;

        try {
            // Extract values from JSON
            JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
            origin = new Waypoint(
                json.get("origLng").getAsFloat(),
                json.get("origLat").getAsFloat()
            );
            destination = new Waypoint(
                json.get("destLat").getAsFloat(),
                json.get("destLng").getAsFloat()
            );
            arrival = json.get("arrival").getAsLong();
            seats = json.get("seats").getAsByte();
        } catch (JsonSyntaxException e) {
            throw new API_Exception("Malformed JSON supplied", 400, e);
        }

        try {
            SignedJWT signedJWT = Token.getVerifiedToken(jwtString);
            RideDTO ride = new RideDTO(RIDE_FACADE.createRide(
                    Integer.parseInt(signedJWT.getJWTClaimsSet().getSubject()),
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
