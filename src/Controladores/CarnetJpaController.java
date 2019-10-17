/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Alumno;
import Entidades.Carnet;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author PANCHY
 */
public class CarnetJpaController implements Serializable {

    public CarnetJpaController() {
        this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public CarnetJpaController(EntityManagerFactory instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carnet carnet) throws PreexistingEntityException, Exception {
        if (carnet.getAlumnoCollection() == null) {
            carnet.setAlumnoCollection(new ArrayList<Alumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Alumno> attachedAlumnoCollection = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionAlumnoToAttach : carnet.getAlumnoCollection()) {
                alumnoCollectionAlumnoToAttach = em.getReference(alumnoCollectionAlumnoToAttach.getClass(), alumnoCollectionAlumnoToAttach.getIdAlumno());
                attachedAlumnoCollection.add(alumnoCollectionAlumnoToAttach);
            }
            carnet.setAlumnoCollection(attachedAlumnoCollection);
            em.persist(carnet);
            for (Alumno alumnoCollectionAlumno : carnet.getAlumnoCollection()) {
                Carnet oldIdCarnetOfAlumnoCollectionAlumno = alumnoCollectionAlumno.getIdCarnet();
                alumnoCollectionAlumno.setIdCarnet(carnet);
                alumnoCollectionAlumno = em.merge(alumnoCollectionAlumno);
                if (oldIdCarnetOfAlumnoCollectionAlumno != null) {
                    oldIdCarnetOfAlumnoCollectionAlumno.getAlumnoCollection().remove(alumnoCollectionAlumno);
                    oldIdCarnetOfAlumnoCollectionAlumno = em.merge(oldIdCarnetOfAlumnoCollectionAlumno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarnet(carnet.getIdCarnet()) != null) {
                throw new PreexistingEntityException("Carnet " + carnet + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carnet carnet) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carnet persistentCarnet = em.find(Carnet.class, carnet.getIdCarnet());
            Collection<Alumno> alumnoCollectionOld = persistentCarnet.getAlumnoCollection();
            Collection<Alumno> alumnoCollectionNew = carnet.getAlumnoCollection();
            Collection<Alumno> attachedAlumnoCollectionNew = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionNewAlumnoToAttach : alumnoCollectionNew) {
                alumnoCollectionNewAlumnoToAttach = em.getReference(alumnoCollectionNewAlumnoToAttach.getClass(), alumnoCollectionNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoCollectionNew.add(alumnoCollectionNewAlumnoToAttach);
            }
            alumnoCollectionNew = attachedAlumnoCollectionNew;
            carnet.setAlumnoCollection(alumnoCollectionNew);
            carnet = em.merge(carnet);
            for (Alumno alumnoCollectionOldAlumno : alumnoCollectionOld) {
                if (!alumnoCollectionNew.contains(alumnoCollectionOldAlumno)) {
                    alumnoCollectionOldAlumno.setIdCarnet(null);
                    alumnoCollectionOldAlumno = em.merge(alumnoCollectionOldAlumno);
                }
            }
            for (Alumno alumnoCollectionNewAlumno : alumnoCollectionNew) {
                if (!alumnoCollectionOld.contains(alumnoCollectionNewAlumno)) {
                    Carnet oldIdCarnetOfAlumnoCollectionNewAlumno = alumnoCollectionNewAlumno.getIdCarnet();
                    alumnoCollectionNewAlumno.setIdCarnet(carnet);
                    alumnoCollectionNewAlumno = em.merge(alumnoCollectionNewAlumno);
                    if (oldIdCarnetOfAlumnoCollectionNewAlumno != null && !oldIdCarnetOfAlumnoCollectionNewAlumno.equals(carnet)) {
                        oldIdCarnetOfAlumnoCollectionNewAlumno.getAlumnoCollection().remove(alumnoCollectionNewAlumno);
                        oldIdCarnetOfAlumnoCollectionNewAlumno = em.merge(oldIdCarnetOfAlumnoCollectionNewAlumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = carnet.getIdCarnet();
                if (findCarnet(id) == null) {
                    throw new NonexistentEntityException("The carnet with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carnet carnet;
            try {
                carnet = em.getReference(Carnet.class, id);
                carnet.getIdCarnet();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carnet with id " + id + " no longer exists.", enfe);
            }
            Collection<Alumno> alumnoCollection = carnet.getAlumnoCollection();
            for (Alumno alumnoCollectionAlumno : alumnoCollection) {
                alumnoCollectionAlumno.setIdCarnet(null);
                alumnoCollectionAlumno = em.merge(alumnoCollectionAlumno);
            }
            em.remove(carnet);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carnet> findCarnetEntities() {
        return findCarnetEntities(true, -1, -1);
    }

    public List<Carnet> findCarnetEntities(int maxResults, int firstResult) {
        return findCarnetEntities(false, maxResults, firstResult);
    }

    private List<Carnet> findCarnetEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carnet.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Carnet findCarnet(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carnet.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarnetCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carnet> rt = cq.from(Carnet.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

 

    
    
}
