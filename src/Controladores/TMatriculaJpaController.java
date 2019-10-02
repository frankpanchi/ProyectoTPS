/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.IllegalOrphanException;
import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Matricula;
import Entidades.TMatricula;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PANCHY
 */
public class TMatriculaJpaController implements Serializable {

    public TMatriculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TMatricula TMatricula) throws PreexistingEntityException, Exception {
        if (TMatricula.getMatriculaCollection() == null) {
            TMatricula.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : TMatricula.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            TMatricula.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(TMatricula);
            for (Matricula matriculaCollectionMatricula : TMatricula.getMatriculaCollection()) {
                TMatricula oldIdTmatriculaOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getIdTmatricula();
                matriculaCollectionMatricula.setIdTmatricula(TMatricula);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldIdTmatriculaOfMatriculaCollectionMatricula != null) {
                    oldIdTmatriculaOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldIdTmatriculaOfMatriculaCollectionMatricula = em.merge(oldIdTmatriculaOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTMatricula(TMatricula.getIdTmatricula()) != null) {
                throw new PreexistingEntityException("TMatricula " + TMatricula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TMatricula TMatricula) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TMatricula persistentTMatricula = em.find(TMatricula.class, TMatricula.getIdTmatricula());
            Collection<Matricula> matriculaCollectionOld = persistentTMatricula.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = TMatricula.getMatriculaCollection();
            List<String> illegalOrphanMessages = null;
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Matricula " + matriculaCollectionOldMatricula + " since its idTmatricula field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            TMatricula.setMatriculaCollection(matriculaCollectionNew);
            TMatricula = em.merge(TMatricula);
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    TMatricula oldIdTmatriculaOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getIdTmatricula();
                    matriculaCollectionNewMatricula.setIdTmatricula(TMatricula);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldIdTmatriculaOfMatriculaCollectionNewMatricula != null && !oldIdTmatriculaOfMatriculaCollectionNewMatricula.equals(TMatricula)) {
                        oldIdTmatriculaOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldIdTmatriculaOfMatriculaCollectionNewMatricula = em.merge(oldIdTmatriculaOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = TMatricula.getIdTmatricula();
                if (findTMatricula(id) == null) {
                    throw new NonexistentEntityException("The tMatricula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TMatricula TMatricula;
            try {
                TMatricula = em.getReference(TMatricula.class, id);
                TMatricula.getIdTmatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TMatricula with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Matricula> matriculaCollectionOrphanCheck = TMatricula.getMatriculaCollection();
            for (Matricula matriculaCollectionOrphanCheckMatricula : matriculaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TMatricula (" + TMatricula + ") cannot be destroyed since the Matricula " + matriculaCollectionOrphanCheckMatricula + " in its matriculaCollection field has a non-nullable idTmatricula field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(TMatricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TMatricula> findTMatriculaEntities() {
        return findTMatriculaEntities(true, -1, -1);
    }

    public List<TMatricula> findTMatriculaEntities(int maxResults, int firstResult) {
        return findTMatriculaEntities(false, maxResults, firstResult);
    }

    private List<TMatricula> findTMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TMatricula.class));
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

    public TMatricula findTMatricula(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TMatricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getTMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TMatricula> rt = cq.from(TMatricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
