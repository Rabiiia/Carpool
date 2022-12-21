package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dtos.RideDTO;
import dtos.UserDTO;
import entities.Ride;
import entities.School;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RideEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    User u1, u2;
    School s1, s2;
    Ride r1, r2;

    int rideId;

    private static String securityToken;

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

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        String username = "user1";
        String password = "test123";
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Ride.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("School.deleteAllRows").executeUpdate();
            u1 = new User("user1", password,"testName",8198201, "testAddress", 2400);
            u2 = new User("user2", password,"testName2",22222222, "testAddress2", 2222);
            s1 = new School("testSchoolName", "testSchoolLocation", 2400);
            s2 = new School("testSchoolName2", "testSchoolLocation2", 2222);
            r1 = new Ride("originTest", "destinationTest", 1L, (byte) 4, u1);
            r2 = new Ride("originTest2", "destinationTest2", 2L, (byte) 2, u2);

            em.persist(u1);
            u1.setSchool(s1);
            em.persist(s1);

            em.persist(u2);
            u2.setSchool(s2);
            em.persist(s2);

            em.persist(r1);
            em.persist(r2);
            em.getTransaction().commit();
            rideId = r1.getId();
        } finally {
            em.close();
        }

        String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    public void postRidesTest() {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("origin", "originTest");
        requestBody.addProperty("destination", "destinationTest");
        requestBody.addProperty("arrival", 1);
        requestBody.addProperty("seats", 4);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .and()
                .body(requestBody)
                .when()
                .post("/rides")
                .then()
                .assertThat()
                .statusCode(200)
                .body("origin", equalTo("originTest"));
    }

    /*@Test
    public void getAll() {
        List<RideDTO> rideDTOS;

        rideDTOS = given()
                .contentType("application/json")
                .when()
                .get("/rides")
                .then()
                .extract().body().jsonPath().getList("", RideDTO.class);

        RideDTO r1DTO = new RideDTO(r1);
        RideDTO r2DTO = new RideDTO(r2);
        assertThat(rideDTOS, containsInAnyOrder(r1DTO, r2DTO));
    }*/

    @Test
     void testGetSpecificRide() {
        given()
                .contentType("application/json")
                .when()
                .get("/rides/{id}", rideId).then()
                .statusCode(200)
                .body("id", equalTo(r1.getId()))
                .body("origin", equalTo(r1.getOrigin()));
               // .body("children", hasItems(hasEntry("name","Joseph"),hasEntry("name","Alberta")));
        System.out.println(rideId);
    }

    /*@Test
    void testRideByDestination() {
        String destination = r1.getDestination();

        given()
                .contentType("application/json")
                .when()
                .get("/rides/{destination}", destination).then()
                .statusCode(200);
                //.body("destination", equalTo(r1.getDestination()));

        System.out.println(destination);
    }*/
}
