package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.SchoolDTO;
import errorhandling.API_Exception;
import facades.SchoolFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("schools")
public class SchoolEndpoint {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final SchoolFacade SCHOOL_FACADE = SchoolFacade.getSchoolFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String jsonString) throws API_Exception {
        String name, street;
        int zipcode;

        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        name = jsonObject.get("name").getAsString();
        street = jsonObject.get("street").getAsString();
        zipcode = jsonObject.get("zipcode").getAsInt();

        SchoolDTO school = new SchoolDTO(SCHOOL_FACADE.createSchool(name, street, zipcode));
        System.out.println(GSON.toJson(school));
        return Response.ok(new Gson().toJson(school)).build();

    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllSchools() {
        List<SchoolDTO> schools = SCHOOL_FACADE.getAll().stream().map(SchoolDTO::new).collect(Collectors.toList());
        System.out.println(schools);
        return Response.ok(new Gson().toJson(schools)).build();
    }
}
