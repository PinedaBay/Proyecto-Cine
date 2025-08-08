
package sistemacine.vista;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.modelo.Conexion;

public class FrmSalas extends javax.swing.JInternalFrame{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmSalas.class.getName());
    
    public FrmSalas() {
        initComponents();
        limpiarCampos();
        cargarTablaSalas();
        mostrarSiguienteIdSala();     
    }
    public void FilaSeleccionada() {
                int fila = tblSalas.getSelectedRow();
                if (fila >= 0) {
                    txtIdSalas.setText(tblSalas.getValueAt(fila, 0).toString());
                    txtNombreSala.setText(tblSalas.getValueAt(fila, 1).toString());
                    txtTipoSalas.setText(tblSalas.getValueAt(fila, 2).toString());
                    txtCapacidadSalas.setText(tblSalas.getValueAt(fila, 3).toString());
                    btnGuardar.setEnabled(false);
                }
            }
    
    private void mostrarSiguienteIdSala() {
        String sql = "SELECT COALESCE(MAX(sala_id), 0) + 1 AS siguienteId FROM salas";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                txtIdSalas.setText(String.valueOf(rs.getInt("siguienteId")));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al obtener el siguiente ID: " + e.getMessage());
        }
    }

   private void cargarTablaSalas() {
        DefaultTableModel modelo = (DefaultTableModel) tblSalas.getModel();
        modelo.setRowCount(0);

        String sql = "SELECT sala_id, nombre,tipo, capacidad FROM salas";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("sala_id"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getInt("capacidad")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando salas: " + e.getMessage());
        }
    }
   
private void guardarSala() {
    try {
        String nombre = txtNombreSala.getText().trim();
        String tipo = txtTipoSalas.getText().trim();
        int capacidad = Integer.parseInt(txtCapacidadSalas.getText().trim());

        String sql = "INSERT INTO salas (nombre, tipo, capacidad) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setInt(3, capacidad);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Sala guardada exitosamente.");
                cargarTablaSalas();  
                limpiarCampos();     
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la sala.");
            }
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero.");
    } catch (HeadlessException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar sala: " + e.getMessage());
    }
}

private void actualizarSala() {
         try {
        int salaId = Integer.parseInt(txtIdSalas.getText().trim());
        String nombre = txtNombreSala.getText().trim();
        String tipo = txtTipoSalas.getText().trim();
        int capacidad = Integer.parseInt(txtCapacidadSalas.getText().trim());

        String sql = "UPDATE salas SET nombre = ?, tipo = ?, capacidad = ? WHERE sala_id = ?";
        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, tipo);
            ps.setInt(3, capacidad);
            ps.setInt(4, salaId);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Sala actualizada correctamente.");
                cargarTablaSalas();
                limpiarCampos();
                mostrarSiguienteIdSala();
            }
        }
    } catch (HeadlessException | NumberFormatException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al editar sala: " + e.getMessage());
    }
    }

    private void eliminarSala() {
        int fila = tblSalas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una sala para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar sala seleccionada?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(tblSalas.getValueAt(fila, 0).toString());
                String sql = "DELETE FROM salas WHERE sala_id = ?";
                try (Connection conn = Conexion.getConexion();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int filas = ps.executeUpdate();
                    if (filas > 0) {
                        JOptionPane.showMessageDialog(this, "Sala eliminada correctamente.");
                        cargarTablaSalas();
                        limpiarCampos();
                        mostrarSiguienteIdSala();
                    }
                }
            } catch (HeadlessException | NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar sala: " + e.getMessage());
            }
        }
        
 }

private void limpiarCampos() {
    txtIdSalas.setText("");
    txtNombreSala.setText("");
    txtTipoSalas.setText("");
    txtCapacidadSalas.setText("");
    btnGuardar.setEnabled(true); 
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblFuncion = new javax.swing.JLabel();
        lblPelicula = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSalas = new javax.swing.JTable();
        lblFecha = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        lblHora = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        txtIdSalas = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        txtTipoSalas = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        txtCapacidadSalas = new javax.swing.JTextField();
        txtNombreSala = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("GESTION DE FUNCIONES");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 20, -1, -1));

        lblFuncion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFuncion.setText("ID SALA");
        getContentPane().add(lblFuncion, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 107, -1));

        lblPelicula.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPelicula.setText("NOMBRE");
        getContentPane().add(lblPelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 107, -1));

        tblSalas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID SALAS", "NOMBRE", "TIPO", "CAPACIDAD"
            }
        ));
        jScrollPane1.setViewportView(tblSalas);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 890, 510));

        lblFecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFecha.setText("TIPO");
        getContentPane().add(lblFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 420, 107, -1));

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 660, 132, -1));

        lblHora.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHora.setText("CAPACIDAD");
        getContentPane().add(lblHora, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 560, 107, 20));

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 660, 119, -1));

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditar.setText("ACTUALIZAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 660, -1, -1));

        txtIdSalas.setText("jTextField1");
        txtIdSalas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdSalasActionPerformed(evt);
            }
        });
        getContentPane().add(txtIdSalas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 90, 152, -1));

        btnLimpiar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 660, 125, -1));

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 660, 115, -1));

        txtTipoSalas.setText("jTextField1");
        txtTipoSalas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipoSalasActionPerformed(evt);
            }
        });
        getContentPane().add(txtTipoSalas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 158, -1));

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 660, 122, -1));

        txtCapacidadSalas.setText("jTextField1");
        txtCapacidadSalas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapacidadSalasActionPerformed(evt);
            }
        });
        getContentPane().add(txtCapacidadSalas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 560, 158, 20));

        txtNombreSala.setText("jTextField1");
        txtNombreSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreSalaActionPerformed(evt);
            }
        });
        getContentPane().add(txtNombreSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 158, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarSala();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
         actualizarSala();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void txtIdSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdSalasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdSalasActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
       limpiarCampos(); 
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiarCampos();
        mostrarSiguienteIdSala();
        btnGuardar.setEnabled(true);
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void txtTipoSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipoSalasActionPerformed

    }//GEN-LAST:event_txtTipoSalasActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarSala();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtCapacidadSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapacidadSalasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCapacidadSalasActionPerformed

    private void txtNombreSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreSalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreSalaActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        FilaSeleccionada();
    }//GEN-LAST:event_formMouseClicked

   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFuncion;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblPelicula;
    private javax.swing.JTable tblSalas;
    private javax.swing.JTextField txtCapacidadSalas;
    private javax.swing.JTextField txtIdSalas;
    private javax.swing.JTextField txtNombreSala;
    private javax.swing.JTextField txtTipoSalas;
    // End of variables declaration//GEN-END:variables
}
