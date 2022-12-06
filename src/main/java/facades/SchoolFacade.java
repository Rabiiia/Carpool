package facades;

import entities.School;
import errorhandling.API_Exception;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public class SchoolFacade {

    private static SchoolFacade instance;
    private final EntityManagerFactory EMF;

    /**
     *
     * @param emf
     */
    private SchoolFacade(EntityManagerFactory emf) {
        EMF = emf;
    }

    /**
     *
     * @param emf
     * @return the instance of this facade.
     */
    public static SchoolFacade getSchoolFacade(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new SchoolFacade(emf);
        }
        return instance;
    }

    public School createSchool(String name, String location) throws API_Exception {

        School school = new School(name, location);

        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(school);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new API_Exception("Could not create school", 500, e);
        } finally {
            em.close();
        }
        return school;
    }

}
