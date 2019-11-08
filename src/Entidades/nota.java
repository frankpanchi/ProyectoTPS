/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.math.BigDecimal;

/**
 *
 * @author Edgar
 */
public class nota {
    private BigDecimal idnota;
    private BigDecimal idprofesor;
    private BigDecimal idperiodo;
    private BigDecimal idmatricula;
    private float nota1;
    private float nota2;
    private float nota3;

    public nota(BigDecimal idnota,BigDecimal idprofesor,BigDecimal idperiodo,BigDecimal idmatricula,float nota1,float nota2,float nota3) {
        
      this.idnota=idnota;
    this.idprofesor=idprofesor;
    this.idperiodo=idperiodo;
    this.idmatricula=idmatricula;
    this.nota1=nota1;
    this.nota2=nota2;
    this.nota3=nota3;
    }
    
   public BigDecimal getIdNota() {
        return idnota;
    }

    public void setIdNota(BigDecimal idnota) {
        this.idnota = idnota;
    }
    
     public BigDecimal getIdProfesor() {
        return idprofesor;
    }

    public void setIdProfesor(BigDecimal idprofesor) {
        this.idprofesor = idprofesor;
    }
    
     public BigDecimal getIdPeriodo() {
        return idperiodo;
    }

    public void setIdPeriodo(BigDecimal idperiodo) {
        this.idnota = idperiodo;
    }
    
     public BigDecimal getIdMatricula() {
        return idmatricula;
    }

    public void setIdMatricula(BigDecimal idmatricula) {
        this.idnota = idmatricula;
    }
    
     public float getNota1() {
        return nota1;
    }

    public void setNota1(float nota1) {
        this.nota1 = nota1;
    }
    public float getNota2() {
        return nota2;
    }

    public void setNota2(float nota2) {
        this.nota2 = nota2;
    }
    
    public float getNota3() {
        return nota3;
    }

    public void setNota3(float nota3) {
        this.nota3 = nota3;
    }
    
}
