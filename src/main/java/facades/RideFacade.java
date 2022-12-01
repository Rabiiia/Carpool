package facades;

import dtos.Waypoint;
import entities.Request;
import entities.Ride;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

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

    public Ride createRide(int driverId, Waypoint origin, Waypoint destination, long arrival, byte seats) {
        Ride ride = null;
        User driver;
        EntityManager em = EMF.createEntityManager();
        System.out.println("A");
        if ((driver = em.find(User.class, driverId)) != null) {
            System.out.println("B");
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

    public List<Ride> getAllRides() {
        EntityManager em = EMF.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Ride r", Ride.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Ride getRide(int id) {
        EntityManager em = EMF.createEntityManager();
        return em.find(Ride.class, id);
    }
}
