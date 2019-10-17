/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Pago;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Profesor;
import Entidades.Rol;
import Entidades.Salario;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JComboBox;

/**
 *
 * @author PANCHY
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController() {
      
       this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesor idProfesor = pago.getIdProfesor();
            if (idProfesor != null) {
                idProfesor = em.getReference(idProfesor.getClass(), idProfesor.getIdProfesor());
                pago.setIdProfesor(idProfesor);
            }
            Salario idSalario = pago.getIdSalario();
            if (idSalario != null) {
                idSalario = em.getReference(idSalario.getClass(), idSalario.getIdSalario());
                pago.setIdSalario(idSalario);
            }
            em.persist(pago);
            if (idProfesor != null) {
                idProfesor.getPagoCollection().add(pago);
                idProfesor = em.merge(idProfesor);
            }
            if (idSalario != null) {
                idSalario.getPagoCollection().add(pago);
                idSalario = em.merge(idSalario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPago(pago.getIdPago()) != null) {
                throw new PreexistingEntityException("Pago " + pago + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getIdPago());
            Profesor idProfesorOld = persistentPago.getIdProfesor();
            Profesor idProfesorNew = pago.getIdProfesor();
            Salario idSalarioOld = persistentPago.getIdSalario();
            Salario idSalarioNew = pago.getIdSalario();
            if (idProfesorNew != null) {
                idProfesorNew = em.getReference(idProfesorNew.getClass(), idProfesorNew.getIdProfesor());
                pago.setIdProfesor(idProfesorNew);
            }
            if (idSalarioNew != null) {
                idSalarioNew = em.getReference(idSalarioNew.getClass(), idSalarioNew.getIdSalario());
                pago.setIdSalario(idSalarioNew);
            }
            pago = em.merge(pago);
            if (idProfesorOld != null && !idProfesorOld.equals(idProfesorNew)) {
                idProfesorOld.getPagoCollection().remove(pago);
                idProfesorOld = em.merge(idProfesorOld);
            }
            if (idProfesorNew != null && !idProfesorNew.equals(idProfesorOld)) {
                idProfesorNew.getPagoCollection().add(pago);
                idProfesorNew = em.merge(idProfesorNew);
            }
            if (idSalarioOld != null && !idSalarioOld.equals(idSalarioNew)) {
                idSalarioOld.getPagoCollection().remove(pago);
                idSalarioOld = em.merge(idSalarioOld);
            }
            if (idSalarioNew != null && !idSalarioNew.equals(idSalarioOld)) {
                idSalarioNew.getPagoCollection().add(pago);
                idSalarioNew = em.merge(idSalarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = pago.getIdPago();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Profesor idProfesor = pago.getIdProfesor();
            if (idProfesor != null) {
                idProfesor.getPagoCollection().remove(pago);
                idProfesor = em.merge(idProfesor);
            }
            Salario idSalario = pago.getIdSalario();
            if (idSalario != null) {
                idSalario.getPagoCollection().remove(pago);
                idSalario = em.merge(idSalario);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      public void ComboSalario(JComboBox<Salario> cbsalario)
            
    {
      try {  
             SalarioJpaController Csalario = new   SalarioJpaController();
             
             List<Salario> Lsal = Csalario.findSalarioEntities();
             
              for (int i = 0; i < Lsal.size() ; i++) {
                  cbsalario.addItem(
                 new Salario(
                   Lsal.get(i).getIdSalario(),
                   Lsal.get(i).getSalario().toString()
                             
                  )           
                  
                  );
              
              
              }
         } catch( Exception e)
            {
             
            } 
    }
    
}
