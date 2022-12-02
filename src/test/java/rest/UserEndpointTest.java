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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {

            em.getTransaction().begin();
            //Delete existing users and roles to get a "fresh" database
            //em.createNamedQuery("User.deleteAllRows").executeUpdate();
            //User user = new User("user", "test","Mogens", 20202020, "Værebrovej 18", 2880);

            User admin = new User("admin", "test","Konrad", 30303030, "Liljevej 13", 2900);

            //admin.setRole("admin");
            //em.persist(user);
            em.persist(admin);

            //System.out.println("Saved test data to database");
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void postTest() {
        User user = new User("TestUserName", "testPassword","testName", 999999, "testAddress", 9990);


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
                .get("/users/2").then()
                .statusCode(200)
                .body("username", equalTo("admin"))
                .body("address",equalTo("Liljevej 13"))
                .body("name", equalTo("Konrad"))
                .body("phone",equalTo(30303030))
                .body("role", equalTo("user"))
                .body("zipcode",equalTo(2900));
    }

}
