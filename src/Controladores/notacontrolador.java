/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class notacontrolador {
    Conexion conn;

    public notacontrolador() {
        conn = new Conexion();
    }
    
    public void AgregarNota(int idProfesor,int idPeriodo,int idMatricula,float nota1,float nota2,float nota3,int idmateria) {
        try {
            

            //llamada del procedimiento almacenado por medio del callable
            Connection cn = conn.getConnection();
            //prepara los valores para llamar el procedimiento
            CallableStatement cst = conn.getConnection().prepareCall("{call NOTAS2(?,?,?,?,?,?,?)}");
            cst.setInt(1, idProfesor);
            cst.setInt(2, idPeriodo);
            cst.setInt(3, idMatricula);
            cst.setFloat(4,  idmateria);
            cst.setFloat(5,  nota1);
            cst.setFloat(6,  nota2);
            cst.setFloat(7,  nota3);
            cst.execute();

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Conexion erronea"+e.getMessage());
        }
    }
    
    
}
