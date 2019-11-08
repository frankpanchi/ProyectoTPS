/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Edgar
 */
public class Conexion {
    
     private static Connection conn=null;
    private static final String  login = "SYSTEM";
    private static final String  clave = "admin";
    private static final String  url = "jdbc:oracle:thin:@localhost:1521:XE";
    
    public Connection getConnection() throws SQLException{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn=DriverManager.getConnection(url,login,clave);
            conn.setAutoCommit(false);
                if (conn!=null) {
                    System.out.println("Conexion exitosa");
                }
                else{
                    System.out.println("Conexion fallida");
                }
                
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Conexion erronea");
        }
        return conn;
    }
    
    public void desconectar(){
        try {
            conn.close();
        } catch (Exception e) {
            System.out.println("Error al desconectar"+e.getMessage());
        }
    }
    
        
    
}
