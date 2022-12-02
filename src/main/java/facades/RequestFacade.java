package facades;

import entities.Request;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.stream.Collectors;

public class RequestFacade {
    private static RequestFacade instance;
    private final EntityManagerFactory EMF;

    /**
     * @param emf
     */
    private RequestFacade(EntityManagerFactory emf) {
        EMF = emf;
    }

    /**
     * @param emf
     * @return the instance of this facade.
     */
    public static RequestFacade getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new RequestFacade(emf);
        }
        return instance;
    }

    public void sendRequest(int rideId, int userId, String status) {
        Request request = new Request(rideId, userId, status);

        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(request);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void updateRequest(int rideId, int userId, String status) {
        //Request request = new Request(rideId, userId, status);

        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Request> query = em.createQuery("SELECT r FROM Request r WHERE r.user.id = :uid AND r.ride.id = :rid AND r.status = :status", Request.class);
            query.setParameter("uid", userId);
            query.setParameter("rid", rideId);
            query.setParameter("status", "pending");
            for (Request request : query.getResultList()) {
                request.setStatus(status);
                em.merge(request);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
