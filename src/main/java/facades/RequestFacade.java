package facades;

import entities.Request;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(new Request(rideId, userId, status));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void updateRequest(int id, String status) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            Request request = em.find(Request.class, id);
            if (request != null) {
                request.setStatus(status);
                em.merge(request);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
