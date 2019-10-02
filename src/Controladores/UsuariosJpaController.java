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
import Entidades.Rol;
import Entidades.Matricula;
import Entidades.Usuarios;
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
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getMatriculaCollection() == null) {
            usuarios.setMatriculaCollection(new ArrayList<Matricula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rol idRol = usuarios.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getIdRol());
                usuarios.setIdRol(idRol);
            }
            Collection<Matricula> attachedMatriculaCollection = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionMatriculaToAttach : usuarios.getMatriculaCollection()) {
                matriculaCollectionMatriculaToAttach = em.getReference(matriculaCollectionMatriculaToAttach.getClass(), matriculaCollectionMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollection.add(matriculaCollectionMatriculaToAttach);
            }
            usuarios.setMatriculaCollection(attachedMatriculaCollection);
            em.persist(usuarios);
            if (idRol != null) {
                idRol.getUsuariosCollection().add(usuarios);
                idRol = em.merge(idRol);
            }
            for (Matricula matriculaCollectionMatricula : usuarios.getMatriculaCollection()) {
                Usuarios oldIdUsuarioOfMatriculaCollectionMatricula = matriculaCollectionMatricula.getIdUsuario();
                matriculaCollectionMatricula.setIdUsuario(usuarios);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
                if (oldIdUsuarioOfMatriculaCollectionMatricula != null) {
                    oldIdUsuarioOfMatriculaCollectionMatricula.getMatriculaCollection().remove(matriculaCollectionMatricula);
                    oldIdUsuarioOfMatriculaCollectionMatricula = em.merge(oldIdUsuarioOfMatriculaCollectionMatricula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getIdUsuario()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdUsuario());
            Rol idRolOld = persistentUsuarios.getIdRol();
            Rol idRolNew = usuarios.getIdRol();
            Collection<Matricula> matriculaCollectionOld = persistentUsuarios.getMatriculaCollection();
            Collection<Matricula> matriculaCollectionNew = usuarios.getMatriculaCollection();
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getIdRol());
                usuarios.setIdRol(idRolNew);
            }
            Collection<Matricula> attachedMatriculaCollectionNew = new ArrayList<Matricula>();
            for (Matricula matriculaCollectionNewMatriculaToAttach : matriculaCollectionNew) {
                matriculaCollectionNewMatriculaToAttach = em.getReference(matriculaCollectionNewMatriculaToAttach.getClass(), matriculaCollectionNewMatriculaToAttach.getIdMatricula());
                attachedMatriculaCollectionNew.add(matriculaCollectionNewMatriculaToAttach);
            }
            matriculaCollectionNew = attachedMatriculaCollectionNew;
            usuarios.setMatriculaCollection(matriculaCollectionNew);
            usuarios = em.merge(usuarios);
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsuariosCollection().remove(usuarios);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsuariosCollection().add(usuarios);
                idRolNew = em.merge(idRolNew);
            }
            for (Matricula matriculaCollectionOldMatricula : matriculaCollectionOld) {
                if (!matriculaCollectionNew.contains(matriculaCollectionOldMatricula)) {
                    matriculaCollectionOldMatricula.setIdUsuario(null);
                    matriculaCollectionOldMatricula = em.merge(matriculaCollectionOldMatricula);
                }
            }
            for (Matricula matriculaCollectionNewMatricula : matriculaCollectionNew) {
                if (!matriculaCollectionOld.contains(matriculaCollectionNewMatricula)) {
                    Usuarios oldIdUsuarioOfMatriculaCollectionNewMatricula = matriculaCollectionNewMatricula.getIdUsuario();
                    matriculaCollectionNewMatricula.setIdUsuario(usuarios);
                    matriculaCollectionNewMatricula = em.merge(matriculaCollectionNewMatricula);
                    if (oldIdUsuarioOfMatriculaCollectionNewMatricula != null && !oldIdUsuarioOfMatriculaCollectionNewMatricula.equals(usuarios)) {
                        oldIdUsuarioOfMatriculaCollectionNewMatricula.getMatriculaCollection().remove(matriculaCollectionNewMatricula);
                        oldIdUsuarioOfMatriculaCollectionNewMatricula = em.merge(oldIdUsuarioOfMatriculaCollectionNewMatricula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = usuarios.getIdUsuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Rol idRol = usuarios.getIdRol();
            if (idRol != null) {
                idRol.getUsuariosCollection().remove(usuarios);
                idRol = em.merge(idRol);
            }
            Collection<Matricula> matriculaCollection = usuarios.getMatriculaCollection();
            for (Matricula matriculaCollectionMatricula : matriculaCollection) {
                matriculaCollectionMatricula.setIdUsuario(null);
                matriculaCollectionMatricula = em.merge(matriculaCollectionMatricula);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
