package facades;

import entities.Request;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public Request sendRequest(int rideId, int userId, String status) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            Request request = new Request(rideId, userId, status);
            em.persist(request);
            em.getTransaction().commit();
            return request;
        } finally {
            em.close();
        }
    }

    public List<Request> getRequests(int userId) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Request> query = em.createQuery("SELECT r FROM Request r WHERE r.user.id = :uid", Request.class);
            query.setParameter("uid", userId);
            em.getTransaction().commit();
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Request updateRequest(int id, String status) {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            Request request = em.find(Request.class, id);
            if (request != null) {
                request.setStatus(status);
                em.merge(request);
            }
            em.getTransaction().commit();
            return request;
        } finally {
            em.close();
        }
    }
}
