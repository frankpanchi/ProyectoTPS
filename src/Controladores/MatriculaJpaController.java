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
import Entidades.Grado;
import Entidades.Matricula;
import Entidades.PagoAlumno;
import Entidades.TMatricula;
import Entidades.Usuarios;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author PANCHY
 */
public class MatriculaJpaController implements Serializable {

    public MatriculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Matricula matricula) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Alumno idAlumno = matricula.getIdAlumno();
            if (idAlumno != null) {
                idAlumno = em.getReference(idAlumno.getClass(), idAlumno.getIdAlumno());
                matricula.setIdAlumno(idAlumno);
            }
            Grado idGrado = matricula.getIdGrado();
            if (idGrado != null) {
                idGrado = em.getReference(idGrado.getClass(), idGrado.getIdGrado());
                matricula.setIdGrado(idGrado);
            }
            PagoAlumno idPagoalumno = matricula.getIdPagoalumno();
            if (idPagoalumno != null) {
                idPagoalumno = em.getReference(idPagoalumno.getClass(), idPagoalumno.getIdPagoalumno());
                matricula.setIdPagoalumno(idPagoalumno);
            }
            TMatricula idTmatricula = matricula.getIdTmatricula();
            if (idTmatricula != null) {
                idTmatricula = em.getReference(idTmatricula.getClass(), idTmatricula.getIdTmatricula());
                matricula.setIdTmatricula(idTmatricula);
            }
            Usuarios idUsuario = matricula.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                matricula.setIdUsuario(idUsuario);
            }
            em.persist(matricula);
            if (idAlumno != null) {
                idAlumno.getMatriculaCollection().add(matricula);
                idAlumno = em.merge(idAlumno);
            }
            if (idGrado != null) {
                idGrado.getMatriculaCollection().add(matricula);
                idGrado = em.merge(idGrado);
            }
            if (idPagoalumno != null) {
                idPagoalumno.getMatriculaCollection().add(matricula);
                idPagoalumno = em.merge(idPagoalumno);
            }
            if (idTmatricula != null) {
                idTmatricula.getMatriculaCollection().add(matricula);
                idTmatricula = em.merge(idTmatricula);
            }
            if (idUsuario != null) {
                idUsuario.getMatriculaCollection().add(matricula);
                idUsuario = em.merge(idUsuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMatricula(matricula.getIdMatricula()) != null) {
                throw new PreexistingEntityException("Matricula " + matricula + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Matricula matricula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Matricula persistentMatricula = em.find(Matricula.class, matricula.getIdMatricula());
            Alumno idAlumnoOld = persistentMatricula.getIdAlumno();
            Alumno idAlumnoNew = matricula.getIdAlumno();
            Grado idGradoOld = persistentMatricula.getIdGrado();
            Grado idGradoNew = matricula.getIdGrado();
            PagoAlumno idPagoalumnoOld = persistentMatricula.getIdPagoalumno();
            PagoAlumno idPagoalumnoNew = matricula.getIdPagoalumno();
            TMatricula idTmatriculaOld = persistentMatricula.getIdTmatricula();
            TMatricula idTmatriculaNew = matricula.getIdTmatricula();
            Usuarios idUsuarioOld = persistentMatricula.getIdUsuario();
            Usuarios idUsuarioNew = matricula.getIdUsuario();
            if (idAlumnoNew != null) {
                idAlumnoNew = em.getReference(idAlumnoNew.getClass(), idAlumnoNew.getIdAlumno());
                matricula.setIdAlumno(idAlumnoNew);
            }
            if (idGradoNew != null) {
                idGradoNew = em.getReference(idGradoNew.getClass(), idGradoNew.getIdGrado());
                matricula.setIdGrado(idGradoNew);
            }
            if (idPagoalumnoNew != null) {
                idPagoalumnoNew = em.getReference(idPagoalumnoNew.getClass(), idPagoalumnoNew.getIdPagoalumno());
                matricula.setIdPagoalumno(idPagoalumnoNew);
            }
            if (idTmatriculaNew != null) {
                idTmatriculaNew = em.getReference(idTmatriculaNew.getClass(), idTmatriculaNew.getIdTmatricula());
                matricula.setIdTmatricula(idTmatriculaNew);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                matricula.setIdUsuario(idUsuarioNew);
            }
            matricula = em.merge(matricula);
            if (idAlumnoOld != null && !idAlumnoOld.equals(idAlumnoNew)) {
                idAlumnoOld.getMatriculaCollection().remove(matricula);
                idAlumnoOld = em.merge(idAlumnoOld);
            }
            if (idAlumnoNew != null && !idAlumnoNew.equals(idAlumnoOld)) {
                idAlumnoNew.getMatriculaCollection().add(matricula);
                idAlumnoNew = em.merge(idAlumnoNew);
            }
            if (idGradoOld != null && !idGradoOld.equals(idGradoNew)) {
                idGradoOld.getMatriculaCollection().remove(matricula);
                idGradoOld = em.merge(idGradoOld);
            }
            if (idGradoNew != null && !idGradoNew.equals(idGradoOld)) {
                idGradoNew.getMatriculaCollection().add(matricula);
                idGradoNew = em.merge(idGradoNew);
            }
            if (idPagoalumnoOld != null && !idPagoalumnoOld.equals(idPagoalumnoNew)) {
                idPagoalumnoOld.getMatriculaCollection().remove(matricula);
                idPagoalumnoOld = em.merge(idPagoalumnoOld);
            }
            if (idPagoalumnoNew != null && !idPagoalumnoNew.equals(idPagoalumnoOld)) {
                idPagoalumnoNew.getMatriculaCollection().add(matricula);
                idPagoalumnoNew = em.merge(idPagoalumnoNew);
            }
            if (idTmatriculaOld != null && !idTmatriculaOld.equals(idTmatriculaNew)) {
                idTmatriculaOld.getMatriculaCollection().remove(matricula);
                idTmatriculaOld = em.merge(idTmatriculaOld);
            }
            if (idTmatriculaNew != null && !idTmatriculaNew.equals(idTmatriculaOld)) {
                idTmatriculaNew.getMatriculaCollection().add(matricula);
                idTmatriculaNew = em.merge(idTmatriculaNew);
            }
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getMatriculaCollection().remove(matricula);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getMatriculaCollection().add(matricula);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = matricula.getIdMatricula();
                if (findMatricula(id) == null) {
                    throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.");
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
            Matricula matricula;
            try {
                matricula = em.getReference(Matricula.class, id);
                matricula.getIdMatricula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The matricula with id " + id + " no longer exists.", enfe);
            }
            Alumno idAlumno = matricula.getIdAlumno();
            if (idAlumno != null) {
                idAlumno.getMatriculaCollection().remove(matricula);
                idAlumno = em.merge(idAlumno);
            }
            Grado idGrado = matricula.getIdGrado();
            if (idGrado != null) {
                idGrado.getMatriculaCollection().remove(matricula);
                idGrado = em.merge(idGrado);
            }
            PagoAlumno idPagoalumno = matricula.getIdPagoalumno();
            if (idPagoalumno != null) {
                idPagoalumno.getMatriculaCollection().remove(matricula);
                idPagoalumno = em.merge(idPagoalumno);
            }
            TMatricula idTmatricula = matricula.getIdTmatricula();
            if (idTmatricula != null) {
                idTmatricula.getMatriculaCollection().remove(matricula);
                idTmatricula = em.merge(idTmatricula);
            }
            Usuarios idUsuario = matricula.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getMatriculaCollection().remove(matricula);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(matricula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Matricula> findMatriculaEntities() {
        return findMatriculaEntities(true, -1, -1);
    }

    public List<Matricula> findMatriculaEntities(int maxResults, int firstResult) {
        return findMatriculaEntities(false, maxResults, firstResult);
    }

    private List<Matricula> findMatriculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Matricula.class));
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

    public Matricula findMatricula(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Matricula.class, id);
        } finally {
            em.close();
        }
    }

    public int getMatriculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Matricula> rt = cq.from(Matricula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
