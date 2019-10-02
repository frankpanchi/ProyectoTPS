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
import Entidades.Materia;
import Entidades.Pago;
import Entidades.Profesor;
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
public class ProfesorJpaController implements Serializable {

    public ProfesorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesor profesor) throws PreexistingEntityException, Exception {
        if (profesor.getPagoCollection() == null) {
            profesor.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia idMateria = profesor.getIdMateria();
            if (idMateria != null) {
                idMateria = em.getReference(idMateria.getClass(), idMateria.getIdMateria());
                profesor.setIdMateria(idMateria);
            }
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : profesor.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            profesor.setPagoCollection(attachedPagoCollection);
            em.persist(profesor);
            if (idMateria != null) {
                idMateria.getProfesorCollection().add(profesor);
                idMateria = em.merge(idMateria);
            }
            for (Pago pagoCollectionPago : profesor.getPagoCollection()) {
                Profesor oldIdProfesorOfPagoCollectionPago = pagoCollectionPago.getIdProfesor();
                pagoCollectionPago.setIdProfesor(profesor);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldIdProfesorOfPagoCollectionPago != null) {
                    oldIdProfesorOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldIdProfesorOfPagoCollectionPago = em.merge(oldIdProfesorOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfesor(profesor.getIdProfesor()) != null) {
                throw new PreexistingEntityException("Profesor " + profesor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesor profesor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor persistentProfesor = em.find(Profesor.class, profesor.getIdProfesor());
            Materia idMateriaOld = persistentProfesor.getIdMateria();
            Materia idMateriaNew = profesor.getIdMateria();
            Collection<Pago> pagoCollectionOld = persistentProfesor.getPagoCollection();
            Collection<Pago> pagoCollectionNew = profesor.getPagoCollection();
            if (idMateriaNew != null) {
                idMateriaNew = em.getReference(idMateriaNew.getClass(), idMateriaNew.getIdMateria());
                profesor.setIdMateria(idMateriaNew);
            }
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            profesor.setPagoCollection(pagoCollectionNew);
            profesor = em.merge(profesor);
            if (idMateriaOld != null && !idMateriaOld.equals(idMateriaNew)) {
                idMateriaOld.getProfesorCollection().remove(profesor);
                idMateriaOld = em.merge(idMateriaOld);
            }
            if (idMateriaNew != null && !idMateriaNew.equals(idMateriaOld)) {
                idMateriaNew.getProfesorCollection().add(profesor);
                idMateriaNew = em.merge(idMateriaNew);
            }
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    pagoCollectionOldPago.setIdProfesor(null);
                    pagoCollectionOldPago = em.merge(pagoCollectionOldPago);
                }
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Profesor oldIdProfesorOfPagoCollectionNewPago = pagoCollectionNewPago.getIdProfesor();
                    pagoCollectionNewPago.setIdProfesor(profesor);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldIdProfesorOfPagoCollectionNewPago != null && !oldIdProfesorOfPagoCollectionNewPago.equals(profesor)) {
                        oldIdProfesorOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldIdProfesorOfPagoCollectionNewPago = em.merge(oldIdProfesorOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = profesor.getIdProfesor();
                if (findProfesor(id) == null) {
                    throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.");
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
            Profesor profesor;
            try {
                profesor = em.getReference(Profesor.class, id);
                profesor.getIdProfesor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesor with id " + id + " no longer exists.", enfe);
            }
            Materia idMateria = profesor.getIdMateria();
            if (idMateria != null) {
                idMateria.getProfesorCollection().remove(profesor);
                idMateria = em.merge(idMateria);
            }
            Collection<Pago> pagoCollection = profesor.getPagoCollection();
            for (Pago pagoCollectionPago : pagoCollection) {
                pagoCollectionPago.setIdProfesor(null);
                pagoCollectionPago = em.merge(pagoCollectionPago);
            }
            em.remove(profesor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesor> findProfesorEntities() {
        return findProfesorEntities(true, -1, -1);
    }

    public List<Profesor> findProfesorEntities(int maxResults, int firstResult) {
        return findProfesorEntities(false, maxResults, firstResult);
    }

    private List<Profesor> findProfesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesor.class));
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

    public Profesor findProfesor(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesor> rt = cq.from(Profesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
