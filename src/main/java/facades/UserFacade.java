package facades;

import javax.persistence.*;

import entities.School;
import entities.User;
import errorhandling.API_Exception;
import security.errorhandling.AuthenticationException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {
    private static UserFacade instance;
    private final EntityManagerFactory EMF;

    /**
     *
     * @param emf
     */
    private UserFacade(EntityManagerFactory emf) {
        EMF = emf;
    }

    /**
     *
     * @param emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new UserFacade(emf);
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = EMF.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            user = query.getSingleResult();
            if (!user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } catch (NoResultException e) {
            throw new AuthenticationException("Invalid user name or password");
        } finally {
            em.close();
        }
        return user;
    }

    // added this method because so that we can get new token everytime reloading the page in front end
    // we want this because we want to be stayed logged when we are active
    // however you will be logged out after 30 minutes if you are not active
    public User getUser(String username) throws AuthenticationException {
        EntityManager em = EMF.createEntityManager();
        User user;
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u where u.username = :username", User.class);
            query.setParameter("username", username);
            user = query.getSingleResult();
            if (user == null) {
                throw new AuthenticationException("Faulty token");
            }
        } finally {
            em.close();
        }
        return user;
    }



    public User createUser(String username, String password, String name, Integer phone, String address, Integer zipcode, int schoolId) throws API_Exception {

        // Construct user:
        User user = new User(username, password, name, phone, address, zipcode);


        // Persist user to database:
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            School school = em.find(School.class, schoolId);
            if(school != null) {
                user.setSchool(school);
                em.persist(user);
            }
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new API_Exception("Could not create user", 500, e);
        } finally {
            em.close();
        }
        return user;
    }

    public User getUserById(int id) {
        EntityManager em = EMF.createEntityManager();
        try {
            User user = em.find(entities.User.class, id);
            return user;
        } finally {
            em.close();
        }
    }
}
