
package sistemacine.vista;
import java.awt.HeadlessException;
import java.awt.Image;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import sistemacine.modelo.Conexion;

public class FrmLogin extends javax.swing.JFrame{
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmLogin.class.getName());   
    public FrmLogin() {
        initComponents();
        Icon mIcono = new ImageIcon(new ImageIcon(getClass().getResource("/Imagenes/acceso.png")).getImage()
        .getScaledInstance(lblIcono.getWidth(), lblIcono.getHeight(),0));
        lblIcono.setIcon(mIcono);
        this.setLocationRelativeTo(null);          
       
    }
    

    public void Botonlogin() {
        String usuario = txtUsuario.getText();
        String Contrasenia = new String(txtContrasenia.getPassword());
        String passHash = hashContrasenia(Contrasenia);

    try {
        Connection con = Conexion.getConexion(); 
        String sql = "SELECT u.usuario_id, u.NombreUsuario, r.nombre_rol " +
                     "FROM usuarios u " +
                     "JOIN usuarios_roles ur ON u.usuario_id = ur.usuario_id " +
                     "JOIN roles r ON ur.rol_id = r.rol_id " +
                     "WHERE u.NombreUsuario = ? AND u.Contrasenia = ? AND u.activo = 1";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, passHash);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            SesionUsuario s = new SesionUsuario();
            SesionUsuario.usuarioId = rs.getInt("usuario_id");
            SesionUsuario.nombreUsuario = rs.getString("NombreUsuario");
            SesionUsuario.rol = rs.getString("nombre_rol");
            FrmPrincipal principal = new FrmPrincipal();
            principal.setVisible(true);
            this.dispose();
} else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
        }

    } catch (HeadlessException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error en la conexión o consulta.");
    }    
    }
    
    public static String hashContrasenia(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error al encriptar contraseña", e);
    }
}    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Icono = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        txtContrasenia = new javax.swing.JPasswordField();
        lblIcono = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 153, 255));
        setForeground(new java.awt.Color(0, 102, 204));

        Icono.setBackground(new java.awt.Color(20, 24, 24));
        Icono.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("USUARIO");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("CONTRASEÑA");

        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        btnLogin.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnLogin.setText("LOGIN");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        btnLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLoginKeyPressed(evt);
            }
        });

        lblIcono.setText("jLabel1");
        lblIcono.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblIconoAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(lblIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Icono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(130, 130, 130))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUsuario)
                            .addComponent(txtContrasenia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(Icono, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(32, Short.MAX_VALUE)
                        .addComponent(lblIcono, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContrasenia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(62, 62, 62)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        Botonlogin();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLoginKeyPressed
       Botonlogin();
    }//GEN-LAST:event_btnLoginKeyPressed

    private void lblIconoAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_lblIconoAncestorAdded
        
        
    }//GEN-LAST:event_lblIconoAncestorAdded
public static void main(String args[]) {         
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }      
    } 

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Icono;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblIcono;
    private javax.swing.JPasswordField txtContrasenia;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
