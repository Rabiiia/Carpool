package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.School;
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
    School s1, s2;
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
            em.createNamedQuery("Users.deleteAllRows").executeUpdate();
            em.createNamedQuery("School.deleteAllRows").executeUpdate();
           // u1 = new User("testUserName", "test123","Mogens", 20202020, "Værebrovej 18", 2880);
           // s1 = new School("testSchoolName", "testSchoolLocation");
            s2 = new School("CPH BUSINESS", "Nørgaardsvej 31", 2800);

//            // persist user
//            em.persist(u1);
//            // add user to school
//            s1.addUser(u1);
//            // persist school
//            em.persist(s1);

            //persist school and add users later in the test down below
            em.persist(s2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createUserAndSchool() throws API_Exception {


        USER_FACADE.createUser("Per",
                "test123",
                "Per Madsen",
                8847492,
                "Pilegårdsvej 5",
                1860, 1);

                USER_FACADE.createUser("Konrad",
                "test123",
                "Martinus mark",
                7555555,
                "Søndermark 5",
                9876, 1);


        //Testing if there are actually 2 users that are registered on the same school ?
        assertEquals(1, s2.getId());

    }


}