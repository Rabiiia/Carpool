package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.SchoolDTO;
import entities.School;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SchoolEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    School s1, s2;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        s1 = new School("testSchoolName", "testSchoolLocation", 2800);
        s2 = new School("testSchoolName2", "testSchoolLocation2", 2800);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("School.deleteAllRows").executeUpdate();
            em.persist(s1);
            em.persist(s2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void postTest() {
        SchoolDTO school = new SchoolDTO("DTU", "Kongens Lyngby", 2800  );
        String requestBody = GSON.toJson(school);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/schools")
                .then()
                .assertThat()
                .statusCode(200)
                //.extract().body().jsonPath().getJsonObject("schoolName")
                .body("schoolName", equalTo("DTU"));//.body("role", equalTo("user"));


    }

    @Test
    public void getAll() throws Exception {
        List<SchoolDTO> schoolDTOS;

        schoolDTOS = given()
                .contentType("application/json")
                .when()
                .get("/schools")
                .then()
                .extract().body().jsonPath().getList("", SchoolDTO.class);

        SchoolDTO s1DTO = new SchoolDTO(s1);
        SchoolDTO s2DTO = new SchoolDTO(s2);
        assertThat(schoolDTOS, containsInAnyOrder(s1DTO, s2DTO));

    }
}
