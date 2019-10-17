/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Controladores.exceptions.NonexistentEntityException;
import Controladores.exceptions.PreexistingEntityException;
import Entidades.Alumno;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Carnet;
import Entidades.Contacto;
import Entidades.Matricula;
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
public class AlumnoJpaController implements Serializable {
Carnet ca = new Carnet();
    public AlumnoJpaController() {
       this.emf = Persistence.createEntityManagerFactory("ColegioTPSPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Alumno alumno) throws PreexistingEntityException, Exception {
        if (alumno.getMatriculaCollection() == null) {
            alumno.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carnet idCarnet = alumno.getIdCarnet();
            if (idCarnet != null) {
                idCarnet = em.getReference(idCarnet.getClass(), idCarnet.getIdCarnet());
                alumno.setIdCarnet(idCarnet);
            }
            Contacto idContacto = alumno.getIdContacto();
            if (idContacto != null) {
                idContacto = em.getReference(idContacto.getClass(), idContacto.getIdContacto());
                alumno.setIdContacto(idContacto);
            }
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : alumno.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            alumno.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(alumno);
            if (idCarnet != null) {
                idCarnet.getAlumnoCollection().add(alumno);
                idCarnet = em.merge(idCarnet);
            }
            if (idContacto != null) {
                idContacto.getAlumnoCollection().add(alumno);
                idContacto = em.merge(idContacto);
            }
            for (Matricula matriculaCollectionMatricula : alumno.getMatriculaCollection()) {
                Alumno oldIdAlumnoOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getIdAlumno();
                matriculaCollectionMatricula.setIdAlumno(alumno);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldIdAlumnoOfMatriculaCollectionMatricula != null) {
                    oldIdAlumnoOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldIdAlumnoOfMatriculaCollectionMatricula = em.merge(oldIdAlumnoOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlumno(alumno.getIdAlumno()) != null) {
                throw new PreexistingEntityException("Alumno " + alumno + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Alumno alumno) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno persistentAlumno = em.find(Alumno.class, alumno.getIdAlumno());
            Carnet idCarnetOld = persistentAlumno.getIdCarnet();
            Carnet idCarnetNew = alumno.getIdCarnet();
            Contacto idContactoOld = persistentAlumno.getIdContacto();
            Contacto idContactoNew = alumno.getIdContacto();
            Collection<Matricula> matriculaCollectionOld = persistentAlumno.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = alumno.getMatriculaCollection();
            if (idCarnetNew != null) {
                idCarnetNew = em.getReference(idCarnetNew.getClass(), idCarnetNew.getIdCarnet());
                alumno.setIdCarnet(idCarnetNew);
            }
            if (idContactoNew != null) {
                idContactoNew = em.getReference(idContactoNew.getClass(), idContactoNew.getIdContacto());
                alumno.setIdContacto(idContactoNew);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            alumno.setMatriculaCollection(matriculaCollectionNew);
            alumno = em.merge(alumno);
            if (idCarnetOld != null && !idCarnetOld.equals(idCarnetNew)) {
                idCarnetOld.getAlumnoCollection().remove(alumno);
                idCarnetOld = em.merge(idCarnetOld);
            }
            if (idCarnetNew != null && !idCarnetNew.equals(idCarnetOld)) {
                idCarnetNew.getAlumnoCollection().add(alumno);
                idCarnetNew = em.merge(idCarnetNew);
            }
            if (idContactoOld != null && !idContactoOld.equals(idContactoNew)) {
                idContactoOld.getAlumnoCollection().remove(alumno);
                idContactoOld = em.merge(idContactoOld);
            }
            if (idContactoNew != null && !idContactoNew.equals(idContactoOld)) {
                idContactoNew.getAlumnoCollection().add(alumno);
                idContactoNew = em.merge(idContactoNew);
            }
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    matriculaCollectionOldMatricula.setIdAlumno(null);
                    matriculaCollectionOldMatricula = em.merge(matriculaCollectionOldMatricula);
                }
            }
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    Alumno oldIdAlumnoOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getIdAlumno();
                    matriculaCollectionNewMatricula.setIdAlumno(alumno);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldIdAlumnoOfMatriculaCollectionNewMatricula != null && !oldIdAlumnoOfMatriculaCollectionNewMatricula.equals(alumno)) {
                        oldIdAlumnoOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldIdAlumnoOfMatriculaCollectionNewMatricula = em.merge(oldIdAlumnoOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = alumno.getIdAlumno();
                if (findAlumno(id) == null) {
                    throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.");
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
            Alumno alumno;
            try {
                alumno = em.getReference(Alumno.class, id);
                alumno.getIdAlumno();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumno with id " + id + " no longer exists.", enfe);
            }
            Carnet idCarnet = alumno.getIdCarnet();
            if (idCarnet != null) {
                idCarnet.getAlumnoCollection().remove(alumno);
                idCarnet = em.merge(idCarnet);
            }
            Contacto idContacto = alumno.getIdContacto();
            if (idContacto != null) {
                idContacto.getAlumnoCollection().remove(alumno);
                idContacto = em.merge(idContacto);
            }
            Collection<Matricula> matriculaCollection = alumno.getMatriculaCollection();
            for (Matricula matriculaCollectionMatricula : matriculaCollection) {
                matriculaCollectionMatricula.setIdAlumno(null);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
            }
            em.remove(alumno);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Alumno> findAlumnoEntities() {
        return findAlumnoEntities(true, -1, -1);
    }

    public List<Alumno> findAlumnoEntities(int maxResults, int firstResult) {
        return findAlumnoEntities(false, maxResults, firstResult);
    }

    private List<Alumno> findAlumnoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Alumno.class));
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

    public Alumno findAlumno(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Alumno.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlumnoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Alumno> rt = cq.from(Alumno.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
     public void ComboCarnet(JComboBox<Entidades.Carnet> cbcarnet)
            
    {
      try {  
             CarnetJpaController Ccarnet = new   CarnetJpaController();
             
             List<Carnet> Lcarnet = Ccarnet.findCarnetEntities();
             
              for (int i = 0; i < Lcarnet.size() ; i++) {
                  cbcarnet.addItem(
                  new Carnet(
                   Lcarnet.get(i).getIdCarnet(),
                   Lcarnet.get(i).getCarnet()
                             
                  )           
                  
                  );
              
              
              }
         } catch( Exception e)
            {
             
            } 
    }
     
      public void ComboContacto(JComboBox<Contacto> cbcontacto)
            
    {
      try {  
             ContactoJpaController Ccontacto = new   ContactoJpaController();
             
             List<Contacto> Lcontacto = Ccontacto.findContactoEntities();
             
              for (int i = 0; i < Lcontacto.size() ; i++) {
                  cbcontacto.addItem(
                   new Contacto(
                   Lcontacto.get(i).getIdContacto(),
                   Lcontacto.get(i).getNombre(),
                   Lcontacto.get(i).getApellido(),
                   Lcontacto.get(i).getDui(),
                   Lcontacto.get(i).getDireccion(),
                   Lcontacto.get(i).getTelefono()            
                  )           
                  
                  );
              
              
              }
         } catch( Exception e)
            {
             
            } 
    }
}
