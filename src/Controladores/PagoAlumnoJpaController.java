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
import Entidades.Matricula;
import Entidades.PagoAlumno;
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
public class PagoAlumnoJpaController implements Serializable {

    public PagoAlumnoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PagoAlumno pagoAlumno) throws PreexistingEntityException, Exception {
        if (pagoAlumno.getMatriculaCollection() == null) {
            pagoAlumno.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : pagoAlumno.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            pagoAlumno.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(pagoAlumno);
            for (Matricula matriculaCollectionMatricula : pagoAlumno.getMatriculaCollection()) {
                PagoAlumno oldIdPagoalumnoOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getIdPagoalumno();
                matriculaCollectionMatricula.setIdPagoalumno(pagoAlumno);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldIdPagoalumnoOfMatriculaCollectionMatricula != null) {
                    oldIdPagoalumnoOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldIdPagoalumnoOfMatriculaCollectionMatricula = em.merge(oldIdPagoalumnoOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPagoAlumno(pagoAlumno.getIdPagoalumno()) != null) {
                throw new PreexistingEntityException("PagoAlumno " + pagoAlumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PagoAlumno pagoAlumno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagoAlumno persistentPagoAlumno = em.find(PagoAlumno.class, pagoAlumno.getIdPagoalumno());
            Collection<Matricula> matriculaCollectionOld = persistentPagoAlumno.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = pagoAlumno.getMatriculaCollection();
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            pagoAlumno.setMatriculaCollection(matriculaCollectionNew);
            pagoAlumno = em.merge(pagoAlumno);
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    matriculaCollectionOldMatricula.setIdPagoalumno(null);
                    matriculaCollectionOldMatricula = em.merge(matriculaCollectionOldMatricula);
                }
            }
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    PagoAlumno oldIdPagoalumnoOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getIdPagoalumno();
                    matriculaCollectionNewMatricula.setIdPagoalumno(pagoAlumno);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldIdPagoalumnoOfMatriculaCollectionNewMatricula != null && !oldIdPagoalumnoOfMatriculaCollectionNewMatricula.equals(pagoAlumno)) {
                        oldIdPagoalumnoOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldIdPagoalumnoOfMatriculaCollectionNewMatricula = em.merge(oldIdPagoalumnoOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = pagoAlumno.getIdPagoalumno();
                if (findPagoAlumno(id) == null) {
                    throw new NonexistentEntityException("The pagoAlumno with id " + id + " no longer exists.");
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
            PagoAlumno pagoAlumno;
            try {
                pagoAlumno = em.getReference(PagoAlumno.class, id);
                pagoAlumno.getIdPagoalumno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagoAlumno with id " + id + " no longer exists.", enfe);
            }
            Collection<Matricula> matriculaCollection = pagoAlumno.getMatriculaCollection();
            for (Matricula matriculaCollectionMatricula : matriculaCollection) {
                matriculaCollectionMatricula.setIdPagoalumno(null);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
            }
            em.remove(pagoAlumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PagoAlumno> findPagoAlumnoEntities() {
        return findPagoAlumnoEntities(true, -1, -1);
    }

    public List<PagoAlumno> findPagoAlumnoEntities(int maxResults, int firstResult) {
        return findPagoAlumnoEntities(false, maxResults, firstResult);
    }

    private List<PagoAlumno> findPagoAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PagoAlumno.class));
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

    public PagoAlumno findPagoAlumno(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PagoAlumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PagoAlumno> rt = cq.from(PagoAlumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
