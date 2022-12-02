package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.*;

public class UserEndpointTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

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
    int userId;

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Users.deleteAllRows").executeUpdate();
            User user = new User("user", "test","Mogens", 20202020, "Værebrovej 18", 2880);
            em.persist(user);
            em.getTransaction().commit();
            userId = user.getId();
        } finally {
            em.close();
        }
    }

    @Test
    public void postTest() {
        User user = new User("testUserName", "testPassword","testName", 999999, "testAddress", 9990);


        String requestBody = GSON.toJson(user);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .assertThat()
                .statusCode(200);
                //.body("id", notNullValue()) //
                //.body(JsonPath."name", equalTo("testName"));
                //.body("role", equalTo("user"));

    }

    @Test
    public void testGetSpecificUser() {

        given()
                .contentType("application/json")
                .when()
                .get("/users/"+userId).then()
                .statusCode(200)
                .body("username", equalTo("user"))
                .body("address",equalTo("Værebrovej 18"))
                .body("name", equalTo("Mogens"))
                .body("phone",equalTo(20202020))
                .body("role", equalTo("user"))
                .body("zipcode",equalTo(2880));
    }

}