/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Materia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Profesor;
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
public class MateriaJpaController implements Serializable {

    public MateriaJpaController() {
       this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) throws PreexistingEntityException, Exception {
        if (materia.getProfesorCollection() == null) {
            materia.setProfesorCollection(new ArrayList<Profesor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profesor> attachedProfesorCollection = new ArrayList<Profesor>();
            for (Profesor profesorCollectionProfesorToAttach : materia.getProfesorCollection()) {
                profesorCollectionProfesorToAttach = em.getReference(profesorCollectionProfesorToAttach.getClass(), profesorCollectionProfesorToAttach.getIdProfesor());
                attachedProfesorCollection.add(profesorCollectionProfesorToAttach);
            }
            materia.setProfesorCollection(attachedProfesorCollection);
            em.persist(materia);
            for (Profesor profesorCollectionProfesor : materia.getProfesorCollection()) {
                Materia oldIdMateriaOfProfesorCollectionProfesor = profesorCollectionProfesor.getIdMateria();
                profesorCollectionProfesor.setIdMateria(materia);
                profesorCollectionProfesor = em.merge(profesorCollectionProfesor);
                if (oldIdMateriaOfProfesorCollectionProfesor != null) {
                    oldIdMateriaOfProfesorCollectionProfesor.getProfesorCollection().remove(profesorCollectionProfesor);
                    oldIdMateriaOfProfesorCollectionProfesor = em.merge(oldIdMateriaOfProfesorCollectionProfesor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMateria(materia.getIdMateria()) != null) {
                throw new PreexistingEntityException("Materia " + materia + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            Collection<Profesor> profesorCollectionOld = persistentMateria.getProfesorCollection();
            Collection<Profesor> profesorCollectionNew = materia.getProfesorCollection();
            Collection<Profesor> attachedProfesorCollectionNew = new ArrayList<Profesor>();
            for (Profesor profesorCollectionNewProfesorToAttach : profesorCollectionNew) {
                profesorCollectionNewProfesorToAttach = em.getReference(profesorCollectionNewProfesorToAttach.getClass(), profesorCollectionNewProfesorToAttach.getIdProfesor());
                attachedProfesorCollectionNew.add(profesorCollectionNewProfesorToAttach);
            }
            profesorCollectionNew = attachedProfesorCollectionNew;
            materia.setProfesorCollection(profesorCollectionNew);
            materia = em.merge(materia);
            for (Profesor profesorCollectionOldProfesor : profesorCollectionOld) {
                if (!profesorCollectionNew.contains(profesorCollectionOldProfesor)) {
                    profesorCollectionOldProfesor.setIdMateria(null);
                    profesorCollectionOldProfesor = em.merge(profesorCollectionOldProfesor);
                }
            }
            for (Profesor profesorCollectionNewProfesor : profesorCollectionNew) {
                if (!profesorCollectionOld.contains(profesorCollectionNewProfesor)) {
                    Materia oldIdMateriaOfProfesorCollectionNewProfesor = profesorCollectionNewProfesor.getIdMateria();
                    profesorCollectionNewProfesor.setIdMateria(materia);
                    profesorCollectionNewProfesor = em.merge(profesorCollectionNewProfesor);
                    if (oldIdMateriaOfProfesorCollectionNewProfesor != null && !oldIdMateriaOfProfesorCollectionNewProfesor.equals(materia)) {
                        oldIdMateriaOfProfesorCollectionNewProfesor.getProfesorCollection().remove(profesorCollectionNewProfesor);
                        oldIdMateriaOfProfesorCollectionNewProfesor = em.merge(oldIdMateriaOfProfesorCollectionNewProfesor);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            Collection<Profesor> profesorCollection = materia.getProfesorCollection();
            for (Profesor profesorCollectionProfesor : profesorCollection) {
                profesorCollectionProfesor.setIdMateria(null);
                profesorCollectionProfesor = em.merge(profesorCollectionProfesor);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
