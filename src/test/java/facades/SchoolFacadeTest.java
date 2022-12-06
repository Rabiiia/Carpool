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

class SchoolFacadeTest {

    private static EntityManagerFactory EMF;
    private static SchoolFacade SCHOOL_FACADE;


    School s1, s2;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactoryForTest();
        SCHOOL_FACADE= SchoolFacade.getSchoolFacade(EMF);
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
            em.createNamedQuery("School.deleteAllRows").executeUpdate();
            s1 = new School("testSchoolName", "testSchoolLocation");
            s2 = new School("testSchoolName2", "testSchoolLocation2");
            em.persist(s1);
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
    void createSchool() throws API_Exception {
        School actual = SCHOOL_FACADE.createSchool("CPH business", "Nørgaardsvej 36");
        assertTrue(actual.getId()!=0);
        System.out.println(actual.getId()); //should print 2 meaning second index in the list
    }

    @Test
    void allSchools() throws Exception {
        int actual = SCHOOL_FACADE.getAll().size();
        int expected = 2;
        assertEquals(expected, actual);
        System.out.println(actual);
    }


}
