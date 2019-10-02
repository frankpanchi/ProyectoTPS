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
import Entidades.Contacto;
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
public class ContactoJpaController implements Serializable {

    public ContactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacto contacto) throws PreexistingEntityException, Exception {
        if (contacto.getAlumnoCollection() == null) {
            contacto.setAlumnoCollection(new ArrayList<Alumno>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Alumno> attachedAlumnoCollection = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionAlumnoToAttach : contacto.getAlumnoCollection()) {
                alumnoCollectionAlumnoToAttach = em.getReference(alumnoCollectionAlumnoToAttach.getClass(), alumnoCollectionAlumnoToAttach.getIdAlumno());
                attachedAlumnoCollection.add(alumnoCollectionAlumnoToAttach);
            }
            contacto.setAlumnoCollection(attachedAlumnoCollection);
            em.persist(contacto);
            for (Alumno alumnoCollectionAlumno : contacto.getAlumnoCollection()) {
                Contacto oldIdContactoOfAlumnoCollectionAlumno = alumnoCollectionAlumno.getIdContacto();
                alumnoCollectionAlumno.setIdContacto(contacto);
                alumnoCollectionAlumno = em.merge(alumnoCollectionAlumno);
                if (oldIdContactoOfAlumnoCollectionAlumno != null) {
                    oldIdContactoOfAlumnoCollectionAlumno.getAlumnoCollection().remove(alumnoCollectionAlumno);
                    oldIdContactoOfAlumnoCollectionAlumno = em.merge(oldIdContactoOfAlumnoCollectionAlumno);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findContacto(contacto.getIdContacto()) != null) {
                throw new PreexistingEntityException("Contacto " + contacto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto persistentContacto = em.find(Contacto.class, contacto.getIdContacto());
            Collection<Alumno> alumnoCollectionOld = persistentContacto.getAlumnoCollection();
            Collection<Alumno> alumnoCollectionNew = contacto.getAlumnoCollection();
            Collection<Alumno> attachedAlumnoCollectionNew = new ArrayList<Alumno>();
            for (Alumno alumnoCollectionNewAlumnoToAttach : alumnoCollectionNew) {
                alumnoCollectionNewAlumnoToAttach = em.getReference(alumnoCollectionNewAlumnoToAttach.getClass(), alumnoCollectionNewAlumnoToAttach.getIdAlumno());
                attachedAlumnoCollectionNew.add(alumnoCollectionNewAlumnoToAttach);
            }
            alumnoCollectionNew = attachedAlumnoCollectionNew;
            contacto.setAlumnoCollection(alumnoCollectionNew);
            contacto = em.merge(contacto);
            for (Alumno alumnoCollectionOldAlumno : alumnoCollectionOld) {
                if (!alumnoCollectionNew.contains(alumnoCollectionOldAlumno)) {
                    alumnoCollectionOldAlumno.setIdContacto(null);
                    alumnoCollectionOldAlumno = em.merge(alumnoCollectionOldAlumno);
                }
            }
            for (Alumno alumnoCollectionNewAlumno : alumnoCollectionNew) {
                if (!alumnoCollectionOld.contains(alumnoCollectionNewAlumno)) {
                    Contacto oldIdContactoOfAlumnoCollectionNewAlumno = alumnoCollectionNewAlumno.getIdContacto();
                    alumnoCollectionNewAlumno.setIdContacto(contacto);
                    alumnoCollectionNewAlumno = em.merge(alumnoCollectionNewAlumno);
                    if (oldIdContactoOfAlumnoCollectionNewAlumno != null && !oldIdContactoOfAlumnoCollectionNewAlumno.equals(contacto)) {
                        oldIdContactoOfAlumnoCollectionNewAlumno.getAlumnoCollection().remove(alumnoCollectionNewAlumno);
                        oldIdContactoOfAlumnoCollectionNewAlumno = em.merge(oldIdContactoOfAlumnoCollectionNewAlumno);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = contacto.getIdContacto();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getIdContacto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            Collection<Alumno> alumnoCollection = contacto.getAlumnoCollection();
            for (Alumno alumnoCollectionAlumno : alumnoCollection) {
                alumnoCollectionAlumno.setIdContacto(null);
                alumnoCollectionAlumno = em.merge(alumnoCollectionAlumno);
            }
            em.remove(contacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
