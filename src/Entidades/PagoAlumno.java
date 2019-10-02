/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PANCHY
 */
@Entity
@Table(name = "PAGO_ALUMNO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PagoAlumno.findAll", query = "SELECT p FROM PagoAlumno p"),
    @NamedQuery(name = "PagoAlumno.findByIdPagoalumno", query = "SELECT p FROM PagoAlumno p WHERE p.idPagoalumno = :idPagoalumno"),
    @NamedQuery(name = "PagoAlumno.findByPago", query = "SELECT p FROM PagoAlumno p WHERE p.pago = :pago")})
public class PagoAlumno implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_PAGOALUMNO")
    private BigDecimal idPagoalumno;
    @Column(name = "PAGO")
    private Double pago;
    @OneToMany(mappedBy = "idPagoalumno")
    private Collection<Matricula> matriculaCollection;

    public PagoAlumno() {
    }

    public PagoAlumno(BigDecimal idPagoalumno) {
        this.idPagoalumno = idPagoalumno;
    }

    public BigDecimal getIdPagoalumno() {
        return idPagoalumno;
    }

    public void setIdPagoalumno(BigDecimal idPagoalumno) {
        this.idPagoalumno = idPagoalumno;
    }

    public Double getPago() {
        return pago;
    }

    public void setPago(Double pago) {
        this.pago = pago;
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
        hash += (idPagoalumno != null ? idPagoalumno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PagoAlumno)) {
            return false;
        }
        PagoAlumno other = (PagoAlumno) object;
        if ((this.idPagoalumno == null && other.idPagoalumno != null) || (this.idPagoalumno != null && !this.idPagoalumno.equals(other.idPagoalumno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.PagoAlumno[ idPagoalumno=" + idPagoalumno + " ]";
    }
    
}
