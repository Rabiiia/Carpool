package facades;

import entities.Ride;
import entities.User;
import dtos.Waypoint;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;

public class RideFacade {
    private static RideFacade instance;
    private final EntityManagerFactory EMF;

    /**
     *
     * @param emf
     */
    private RideFacade(EntityManagerFactory emf) {
        EMF = emf;
    }

    /**
     *
     * @param emf
     * @return the instance of this facade.
     */
    public static RideFacade getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new RideFacade(emf);
        }
        return instance;
    }

    public Ride createRide(int driverId, Waypoint origin, Waypoint destination, long arrival, int seats) {
        Ride ride = null;
        User driver;
        EntityManager em = EMF.createEntityManager();
        if ((driver = em.find(User.class, driverId)) != null) {
            ride = new Ride(origin, destination, arrival, seats, driver);
            try {
                em.getTransaction().begin();
                em.persist(ride);
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }
        return ride;
    }

}
