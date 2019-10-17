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
@Table(name = "SALARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Salario.findAll", query = "SELECT s FROM Salario s"),
    @NamedQuery(name = "Salario.findByIdSalario", query = "SELECT s FROM Salario s WHERE s.idSalario = :idSalario"),
    @NamedQuery(name = "Salario.findBySalario", query = "SELECT s FROM Salario s WHERE s.salario = :salario"),
    @NamedQuery(name = "Salario.findByHorasExtras", query = "SELECT s FROM Salario s WHERE s.horasExtras = :horasExtras")})
public class Salario implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
     @SequenceGenerator(name="id_salario",sequenceName="id_salario",allocationSize=1)
    @GeneratedValue(strategy=GenerationType.IDENTITY,generator="id_salario")
    @Column(name = "ID_SALARIO")
    private BigDecimal idSalario;
    @Column(name = "SALARIO")
    private Double salario;
    @Column(name = "HORAS_EXTRAS")
    private Double horasExtras;
    @OneToMany(mappedBy = "idSalario")
    private Collection<Pago> pagoCollection;

    public Salario() {
    }

     public Salario(BigDecimal idSalario, String Salario) {
        this.idSalario = idSalario;
        this.salario = salario;
    }

    public BigDecimal getIdSalario() {
        return idSalario;
    }

    public void setIdSalario(BigDecimal idSalario) {
        this.idSalario = idSalario;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public Double getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Double horasExtras) {
        this.horasExtras = horasExtras;
    }

    @XmlTransient
    public Collection<Pago> getPagoCollection() {
        return pagoCollection;
    }

    public void setPagoCollection(Collection<Pago> pagoCollection) {
        this.pagoCollection = pagoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSalario != null ? idSalario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Salario)) {
            return false;
        }
        Salario other = (Salario) object;
        if ((this.idSalario == null && other.idSalario != null) || (this.idSalario != null && !this.idSalario.equals(other.idSalario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return salario.toString();
    }
    
}
