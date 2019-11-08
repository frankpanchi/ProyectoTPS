/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Entidades.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class MATRICULA {
    
    Conexion conn;
//incializa la clase conexion o el objeto
    public MATRICULA() {
        
         conn = new Conexion();
    }
    
    public void matricular(int idUsuario,int idGrado,int idAlum,int idmatricul,int idpagoalumno) {
        try {
            Date fecha = new Date();

            //llamada del procedimiento almacenado por medio del callable
            Connection cn = conn.getConnection();
            //prepara los valores para llamar el procedimiento
            CallableStatement cst = conn.getConnection().prepareCall("{call INSERTAR_MATRICULA(?,?,?,?,?,?)}");
            cst.setInt(1, idUsuario);
            cst.setInt(2, idGrado);
            cst.setInt(3, idAlum);
            cst.setInt(4, idmatricul);
            cst.setDate(5, (java.sql.Date) fecha);
            cst.setInt(6, idpagoalumno);
            cst.execute();

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Conexion erronea"+e.getMessage());
        }
    }
    
    
}
