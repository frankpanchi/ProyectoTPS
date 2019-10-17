/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Grado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Seccion;
import Entidades.Matricula;
import Entidades.Rol;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JComboBox;

/**
 *
 * @author PANCHY
 */
public class GradoJpaController implements Serializable {

    public GradoJpaController() {
      this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grado grado) throws PreexistingEntityException, Exception {
        if (grado.getMatriculaCollection() == null) {
            grado.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Seccion idSeccion = grado.getIdSeccion();
            if (idSeccion != null) {
                idSeccion = em.getReference(idSeccion.getClass(), idSeccion.getIdSeccion());
                grado.setIdSeccion(idSeccion);
            }
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : grado.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            grado.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(grado);
            if (idSeccion != null) {
                idSeccion.getGradoCollection().add(grado);
                idSeccion = em.merge(idSeccion);
            }
            for (Matricula matriculaCollectionMatricula : grado.getMatriculaCollection()) {
                Grado oldIdGradoOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getIdGrado();
                matriculaCollectionMatricula.setIdGrado(grado);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldIdGradoOfMatriculaCollectionMatricula != null) {
                    oldIdGradoOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldIdGradoOfMatriculaCollectionMatricula = em.merge(oldIdGradoOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrado(grado.getIdGrado()) != null) {
                throw new PreexistingEntityException("Grado " + grado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Grado grado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grado persistentGrado = em.find(Grado.class, grado.getIdGrado());
            Seccion idSeccionOld = persistentGrado.getIdSeccion();
            Seccion idSeccionNew = grado.getIdSeccion();
            Collection<Matricula> matriculaCollectionOld = persistentGrado.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = grado.getMatriculaCollection();
            if (idSeccionNew != null) {
                idSeccionNew = em.getReference(idSeccionNew.getClass(), idSeccionNew.getIdSeccion());
                grado.setIdSeccion(idSeccionNew);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            grado.setMatriculaCollection(matriculaCollectionNew);
            grado = em.merge(grado);
            if (idSeccionOld != null && !idSeccionOld.equals(idSeccionNew)) {
                idSeccionOld.getGradoCollection().remove(grado);
                idSeccionOld = em.merge(idSeccionOld);
            }
            if (idSeccionNew != null && !idSeccionNew.equals(idSeccionOld)) {
                idSeccionNew.getGradoCollection().add(grado);
                idSeccionNew = em.merge(idSeccionNew);
            }
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    matriculaCollectionOldMatricula.setIdGrado(null);
                    matriculaCollectionOldMatricula = em.merge(matriculaCollectionOldMatricula);
                }
            }
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    Grado oldIdGradoOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getIdGrado();
                    matriculaCollectionNewMatricula.setIdGrado(grado);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldIdGradoOfMatriculaCollectionNewMatricula != null && !oldIdGradoOfMatriculaCollectionNewMatricula.equals(grado)) {
                        oldIdGradoOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldIdGradoOfMatriculaCollectionNewMatricula = em.merge(oldIdGradoOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = grado.getIdGrado();
                if (findGrado(id) == null) {
                    throw new NonexistentEntityException("The grado with id " + id + " no longer exists.");
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
            Grado grado;
            try {
                grado = em.getReference(Grado.class, id);
                grado.getIdGrado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grado with id " + id + " no longer exists.", enfe);
            }
            Seccion idSeccion = grado.getIdSeccion();
            if (idSeccion != null) {
                idSeccion.getGradoCollection().remove(grado);
                idSeccion = em.merge(idSeccion);
            }
            Collection<Matricula> matriculaCollection = grado.getMatriculaCollection();
            for (Matricula matriculaCollectionMatricula : matriculaCollection) {
                matriculaCollectionMatricula.setIdGrado(null);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
            }
            em.remove(grado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Grado> findGradoEntities() {
        return findGradoEntities(true, -1, -1);
    }

    public List<Grado> findGradoEntities(int maxResults, int firstResult) {
        return findGradoEntities(false, maxResults, firstResult);
    }

    private List<Grado> findGradoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grado.class));
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

    public Grado findGrado(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grado.class, id);
        } finally {
            em.close();
        }
    }

    public int getGradoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grado> rt = cq.from(Grado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
      public void ComboRol(JComboBox<Entidades.Seccion> cbrol)
            
    {
      try {  
             SeccionJpaController Cseccion = new   SeccionJpaController();
             
             List<Seccion> Lsec = Cseccion.findSeccionEntities();
             
              for (int i = 0; i < Lsec.size() ; i++) {
                  cbrol.addItem(
                 new Seccion(
                   Lsec.get(i).getIdSeccion(),
                   Lsec.get(i).getSeccion()
                             
                  )           
                  
                  );
              
              
              }
         } catch( Exception e)
            {
             
            } 
    }
    
}
