package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import errorhandling.API_Exception;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class UserFacadeTest {

    private static EntityManagerFactory EMF;
    private static UserFacade USER_FACADE;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    User u1;
    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactoryForTest();
        USER_FACADE= UserFacade.getUserFacade(EMF);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }
    @BeforeEach
    void setUp() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            u1 = new User("testUserName", "test123","Mogens", 20202020, "Værebrovej 18", 2880);
            em.persist(u1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUser() throws API_Exception {

       // User user = new User("testUserName", "test123","Mogens", 20202020, "Værebrovej 18", 2880);
        User actual = USER_FACADE.createUser("testUserName", "test12","testName", 000000, "testAddress", 9990);
        assertTrue(actual.getId()!=0);
        System.out.println(actual.getId()); //should print 2 meaning second index in the list
    }


}