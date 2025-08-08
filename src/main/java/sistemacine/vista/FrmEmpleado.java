
package sistemacine.vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.modelo.Conexion;


public class FrmEmpleado extends javax.swing.JInternalFrame {  

    public FrmEmpleado() {
        initComponents();
        limpiarCampos();
        configurarPopupCombo();
        cboEstado.removeAllItems();
        cboEstado.setSelectedIndex(-1); 
        btnGuardarEmpleado.setEnabled(true);
        mostrarSiguienteIdEmpleado();
        cargarTablaEmpleados();
        mostrarSiguienteIdEmpleado();
        cargarTablaEmpleados();          
    }
    
    public void FilaSeleccionada() {
            int fila = tblEmpleados.getSelectedRow();
    if (fila >= 0) {
        txtIdEmpleado.setText(tblEmpleados.getValueAt(fila, 0).toString());
        txtNombreEmpleado.setText(tblEmpleados.getValueAt(fila, 1).toString());
        txtCargo.setText(tblEmpleados.getValueAt(fila, 2).toString());

        // Aquí cargamos ambas opciones en el combo
        cboEstado.removeAllItems();
        cboEstado.addItem("Activo");
        cboEstado.addItem("Inactivo");

        // Seleccionamos la opción según el valor de la tabla
        String estado = tblEmpleados.getValueAt(fila, 3).toString();
        if (estado.equalsIgnoreCase("Activo")) {
            cboEstado.setSelectedItem("Activo");
        } else {
            cboEstado.setSelectedItem("Inactivo");
        }
        btnGuardarEmpleado.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnEliminar.setEnabled(true);
        }
    }
    private void limpiarCampos() {
    txtIdEmpleado.setText("");
    txtNombreEmpleado.setText("");
    txtCargo.setText("");
    cboEstado.removeAllItems();
    cboEstado.setSelectedIndex(-1);

    btnGuardarEmpleado.setEnabled(true);
    btnActualizar.setEnabled(false);
    btnEliminar.setEnabled(false);
    mostrarSiguienteIdEmpleado();
    }
    
    private void configurarPopupCombo() {
    cboEstado.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
        @Override
        public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
            if (cboEstado.getItemCount() == 1) return;
            cboEstado.removeAllItems();
            cboEstado.addItem("Activo");
            cboEstado.addItem("Inactivo");
        }
        @Override
        public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
        @Override
        public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
    });
}   

   private void mostrarSiguienteIdEmpleado() {
    try (Connection conn = Conexion.getConexion()) {
            String sql = "SELECT IFNULL(MAX(empleado_id), 0) + 1 AS proximo_id FROM empleados";
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int siguienteId = rs.getInt("proximo_id");
                txtIdEmpleado.setText(String.valueOf(siguienteId));
            }
        }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener siguiente ID: " + e.getMessage());
        }
}

private void cargarTablaEmpleados() {
     DefaultTableModel modelo = (DefaultTableModel) tblEmpleados.getModel();
        modelo.setRowCount(0);

        try (Connection conn = Conexion.getConexion()) {
            String sql = "SELECT empleado_id, nombre, cargo, activo FROM empleados";
         try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
             
             while (rs.next()) {
                 Object[] fila = {
                     rs.getInt("empleado_id"),
                     rs.getString("nombre"),
                     rs.getString("cargo"),
                     rs.getBoolean("activo") ? "Activo" : "Inactivo"
                 };
                 modelo.addRow(fila);
             }
             
         }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage());
        }
}
public void BotonGuardar(){
    String id = txtIdEmpleado.getText().trim();
        String nombre = txtNombreEmpleado.getText().trim();
        String cargo = txtCargo.getText().trim();

        if (nombre.isEmpty() || cargo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
            return;
        }

        try (Connection conn = Conexion.getConexion()) {
            String sql = "INSERT INTO empleados (empleado_id, nombre, cargo) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(id));
                ps.setString(2, nombre);
                ps.setString(3, cargo);
                
                int resultado = ps.executeUpdate();
                
                if (resultado > 0) {
                    JOptionPane.showMessageDialog(this, "Empleado guardado correctamente.");
                    cargarTablaEmpleados();
                    mostrarSiguienteIdEmpleado();
                    txtNombreEmpleado.setText("");
                    txtCargo.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar empleado.");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar empleado: " + e.getMessage());
        }
        FilaSeleccionada(); 
}

public void BotonActualizar(){
    String id = txtIdEmpleado.getText().trim();
    String nombre = txtNombreEmpleado.getText().trim();
    String cargo = txtCargo.getText().trim();
    String estadoSeleccionado = cboEstado.getSelectedItem().toString();

    if (id.isEmpty() || nombre.isEmpty() || cargo.isEmpty() || estadoSeleccionado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.");
        return;
    }

    boolean activo = estadoSeleccionado.equalsIgnoreCase("Activo");

    try (Connection conn = Conexion.getConexion()) {
        String sql = "UPDATE empleados SET nombre = ?, cargo = ?, activo = ? WHERE empleado_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ps.setString(2, cargo);
        ps.setBoolean(3, activo);
        ps.setInt(4, Integer.parseInt(id));

        int resultado = ps.executeUpdate();

        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
            cargarTablaEmpleados();
            mostrarSiguienteIdEmpleado();
            btnGuardarEmpleado.setEnabled(true);
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró el empleado.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar empleado: " + e.getMessage());
    }
}
public void eliminarEmpleado() {
    int fila = tblEmpleados.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un empleado para inactivar.");
        return;
    }

    String estadoActual = tblEmpleados.getValueAt(fila, 3).toString();
    if (estadoActual.equalsIgnoreCase("Inactivo")) {
        JOptionPane.showMessageDialog(null, "Este empleado ya está eliminado.");
        return;
    }

    int id = Integer.parseInt(tblEmpleados.getValueAt(fila, 0).toString());

    int confirmar = JOptionPane.showConfirmDialog(
        null,
        "El empleado será marcado como INACTIVO. ¿Desea continuar?",
        "Confirmar inactivación",
        JOptionPane.YES_NO_OPTION
    );
    if (confirmar != JOptionPane.YES_OPTION) return;

    try (Connection conn = Conexion.getConexion()) {
        String sql = "UPDATE empleados SET activo = false WHERE empleado_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                JOptionPane.showMessageDialog(null, "Empleado eliminado con éxito.");
                cargarTablaEmpleados();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró el empleado.");
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al inactivar empleado: " + e.getMessage());
    }
    cargarTablaEmpleados();
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        txtIdEmpleado = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNombreEmpleado = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtCargo = new javax.swing.JTextField();
        btnGuardarEmpleado = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cboEstado = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("ID EMPLEADO");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 90, -1, -1));

        txtIdEmpleado.setText("jTextField1");
        txtIdEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdEmpleadoActionPerformed(evt);
            }
        });
        getContentPane().add(txtIdEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, 180, -1));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("NOMBRE");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 230, 83, -1));

        txtNombreEmpleado.setText("jTextField1");
        txtNombreEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreEmpleadoActionPerformed(evt);
            }
        });
        getContentPane().add(txtNombreEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 181, -1));

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID EMPLEADO", "NOMBRE", "CARGO", "ESTADO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpleadosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmpleados);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 80, 920, 500));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("CARGO");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 360, 83, -1));

        txtCargo.setText("jTextField1");
        txtCargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCargoActionPerformed(evt);
            }
        });
        getContentPane().add(txtCargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 360, 181, -1));

        btnGuardarEmpleado.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGuardarEmpleado.setText("GUARDAR");
        btnGuardarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarEmpleadoActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardarEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 640, 101, -1));

        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 640, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 640, -1, -1));

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 640, 92, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("GESTION DE EMPLEADOS");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 0, 190, 30));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("ESTADO");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 540, 83, -1));

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboEstado.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                cboEstadoPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        cboEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEstadoActionPerformed(evt);
            }
        });
        getContentPane().add(cboEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 540, 181, -1));

        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 640, 101, -1));

        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 640, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreEmpleadoActionPerformed

    private void txtIdEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdEmpleadoActionPerformed

    private void btnGuardarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarEmpleadoActionPerformed
        BotonGuardar();
    }//GEN-LAST:event_btnGuardarEmpleadoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        BotonActualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarEmpleado();
        FilaSeleccionada(); 
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtCargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCargoActionPerformed

    private void cboEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoActionPerformed

    private void tblEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadosMouseClicked
        FilaSeleccionada();
    }//GEN-LAST:event_tblEmpleadosMouseClicked

    private void cboEstadoPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cboEstadoPopupMenuCanceled
        // TODO add your handling code here:
    }//GEN-LAST:event_cboEstadoPopupMenuCanceled

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
            limpiarCampos();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
          limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        FilaSeleccionada(); 
    }//GEN-LAST:event_formMouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardarEmpleado;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtCargo;
    private javax.swing.JTextField txtIdEmpleado;
    private javax.swing.JTextField txtNombreEmpleado;
    // End of variables declaration//GEN-END:variables
}
