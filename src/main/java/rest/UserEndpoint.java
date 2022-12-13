package rest;

import com.google.gson.*;
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
        System.out.println(jsonString);

        String name, email, address, password;
        int phone, zipcode;
        int school;

        try {
            JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            name = jsonObject.get("name").getAsString();
            email = jsonObject.get("email").getAsString();
            phone = jsonObject.get("phone").getAsInt();
            address = jsonObject.get("address").getAsString();
            zipcode = jsonObject.get("zipcode").getAsInt();
            password = jsonObject.get("password").getAsString();
            school = jsonObject.get("school").getAsInt();
        } catch (JsonSyntaxException e) {
            throw new API_Exception("Malformed JSON Supplied", 400, e);
        }

        UserDTO user = new UserDTO(USER_FACADE.createUser(email, password, name, phone, address, zipcode, school));
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
