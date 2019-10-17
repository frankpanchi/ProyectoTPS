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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PANCHY
 */
@Entity
@Table(name = "SECCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Seccion.findAll", query = "SELECT s FROM Seccion s"),
    @NamedQuery(name = "Seccion.findByIdSeccion", query = "SELECT s FROM Seccion s WHERE s.idSeccion = :idSeccion"),
    @NamedQuery(name = "Seccion.findBySeccion", query = "SELECT s FROM Seccion s WHERE s.seccion = :seccion")})
public class Seccion implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    
    @SequenceGenerator(name="id_seccion",sequenceName="id_seccion",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.IDENTITY,generator="id_seccion") 
    @Column(name = "ID_SECCION")
    private BigDecimal idSeccion;
    @Basic(optional = false)
    @Column(name = "SECCION")
    private String seccion;
    @OneToMany(mappedBy = "idSeccion")
    private Collection<Grado> gradoCollection;

    public Seccion() {
    }

    public Seccion(BigDecimal idSeccion) {
        this.idSeccion = idSeccion;
    }

    public Seccion(BigDecimal idSeccion, String seccion) {
        this.idSeccion = idSeccion;
        this.seccion = seccion;
    }

    public BigDecimal getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(BigDecimal idSeccion) {
        this.idSeccion = idSeccion;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    @XmlTransient
    public Collection<Grado> getGradoCollection() {
        return gradoCollection;
    }

    public void setGradoCollection(Collection<Grado> gradoCollection) {
        this.gradoCollection = gradoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeccion != null ? idSeccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seccion)) {
            return false;
        }
        Seccion other = (Seccion) object;
        if ((this.idSeccion == null && other.idSeccion != null) || (this.idSeccion != null && !this.idSeccion.equals(other.idSeccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return seccion;
    }
    
}
