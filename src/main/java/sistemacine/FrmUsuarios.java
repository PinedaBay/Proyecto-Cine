
package sistemacine;

import java.awt.HeadlessException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmUsuarios extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmUsuarios.class.getName());

    public FrmUsuarios() {
        initComponents();
        cargarEmpleados();
        cargarTablaUsuarios();
        cargarCargosDesdeEmpleados() ;
    }
    private void cargarEmpleados() {
    try (Connection con = Conexion.getConexion()) {
        String sql = "SELECT empleado_id, nombre FROM empleados WHERE activo = true";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        cboEmpleado.removeAllItems();
        
        while (rs.next()) {
            // Puedes almacenar "id - nombre" para recuperar el id luego
            String item = rs.getInt("empleado_id") + " - " + rs.getString("nombre");
            cboEmpleado.addItem(item);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error cargando empleados: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void cargarCargosDesdeEmpleados() {
    try (Connection con = Conexion.getConexion()) {
        String sql = "SELECT DISTINCT cargo FROM empleados WHERE activo = true AND cargo IS NOT NULL";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        cboRoles.removeAllItems();

        while (rs.next()) {
            cboRoles.addItem(rs.getString("cargo"));
        }

        if (cboRoles.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron cargos asignados a empleados.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error cargando cargos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    
    
    private void cargarTablaUsuarios() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[] {"ID", "Usuario", "Rol", "Activo"});

        try (Connection con = Conexion.getConexion()) {
            String sql = "SELECT u.usuario_id, u.NombreUsuario, r.nombre_rol, u.activo " +
                         "FROM usuarios u " +
                         "JOIN usuarios_roles ur ON u.usuario_id = ur.usuario_id " +
                         "JOIN roles r ON ur.rol_id = r.rol_id";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addRow(new Object[] {
                    rs.getInt("usuario_id"),
                    rs.getString("NombreUsuario"),
                    rs.getString("nombre_rol"),
                    rs.getBoolean("activo") ? "Sí" : "No"
                });
            }

            tblUsuarios.setModel(modelo);

        } catch (Exception e) {
            
        }
    }
      private String encriptarSHA256(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(texto.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

  private void BotonGuardar() {
    String usuario = txtUsuario.getText();
    String contrasena = new String(txtContrasenia.getPassword());
    String rol = cboRoles.getSelectedItem().toString();

    if (usuario.isEmpty() || contrasena.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try (Connection con = Conexion.getConexion()) {
        String hash = encriptarSHA256(contrasena);

        // Insertar usuario
        String sqlUsuario = "INSERT INTO usuarios (NombreUsuario, Contrasenia) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, usuario);
        ps.setString(2, hash);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        int usuarioId = 0;
        if (rs.next()) {
            usuarioId = rs.getInt(1);
        }

        // Obtener ID del rol
        String sqlRol = "SELECT rol_id FROM roles WHERE nombre_rol = ?";
        PreparedStatement psRol = con.prepareStatement(sqlRol);
        psRol.setString(1, rol);
        ResultSet rsRol = psRol.executeQuery();
        int rolId = 0;
        if (rsRol.next()) {
            rolId = rsRol.getInt("rol_id");
        }

        // Insertar relación
        String sqlUR = "INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (?, ?)";
        PreparedStatement psUR = con.prepareStatement(sqlUR);
        psUR.setInt(1, usuarioId);
        psUR.setInt(2, rolId);
        psUR.executeUpdate();

        JOptionPane.showMessageDialog(this, "Usuario guardado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        cargarTablaUsuarios();
        limpiarCampos();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void BotonEliminar() {
    int fila = tblUsuarios.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un usuario.", "Atención", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int id = Integer.parseInt(tblUsuarios.getValueAt(fila, 0).toString());

    int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas desactivar este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection con = Conexion.getConexion()) {
            String sql = "UPDATE usuarios SET activo = 0 WHERE usuario_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario desactivado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTablaUsuarios();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


private void BotonActualizar() {
    int fila = tblUsuarios.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Selecciona un usuario para actualizar.", "Atención", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int usuarioId = Integer.parseInt(tblUsuarios.getValueAt(fila, 0).toString());
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasenia.getPassword());
        String rol = cboRoles.getSelectedItem().toString();

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection con = Conexion.getConexion()) {
            String sql;

            // Si el usuario escribió contraseña nueva, actualizamos la contraseña
            if (!contrasena.isEmpty()) {
                String hash = encriptarSHA256(contrasena);
                sql = "UPDATE usuarios SET NombreUsuario = ?, Contrasenia = ? WHERE usuario_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, usuario);
                ps.setString(2, hash);
                ps.setInt(3, usuarioId);
                ps.executeUpdate();
            } else {
                // Si no escribe contraseña, solo actualizamos el usuario
                sql = "UPDATE usuarios SET NombreUsuario = ? WHERE usuario_id = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, usuario);
                ps.setInt(2, usuarioId);
                ps.executeUpdate();
            }

            // Actualizamos el rol
            String sqlRolId = "SELECT rol_id FROM roles WHERE nombre_rol = ?";
            PreparedStatement psRol = con.prepareStatement(sqlRolId);
            psRol.setString(1, rol);
            ResultSet rsRol = psRol.executeQuery();
            int rolId = 0;
            if (rsRol.next()) {
                rolId = rsRol.getInt("rol_id");
            }

            // Actualizamos usuarios_roles
            String sqlUR = "UPDATE usuarios_roles SET rol_id = ? WHERE usuario_id = ?";
            PreparedStatement psUR = con.prepareStatement(sqlUR);
            psUR.setInt(1, rolId);
            psUR.setInt(2, usuarioId);
            psUR.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuario actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            cargarTablaUsuarios();
            limpiarCampos();

        }
    } catch (HeadlessException | NumberFormatException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void limpiarCampos() {
        txtUsuario.setText("");
        txtContrasenia.setText("");
        cboRoles.setSelectedIndex(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        Usuario = new javax.swing.JLabel();
        Contrasenia = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        roles = new javax.swing.JLabel();
        cboRoles = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        Usuario1 = new javax.swing.JLabel();
        cboEmpleado = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("GESTION DE USUARIOS");

        Usuario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Usuario.setText("USUARIO");

        Contrasenia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Contrasenia.setText("CONTRASEÑA");

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        txtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseniaActionPerformed(evt);
            }
        });

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID USAUARIO", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblUsuarios);

        roles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        roles.setText("ROLES");

        cboRoles.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNuevo.setText("NUEVO");

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        Usuario1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Usuario1.setText("EMPLEADO");

        cboEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(roles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Contrasenia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Usuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Usuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtUsuario)
                                .addComponent(txtContrasenia)
                                .addComponent(cboRoles, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(58, 58, 58)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnActualizar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86)))
                .addContainerGap(39, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Usuario1)
                            .addComponent(cboEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Usuario)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Contrasenia)
                            .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(roles)
                            .addComponent(cboRoles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnEliminar)
                    .addComponent(btnCerrar))
                .addContainerGap(270, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
       BotonActualizar();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        BotonGuardar();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        BotonEliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
            dispose();       
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtContraseniaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContraseniaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContraseniaActionPerformed

    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(() -> new FrmUsuarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Contrasenia;
    private javax.swing.JLabel Usuario;
    private javax.swing.JLabel Usuario1;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboEmpleado;
    private javax.swing.JComboBox<String> cboRoles;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel roles;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
