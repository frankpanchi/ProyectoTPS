/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PANCHY
 */
@Entity
@Table(name = "MATRICULA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m"),
    @NamedQuery(name = "Matricula.findByIdMatricula", query = "SELECT m FROM Matricula m WHERE m.idMatricula = :idMatricula"),
    @NamedQuery(name = "Matricula.findByFechaMatricula", query = "SELECT m FROM Matricula m WHERE m.fechaMatricula = :fechaMatricula")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_MATRICULA")
    private BigDecimal idMatricula;
    @Column(name = "FECHA_MATRICULA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMatricula;
    @JoinColumn(name = "ID_ALUMNO", referencedColumnName = "ID_ALUMNO")
    @ManyToOne
    private Alumno idAlumno;
    @JoinColumn(name = "ID_GRADO", referencedColumnName = "ID_GRADO")
    @ManyToOne
    private Grado idGrado;
    @JoinColumn(name = "ID_PAGOALUMNO", referencedColumnName = "ID_PAGOALUMNO")
    @ManyToOne
    private PagoAlumno idPagoalumno;
    @JoinColumn(name = "ID_TMATRICULA", referencedColumnName = "ID_TMATRICULA")
    @ManyToOne(optional = false)
    private TMatricula idTmatricula;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    @ManyToOne
    private Usuarios idUsuario;

    public Matricula() {
    }

    public Matricula(BigDecimal idMatricula) {
        this.idMatricula = idMatricula;
    }

    public BigDecimal getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(BigDecimal idMatricula) {
        this.idMatricula = idMatricula;
    }

    public Date getFechaMatricula() {
        return fechaMatricula;
    }

    public void setFechaMatricula(Date fechaMatricula) {
        this.fechaMatricula = fechaMatricula;
    }

    public Alumno getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(Alumno idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Grado getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Grado idGrado) {
        this.idGrado = idGrado;
    }

    public PagoAlumno getIdPagoalumno() {
        return idPagoalumno;
    }

    public void setIdPagoalumno(PagoAlumno idPagoalumno) {
        this.idPagoalumno = idPagoalumno;
    }

    public TMatricula getIdTmatricula() {
        return idTmatricula;
    }

    public void setIdTmatricula(TMatricula idTmatricula) {
        this.idTmatricula = idTmatricula;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMatricula != null ? idMatricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.idMatricula == null && other.idMatricula != null) || (this.idMatricula != null && !this.idMatricula.equals(other.idMatricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Matricula[ idMatricula=" + idMatricula + " ]";
    }
    
}
