
package sistemacine.vista;

import java.awt.HeadlessException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.modelo.Conexion;

public class FrmUsuarios extends javax.swing.JInternalFrame {  

    public FrmUsuarios() {
        initComponents();
        txtIdRol.setEditable(false);
        txtRoles.setEditable(false);
        cargarEmpleados();
        cargarTablaUsuarios();
    }
   private final Map<String, Integer> empleadoMap = new HashMap<>();

private void cargarEmpleados() {
    try (Connection con = Conexion.getConexion()) {
        String sql = "SELECT empleado_id, nombre FROM empleados";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();        
        while (rs.next()) {
            int id = rs.getInt("empleado_id");
            String nombre = rs.getString("nombre");
            empleadoMap.put(nombre, id);
            cboEmpleados.addItem(nombre);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar empleados: " + e.getMessage());
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

private void BotonGuardar(){
    Connection con = null;
        try {
            con = Conexion.getConexion();
        } catch (SQLException ex) {
            System.getLogger(FrmUsuarios.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    try {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasenia.getPassword()).trim();
        String rolIdText = txtIdRol.getText().trim();
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo usuario y contraseña no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (contrasena.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!usuario.matches("[a-zA-Z0-9_]+")) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario solo puede contener letras, números y guiones bajos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (cboEmpleados.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (rolIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El empleado seleccionado no tiene un rol asignado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sqlCheckUsuario = "SELECT COUNT(*) FROM usuarios WHERE NombreUsuario = ?";
        PreparedStatement psCheckUsuario = con.prepareStatement(sqlCheckUsuario);
        psCheckUsuario.setString(1, usuario);
        ResultSet rsUsuario = psCheckUsuario.executeQuery();
        if (rsUsuario.next() && rsUsuario.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "El nombre de usuario ya existe. Por favor elige otro.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String empleadoSeleccionado = (String) cboEmpleados.getSelectedItem();
        int idEmpleado = empleadoMap.getOrDefault(empleadoSeleccionado, -1);
        if (idEmpleado == -1) {
            JOptionPane.showMessageDialog(this, "Error al obtener el ID del empleado seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sqlCheckEmpleado = "SELECT COUNT(*) FROM usuarios WHERE empleado_id = ?";
        PreparedStatement psCheckEmpleado = con.prepareStatement(sqlCheckEmpleado);
        psCheckEmpleado.setInt(1, idEmpleado);
        ResultSet rsEmpleado = psCheckEmpleado.executeQuery();
        if (rsEmpleado.next() && rsEmpleado.getInt(1) > 0) {
            JOptionPane.showMessageDialog(this, "Este empleado ya tiene un usuario asignado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔐 Encriptar la contraseña (SHA-256)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(contrasena.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        String contrasenaEncriptada = sb.toString();

        // ✅ Insertar usuario
        String sqlInsertUsuario = "INSERT INTO usuarios (NombreUsuario, Contraseña, empleado_id) VALUES (?, ?, ?)";
        PreparedStatement psInsertUsuario = con.prepareStatement(sqlInsertUsuario, Statement.RETURN_GENERATED_KEYS);
        psInsertUsuario.setString(1, usuario);
        psInsertUsuario.setString(2, contrasenaEncriptada);
        psInsertUsuario.setInt(3, idEmpleado);
        psInsertUsuario.executeUpdate();

        // Obtener ID generado del usuario
        ResultSet rsKeys = psInsertUsuario.getGeneratedKeys();
        int usuarioId = -1;
        if (rsKeys.next()) {
            usuarioId = rsKeys.getInt(1);
        }

        // Insertar en tabla usuarios_roles
        int rolId = Integer.parseInt(txtIdRol.getText().trim());
        String sqlInsertRol = "INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (?, ?)";
        PreparedStatement psInsertRol = con.prepareStatement(sqlInsertRol);
        psInsertRol.setInt(1, usuarioId);
        psInsertRol.setInt(2, rolId);
        psInsertRol.executeUpdate();

        JOptionPane.showMessageDialog(this, "Usuario guardado correctamente.");
        limpiarCampos();
        cargarEmpleados();

    } catch (SQLException | HeadlessException | NumberFormatException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar el usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo usuario no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection con = Conexion.getConexion()) {
            String sql;
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
            String sqlRolId = "SELECT rol_id FROM roles WHERE nombre_rol = ?";
            PreparedStatement psRol = con.prepareStatement(sqlRolId);            
            ResultSet rsRol = psRol.executeQuery();
            int rolId = 0;
            if (rsRol.next()) {
                rolId = rsRol.getInt("rol_id");
            }
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
    }

private void cargarRolDesdeEmpleado(int empleadoId) {
    try (Connection con = Conexion.getConexion()) {
        String sql = "SELECT cargo FROM empleados WHERE empleado_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, empleadoId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String cargo = rs.getString("cargo");
            sql = "SELECT rol_id, nombre_rol FROM roles WHERE nombre_rol = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, cargo);
            rs = ps.executeQuery();

            if (rs.next()) {
                int rolId = rs.getInt("rol_id");
                String nombreRol = rs.getString("nombre_rol");

                txtIdRol.setText(String.valueOf(rolId));
                txtRoles.setText(nombreRol);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró un rol para el cargo: " + cargo);
                txtIdRol.setText("");
                txtRoles.setText("");
            }
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener rol: " + e.getMessage());
        txtIdRol.setText("");
        txtRoles.setText("");
    }
}
private void FilaSeleccionada() {
    int fila = tblUsuarios.getSelectedRow();
    if (fila >= 0) {
        String nombreUsuario = tblUsuarios.getValueAt(fila, 1).toString();
        txtUsuario.setText(nombreUsuario);
        txtContrasenia.setText("");String nombreRol = tblUsuarios.getValueAt(fila, 2).toString();
        txtRoles.setText(nombreRol);
        try (Connection con = Conexion.getConexion()) {
            String sql = "SELECT rol_id FROM roles WHERE nombre_rol = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombreRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                txtIdRol.setText(String.valueOf(rs.getInt("rol_id")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID de rol: " + e.getMessage());
        }
    }
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblUsuarios = new javax.swing.JLabel();
        lblContrasenia = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtContrasenia = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        lblRoles = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        lblEmpleados = new javax.swing.JLabel();
        cboEmpleados = new javax.swing.JComboBox<>();
        lblIdRol = new javax.swing.JLabel();
        txtIdRol = new javax.swing.JTextField();
        txtRoles = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("GESTION DE USUARIOS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 10, 195, -1));

        lblUsuarios.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblUsuarios.setText("USUARIO");
        getContentPane().add(lblUsuarios, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 93, -1));

        lblContrasenia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblContrasenia.setText("CONTRASEÑA");
        getContentPane().add(lblContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 93, 10));

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 182, -1));

        txtContrasenia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContraseniaActionPerformed(evt);
            }
        });
        getContentPane().add(txtContrasenia, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 320, 182, 20));

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuarios);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 90, 850, 490));

        lblRoles.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblRoles.setText("ROLES");
        getContentPane().add(lblRoles, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 550, 93, -1));

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnNuevo.setText("NUEVO");
        getContentPane().add(btnNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 670, 100, -1));

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 670, -1, -1));

        btnActualizar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        getContentPane().add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 670, -1, -1));

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        getContentPane().add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 670, 105, -1));

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 670, 95, -1));

        lblEmpleados.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmpleados.setText("EMPLEADO");
        getContentPane().add(lblEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 93, -1));

        cboEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEmpleadosActionPerformed(evt);
            }
        });
        getContentPane().add(cboEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 100, 182, -1));

        lblIdRol.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblIdRol.setText("ID ROL");
        getContentPane().add(lblIdRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, 93, -1));

        txtIdRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdRolActionPerformed(evt);
            }
        });
        getContentPane().add(txtIdRol, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 430, 182, -1));

        txtRoles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRolesActionPerformed(evt);
            }
        });
        getContentPane().add(txtRoles, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 550, 182, -1));

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

    private void cboEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEmpleadosActionPerformed
        String seleccionado = (String) cboEmpleados.getSelectedItem();
        if (seleccionado != null && empleadoMap.containsKey(seleccionado)) {
        int empleadoId = empleadoMap.get(seleccionado);
        cargarRolDesdeEmpleado(empleadoId);
         }
    }//GEN-LAST:event_cboEmpleadosActionPerformed

    private void txtIdRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdRolActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRolActionPerformed

    private void txtRolesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRolesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRolesActionPerformed

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuariosMouseClicked
         FilaSeleccionada();
    }//GEN-LAST:event_tblUsuariosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboEmpleados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblContrasenia;
    private javax.swing.JLabel lblEmpleados;
    private javax.swing.JLabel lblIdRol;
    private javax.swing.JLabel lblRoles;
    private javax.swing.JLabel lblUsuarios;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtIdRol;
    private javax.swing.JTextField txtRoles;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
