/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PANCHY
 */
@Entity
@Table(name = "T_MATRICULA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TMatricula.findAll", query = "SELECT t FROM TMatricula t"),
    @NamedQuery(name = "TMatricula.findByIdTmatricula", query = "SELECT t FROM TMatricula t WHERE t.idTmatricula = :idTmatricula"),
    @NamedQuery(name = "TMatricula.findByFechaIniciom", query = "SELECT t FROM TMatricula t WHERE t.fechaIniciom = :fechaIniciom"),
    @NamedQuery(name = "TMatricula.findByFechaFinalm", query = "SELECT t FROM TMatricula t WHERE t.fechaFinalm = :fechaFinalm"),
    @NamedQuery(name = "TMatricula.findByFechaXim", query = "SELECT t FROM TMatricula t WHERE t.fechaXim = :fechaXim"),
    @NamedQuery(name = "TMatricula.findByFechaXfm", query = "SELECT t FROM TMatricula t WHERE t.fechaXfm = :fechaXfm")})
public class TMatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_TMATRICULA")
    private BigDecimal idTmatricula;
    @Column(name = "FECHA_INICIOM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIniciom;
    @Column(name = "FECHA_FINALM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinalm;
    @Column(name = "FECHA_XIM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaXim;
    @Column(name = "FECHA_XFM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaXfm;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTmatricula")
    private Collection<Matricula> matriculaCollection;

    public TMatricula() {
    }

    public TMatricula(BigDecimal idTmatricula) {
        this.idTmatricula = idTmatricula;
    }

    public BigDecimal getIdTmatricula() {
        return idTmatricula;
    }

    public void setIdTmatricula(BigDecimal idTmatricula) {
        this.idTmatricula = idTmatricula;
    }

    public Date getFechaIniciom() {
        return fechaIniciom;
    }

    public void setFechaIniciom(Date fechaIniciom) {
        this.fechaIniciom = fechaIniciom;
    }

    public Date getFechaFinalm() {
        return fechaFinalm;
    }

    public void setFechaFinalm(Date fechaFinalm) {
        this.fechaFinalm = fechaFinalm;
    }

    public Date getFechaXim() {
        return fechaXim;
    }

    public void setFechaXim(Date fechaXim) {
        this.fechaXim = fechaXim;
    }

    public Date getFechaXfm() {
        return fechaXfm;
    }

    public void setFechaXfm(Date fechaXfm) {
        this.fechaXfm = fechaXfm;
    }

    @XmlTransient
    public Collection<Matricula> getMatriculaCollection() {
        return matriculaCollection;
    }

    public void setMatriculaCollection(Collection<Matricula> matriculaCollection) {
        this.matriculaCollection = matriculaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTmatricula != null ? idTmatricula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TMatricula)) {
            return false;
        }
        TMatricula other = (TMatricula) object;
        if ((this.idTmatricula == null && other.idTmatricula != null) || (this.idTmatricula != null && !this.idTmatricula.equals(other.idTmatricula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TMatricula[ idTmatricula=" + idTmatricula + " ]";
    }
    
}
