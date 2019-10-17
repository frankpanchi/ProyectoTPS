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
import Entidades.Pago;
import Entidades.Salario;
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
public class SalarioJpaController implements Serializable {

    public SalarioJpaController() {
       
       this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Salario salario) throws PreexistingEntityException, Exception {
        if (salario.getPagoCollection() == null) {
            salario.setPagoCollection(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Pago> attachedPagoCollection = new ArrayList<Pago>();
            for (Pago pagoCollectionPagoToAttach : salario.getPagoCollection()) {
                pagoCollectionPagoToAttach = em.getReference(pagoCollectionPagoToAttach.getClass(), pagoCollectionPagoToAttach.getIdPago());
                attachedPagoCollection.add(pagoCollectionPagoToAttach);
            }
            salario.setPagoCollection(attachedPagoCollection);
            em.persist(salario);
            for (Pago pagoCollectionPago : salario.getPagoCollection()) {
                Salario oldIdSalarioOfPagoCollectionPago = pagoCollectionPago.getIdSalario();
                pagoCollectionPago.setIdSalario(salario);
                pagoCollectionPago = em.merge(pagoCollectionPago);
                if (oldIdSalarioOfPagoCollectionPago != null) {
                    oldIdSalarioOfPagoCollectionPago.getPagoCollection().remove(pagoCollectionPago);
                    oldIdSalarioOfPagoCollectionPago = em.merge(oldIdSalarioOfPagoCollectionPago);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSalario(salario.getIdSalario()) != null) {
                throw new PreexistingEntityException("Salario " + salario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Salario salario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Salario persistentSalario = em.find(Salario.class, salario.getIdSalario());
            Collection<Pago> pagoCollectionOld = persistentSalario.getPagoCollection();
            Collection<Pago> pagoCollectionNew = salario.getPagoCollection();
            Collection<Pago> attachedPagoCollectionNew = new ArrayList<Pago>();
            for (Pago pagoCollectionNewPagoToAttach : pagoCollectionNew) {
                pagoCollectionNewPagoToAttach = em.getReference(pagoCollectionNewPagoToAttach.getClass(), pagoCollectionNewPagoToAttach.getIdPago());
                attachedPagoCollectionNew.add(pagoCollectionNewPagoToAttach);
            }
            pagoCollectionNew = attachedPagoCollectionNew;
            salario.setPagoCollection(pagoCollectionNew);
            salario = em.merge(salario);
            for (Pago pagoCollectionOldPago : pagoCollectionOld) {
                if (!pagoCollectionNew.contains(pagoCollectionOldPago)) {
                    pagoCollectionOldPago.setIdSalario(null);
                    pagoCollectionOldPago = em.merge(pagoCollectionOldPago);
                }
            }
            for (Pago pagoCollectionNewPago : pagoCollectionNew) {
                if (!pagoCollectionOld.contains(pagoCollectionNewPago)) {
                    Salario oldIdSalarioOfPagoCollectionNewPago = pagoCollectionNewPago.getIdSalario();
                    pagoCollectionNewPago.setIdSalario(salario);
                    pagoCollectionNewPago = em.merge(pagoCollectionNewPago);
                    if (oldIdSalarioOfPagoCollectionNewPago != null && !oldIdSalarioOfPagoCollectionNewPago.equals(salario)) {
                        oldIdSalarioOfPagoCollectionNewPago.getPagoCollection().remove(pagoCollectionNewPago);
                        oldIdSalarioOfPagoCollectionNewPago = em.merge(oldIdSalarioOfPagoCollectionNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = salario.getIdSalario();
                if (findSalario(id) == null) {
                    throw new NonexistentEntityException("The salario with id " + id + " no longer exists.");
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
            Salario salario;
            try {
                salario = em.getReference(Salario.class, id);
                salario.getIdSalario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The salario with id " + id + " no longer exists.", enfe);
            }
            Collection<Pago> pagoCollection = salario.getPagoCollection();
            for (Pago pagoCollectionPago : pagoCollection) {
                pagoCollectionPago.setIdSalario(null);
                pagoCollectionPago = em.merge(pagoCollectionPago);
            }
            em.remove(salario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Salario> findSalarioEntities() {
        return findSalarioEntities(true, -1, -1);
    }

    public List<Salario> findSalarioEntities(int maxResults, int firstResult) {
        return findSalarioEntities(false, maxResults, firstResult);
    }

    private List<Salario> findSalarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Salario.class));
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

    public Salario findSalario(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Salario.class, id);
        } finally {
            em.close();
        }
    }

    public int getSalarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Salario> rt = cq.from(Salario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
