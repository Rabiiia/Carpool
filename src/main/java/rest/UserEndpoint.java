package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.UserDTO;
import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("users")
public class UserEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String jsonString) throws API_Exception {
       String username, password, name, address, schoolName, location;
        //String username, password, name, address;
        int phone, zipcode;
        //int schoolId;

        try {
            //her laver vi lidt user entity objekt en dto a la' retning, så den ikke går den ned i databasen for at createUser
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            username = jsonObject.get("username").getAsString();
            password = jsonObject.get("password").getAsString();
            name = jsonObject.get("name").getAsString();
            phone = jsonObject.get("phone").getAsInt();
            address = jsonObject.get("address").getAsString();
            zipcode = jsonObject.get("zipcode").getAsInt();
            schoolName = jsonObject.get("schoolName").getAsString();
            location = jsonObject.get("location").getAsString();
            // schoolId = jsonObject.get("schoolId").getAsInt();
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        UserDTO user = new UserDTO(USER_FACADE.createUser(username, password, name, phone, address, zipcode, schoolName, location));
        //UserDTO user = new UserDTO(USER_FACADE.createUser(username, password, name, phone, address, zipcode, schoolId));
        String userJSON = GSON.toJson(user);
        System.out.println(userJSON);
        return Response.ok(userJSON).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getSpecificUser(@PathParam("id") int id) {
        String userJSON = GSON.toJson(new UserDTO(USER_FACADE.getUserById(id)));
        return Response.ok(userJSON).build();
    }
}
