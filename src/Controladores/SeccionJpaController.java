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
import Entidades.Grado;
import Entidades.Seccion;
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
public class SeccionJpaController implements Serializable {

    public SeccionJpaController() {
       this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Seccion seccion) throws PreexistingEntityException, Exception {
        if (seccion.getGradoCollection() == null) {
            seccion.setGradoCollection(new ArrayList<Grado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Grado> attachedGradoCollection = new ArrayList<Grado>();
            for (Grado gradoCollectionGradoToAttach : seccion.getGradoCollection()) {
                gradoCollectionGradoToAttach = em.getReference(gradoCollectionGradoToAttach.getClass(), gradoCollectionGradoToAttach.getIdGrado());
                attachedGradoCollection.add(gradoCollectionGradoToAttach);
            }
            seccion.setGradoCollection(attachedGradoCollection);
            em.persist(seccion);
            for (Grado gradoCollectionGrado : seccion.getGradoCollection()) {
                Seccion oldIdSeccionOfGradoCollectionGrado = gradoCollectionGrado.getIdSeccion();
                gradoCollectionGrado.setIdSeccion(seccion);
                gradoCollectionGrado = em.merge(gradoCollectionGrado);
                if (oldIdSeccionOfGradoCollectionGrado != null) {
                    oldIdSeccionOfGradoCollectionGrado.getGradoCollection().remove(gradoCollectionGrado);
                    oldIdSeccionOfGradoCollectionGrado = em.merge(oldIdSeccionOfGradoCollectionGrado);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeccion(seccion.getIdSeccion()) != null) {
                throw new PreexistingEntityException("Seccion " + seccion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Seccion seccion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion persistentSeccion = em.find(Seccion.class, seccion.getIdSeccion());
            Collection<Grado> gradoCollectionOld = persistentSeccion.getGradoCollection();
            Collection<Grado> gradoCollectionNew = seccion.getGradoCollection();
            Collection<Grado> attachedGradoCollectionNew = new ArrayList<Grado>();
            for (Grado gradoCollectionNewGradoToAttach : gradoCollectionNew) {
                gradoCollectionNewGradoToAttach = em.getReference(gradoCollectionNewGradoToAttach.getClass(), gradoCollectionNewGradoToAttach.getIdGrado());
                attachedGradoCollectionNew.add(gradoCollectionNewGradoToAttach);
            }
            gradoCollectionNew = attachedGradoCollectionNew;
            seccion.setGradoCollection(gradoCollectionNew);
            seccion = em.merge(seccion);
            for (Grado gradoCollectionOldGrado : gradoCollectionOld) {
                if (!gradoCollectionNew.contains(gradoCollectionOldGrado)) {
                    gradoCollectionOldGrado.setIdSeccion(null);
                    gradoCollectionOldGrado = em.merge(gradoCollectionOldGrado);
                }
            }
            for (Grado gradoCollectionNewGrado : gradoCollectionNew) {
                if (!gradoCollectionOld.contains(gradoCollectionNewGrado)) {
                    Seccion oldIdSeccionOfGradoCollectionNewGrado = gradoCollectionNewGrado.getIdSeccion();
                    gradoCollectionNewGrado.setIdSeccion(seccion);
                    gradoCollectionNewGrado = em.merge(gradoCollectionNewGrado);
                    if (oldIdSeccionOfGradoCollectionNewGrado != null && !oldIdSeccionOfGradoCollectionNewGrado.equals(seccion)) {
                        oldIdSeccionOfGradoCollectionNewGrado.getGradoCollection().remove(gradoCollectionNewGrado);
                        oldIdSeccionOfGradoCollectionNewGrado = em.merge(oldIdSeccionOfGradoCollectionNewGrado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = seccion.getIdSeccion();
                if (findSeccion(id) == null) {
                    throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.");
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
            Seccion seccion;
            try {
                seccion = em.getReference(Seccion.class, id);
                seccion.getIdSeccion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The seccion with id " + id + " no longer exists.", enfe);
            }
            Collection<Grado> gradoCollection = seccion.getGradoCollection();
            for (Grado gradoCollectionGrado : gradoCollection) {
                gradoCollectionGrado.setIdSeccion(null);
                gradoCollectionGrado = em.merge(gradoCollectionGrado);
            }
            em.remove(seccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Seccion> findSeccionEntities() {
        return findSeccionEntities(true, -1, -1);
    }

    public List<Seccion> findSeccionEntities(int maxResults, int firstResult) {
        return findSeccionEntities(false, maxResults, firstResult);
    }

    private List<Seccion> findSeccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Seccion.class));
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

    public Seccion findSeccion(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Seccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Seccion> rt = cq.from(Seccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
