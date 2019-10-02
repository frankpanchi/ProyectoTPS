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
@Table(name = "CARNET")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carnet.findAll", query = "SELECT c FROM Carnet c"),
    @NamedQuery(name = "Carnet.findByIdCarnet", query = "SELECT c FROM Carnet c WHERE c.idCarnet = :idCarnet"),
    @NamedQuery(name = "Carnet.findByCarnet", query = "SELECT c FROM Carnet c WHERE c.carnet = :carnet")})
public class Carnet implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "ID_CARNET")
    private BigDecimal idCarnet;
    @Basic(optional = false)
    @Column(name = "CARNET")
    private String carnet;
    @OneToMany(mappedBy = "idCarnet")
    private Collection<Alumno> alumnoCollection;

    public Carnet() {
    }

    public Carnet(BigDecimal idCarnet) {
        this.idCarnet = idCarnet;
    }

    public Carnet(BigDecimal idCarnet, String carnet) {
        this.idCarnet = idCarnet;
        this.carnet = carnet;
    }

    public BigDecimal getIdCarnet() {
        return idCarnet;
    }

    public void setIdCarnet(BigDecimal idCarnet) {
        this.idCarnet = idCarnet;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    @XmlTransient
    public Collection<Alumno> getAlumnoCollection() {
        return alumnoCollection;
    }

    public void setAlumnoCollection(Collection<Alumno> alumnoCollection) {
        this.alumnoCollection = alumnoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarnet != null ? idCarnet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carnet)) {
            return false;
        }
        Carnet other = (Carnet) object;
        if ((this.idCarnet == null && other.idCarnet != null) || (this.idCarnet != null && !this.idCarnet.equals(other.idCarnet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Carnet[ idCarnet=" + idCarnet + " ]";
    }
    
}
