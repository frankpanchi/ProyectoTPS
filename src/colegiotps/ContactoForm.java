/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colegiotps;

import Controladores.CarnetJpaController;
import Controladores.ContactoJpaController;
import Controladores.exceptions.NonexistentEntityException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PANCHY
 */
public class ContactoForm extends javax.swing.JFrame {
private String contact;
    DefaultTableModel modelo;
    ContactoJpaController Ccontacto = new ContactoJpaController();
    Entidades.Contacto contacto;
    /**
     * Creates new form Contacto
     */
    public ContactoForm() {
        initComponents();
        mostrar();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtnombre = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtapellido = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        txtdui = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtdireccion = new javax.swing.JTextPane();
        txttelefono = new javax.swing.JFormattedTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        Jtcontacto = new javax.swing.JTable();
        btncrear = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btneditar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        btncancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(txtnombre);

        jLabel1.setText("Nombre:");

        jLabel2.setText("Apellido:");

        jScrollPane2.setViewportView(txtapellido);

        jLabel3.setText("Dui:");

        try {
            txtdui.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########-#")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel4.setText("Direccion:");

        jLabel5.setText("Telefono:");

        jScrollPane3.setViewportView(txtdireccion);

        try {
            txttelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        Jtcontacto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "id", "Nombre", "Apellido", "Dui", "Direccion", "Telefono"
            }
        ));
        Jtcontacto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JtcontactoMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(Jtcontacto);
        if (Jtcontacto.getColumnModel().getColumnCount() > 0) {
            Jtcontacto.getColumnModel().getColumn(0).setMinWidth(0);
            Jtcontacto.getColumnModel().getColumn(0).setPreferredWidth(0);
            Jtcontacto.getColumnModel().getColumn(0).setMaxWidth(0);
        }

        btncrear.setText("Crear");
        btncrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncrearActionPerformed(evt);
            }
        });

        btneliminar.setText("Eliminar");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        btneditar.setText("Editar");
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 0, 0));
        jLabel7.setText("MANTENIMIENTO DE CONTACTO");

        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(164, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2)
                                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(149, 149, 149)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(jLabel7)))
                .addGap(84, 84, 84))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(btncrear)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btneliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btneditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btncancelar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(txttelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtdui, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncrear)
                    .addComponent(btneliminar)
                    .addComponent(btneditar)
                    .addComponent(btncancelar))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JtcontactoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JtcontactoMouseClicked
        try {  
                int indice =  this.Jtcontacto.getSelectedRow ();
                if(indice >  - 1 )
                {
                       txtnombre. setText (modelo.getValueAt (indice, 2 ).toString ());
                       txtapellido. setText (modelo.getValueAt (indice, 3 ).toString ());
                       txtdui. setText (modelo.getValueAt (indice, 4 ).toString ());
                       txtdireccion. setText (modelo.getValueAt (indice,  5).toString ());
                       txttelefono. setText (modelo.getValueAt (indice, 6 ).toString ());
                      contacto = (Entidades.Contacto) this.Jtcontacto.getValueAt(Jtcontacto.getSelectedRow(), 0);
                       // this.cbEncargado.setSelectedItem ((modeloTable.getValueAt (indice, 4)));
                        this. btneditar . setEnabled ( true );
                    this. btneliminar . setEnabled ( true );
                    this. btncrear . setEnabled (false);
                }
         } catch( Exception e)
            {
                
            } 


// TODO add your handling code here:
    }//GEN-LAST:event_JtcontactoMouseClicked

    private void btncrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncrearActionPerformed
try {
        String nombre = this.txtnombre.getText(); 
        String apellido = this.txtapellido.getText();
        String dui = this.txtdui.getText();
        String direccion = this.txtdireccion.getText();
        String telefono = this.txttelefono.getText();
        

        ContactoJpaController ca = new ContactoJpaController();
        Entidades.Contacto c= new Entidades.Contacto();
//c.setIdCarnet(Integer.parseInteger(idCarnet));
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setDui(dui);
        c.setDireccion(direccion);
        c.setTelefono(telefono);
    ca.create(c);
   JOptionPane.showConfirmDialog(null,"Contacto ingresado correctamente");
    this.mostrar();
    }
    catch(Exception e)
        {
    JOptionPane.showConfirmDialog(null,"Error " + e.toString());
    }        


// TODO add your handling code here:
    }//GEN-LAST:event_btncrearActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
       
 int indice =  this. Jtcontacto . getSelectedRow();       
        try{
            BigDecimal bigDecimalValue =  new  BigDecimal (modelo.getValueAt(indice, 1 ).toString());
            int opcion =  JOptionPane . showConfirmDialog ( null , " Está seguro que desea eliminar el contacto " + bigDecimalValue . toString (), " Eliminar Contacto " , JOptionPane . YES_NO_OPTION );
             this.mostrar();
            if (opcion == 0 ) {
                Ccontacto . destroy(bigDecimalValue);
                
                this.btneliminar . setEnabled ( false );
               
            }
            else
            {
                
                this.Limpiar();
               
            }
            
        } catch ( NonexistentEntityException ex) {
            Logger.getLogger (ContactoForm.class.getName()).log(Level.SEVERE , null , ex);
        }
         this.mostrar();        

// TODO add your handling code here:
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
       
       
 try{
                int opcion =  JOptionPane . showConfirmDialog ( null , " Está seguro que desea actualizar el contacto " , " Actualizar contacto " , JOptionPane . YES_NO_OPTION );
                if (opcion ==  0 )
                {
                    contacto. setNombre (txtnombre .getText());
                    contacto. setApellido (txtapellido .getText());
                    contacto. setDui (txtdui .getText());
                    contacto. setDireccion (txtdireccion .getText());
                    contacto. setTelefono (txttelefono .getText());
                    Ccontacto.edit(contacto);
                    mostrar();
                    this. btneditar . setEnabled ( false );
                    this. btneliminar . setEnabled ( false );
                    this. btncrear . setEnabled (true);
                    
                    this. Limpiar ();
                }
                else
                {
                    this. btncrear . setEnabled (true);
                    this. btneditar. setEnabled (false);
                    this. btneliminar . setEnabled (false);
                    this. Limpiar ();
                }
                
            } catch ( Exception e)     
             {

               JOptionPane.showMessageDialog ( null , e);
             }           

// TODO add your handling code here:
    }//GEN-LAST:event_btneditarActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
     
                     this. btncrear . setEnabled (true);
                    this. btneditar. setEnabled (false);
                    this. btneliminar . setEnabled (false);
                  

                    Limpiar();
// TODO add your handling code here:
    }//GEN-LAST:event_btncancelarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ContactoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ContactoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ContactoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ContactoForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ContactoForm().setVisible(true);
            }
        });
    }
    public void mostrar()
    {
    Object c []=null;
    modelo= (DefaultTableModel) this.Jtcontacto.getModel();
    
    List < Entidades.Contacto >  Lcontacto  = Ccontacto.findContactoEntities();
        for (int i = 0; i <Lcontacto.size() ; i++) {
            modelo.addRow(c);
            modelo.setValueAt (Lcontacto.get(i),i,0);
            modelo.setValueAt (Lcontacto.get(i).getIdContacto(),i,1);
            modelo.setValueAt(Lcontacto.get(i).getNombre(), i,2);
            modelo.setValueAt(Lcontacto.get(i).getApellido(), i,3);
            modelo.setValueAt(Lcontacto.get(i).getDui(), i,4);
            modelo.setValueAt(Lcontacto.get(i).getDireccion(), i,5);
            modelo.setValueAt(Lcontacto.get(i).getTelefono(), i,6);
        }
        
        
    
        

    }
    
    public void Limpiar()
    {
    this.txtnombre.setText(" ");
     this.txtapellido.setText(" ");
      this.txtdui.setText("");
       this.txtdireccion.setText(" ");
        this.txttelefono.setText("");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Jtcontacto;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btncrear;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btneliminar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextPane txtapellido;
    private javax.swing.JTextPane txtdireccion;
    private javax.swing.JFormattedTextField txtdui;
    private javax.swing.JTextPane txtnombre;
    private javax.swing.JFormattedTextField txttelefono;
    // End of variables declaration//GEN-END:variables
}
