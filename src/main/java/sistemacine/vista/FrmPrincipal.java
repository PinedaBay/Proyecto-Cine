package sistemacine.vista;
import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import sistemacine.modelo.JPanelImage;
public class FrmPrincipal extends javax.swing.JFrame {    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPrincipal.class.getName());
    public FrmPrincipal() {
        initComponents();
        configurarMenusSegunRol();
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);        
    }
    
    private void configurarMenusSegunRol() {
    String rol = SesionUsuario.rol;
    menuPeliculas.setVisible(false);
    menuFunciones.setVisible(false);
    menuSalas.setVisible(false);
    menuEmpleados.setVisible(false);
    menuVentas.setVisible(false);
    menuUsuarios.setVisible(false);
    switch (rol) {
        case "Administrador" -> {
            menuPeliculas.setVisible(true);
            menuFunciones.setVisible(true);
            menuSalas.setVisible(true);
            menuEmpleados.setVisible(true);
            menuVentas.setVisible(true);
            menuUsuarios.setVisible(true);
            }
        case "Cajero" -> menuVentas.setVisible(true);
        case "Taquillero" -> menuVentas.setVisible(true);
        case "Gerente" -> {menuSalas.setVisible(true);
                          menuFunciones.setVisible(true);}
        case "Supervisor" -> {menuSalas.setVisible(true);
                          menuFunciones.setVisible(true);}
        case "Asistente" -> {menuSalas.setVisible(true);
                            menuFunciones.setVisible(true);
                            menuVentas.setVisible(true);}                          
      default -> JOptionPane.showMessageDialog(this, "Rol desconocido: " + rol);
    }
    } 
private void abrirFormularioUnico(JInternalFrame nuevoFormulario) {
    for (JInternalFrame frame : pnlPrincipal.getAllFrames()) {
        frame.dispose(); 
    }    
    pnlPrincipal.add(nuevoFormulario);

    // Maximizar y quitar título
    nuevoFormulario.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
    ((javax.swing.plaf.basic.BasicInternalFrameUI)nuevoFormulario.getUI()).setNorthPane(null);
    nuevoFormulario.setBounds(0, 0, pnlPrincipal.getWidth(), pnlPrincipal.getHeight());
    nuevoFormulario.setVisible(true);
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuPeliculas = new javax.swing.JMenu();
        menuFunciones = new javax.swing.JMenu();
        menuSalas = new javax.swing.JMenu();
        menuEmpleados = new javax.swing.JMenu();
        menuVentas = new javax.swing.JMenu();
        menuUsuarios = new javax.swing.JMenu();
        CerrarSesion = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pnlPrincipal.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                pnlPrincipalAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 908, Short.MAX_VALUE)
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 605, Short.MAX_VALUE)
        );

        getContentPane().add(pnlPrincipal, java.awt.BorderLayout.CENTER);

        jMenuBar1.setForeground(new java.awt.Color(51, 102, 255));

        menuPeliculas.setText("Peliculas");
        menuPeliculas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuPeliculasMouseClicked(evt);
            }
        });
        menuPeliculas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPeliculasActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuPeliculas);

        menuFunciones.setText("Funciones");
        menuFunciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuFuncionesMouseClicked(evt);
            }
        });
        menuFunciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFuncionesActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuFunciones);

        menuSalas.setText("Salas");
        menuSalas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuSalasMouseClicked(evt);
            }
        });
        menuSalas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSalasActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuSalas);

        menuEmpleados.setText("Empleados");
        menuEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuEmpleadosMouseClicked(evt);
            }
        });
        menuEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEmpleadosActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuEmpleados);

        menuVentas.setText("Venta ");
        menuVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuVentasMouseClicked(evt);
            }
        });
        menuVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVentasActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuVentas);

        menuUsuarios.setText("Usuarios");
        menuUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuUsuariosMouseClicked(evt);
            }
        });
        menuUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUsuariosActionPerformed(evt);
            }
        });
        jMenuBar1.add(menuUsuarios);

        CerrarSesion.setText("Cerrar Sesion");
        CerrarSesion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CerrarSesionMouseClicked(evt);
            }
        });
        CerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CerrarSesionActionPerformed(evt);
            }
        });
        jMenuBar1.add(CerrarSesion);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasActionPerformed
        FrmVentaBoletos v = new FrmVentaBoletos();
        pnlPrincipal.add(v);
        v.setVisible(true);
    }//GEN-LAST:event_menuVentasActionPerformed

    private void menuEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmpleadosActionPerformed
        FrmEmpleado e = new FrmEmpleado();
        pnlPrincipal.add(e);
        e.setVisible(true);
    }//GEN-LAST:event_menuEmpleadosActionPerformed

    private void menuSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalasActionPerformed
        FrmSalas s = new FrmSalas();
        pnlPrincipal.add(s);
        s.setVisible(true);
    }//GEN-LAST:event_menuSalasActionPerformed

    private void menuFuncionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFuncionesActionPerformed
        FrmFunciones f = new FrmFunciones();
        pnlPrincipal.add(f);
        f.setVisible(true);
    }//GEN-LAST:event_menuFuncionesActionPerformed

    private void menuPeliculasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPeliculasActionPerformed
       // abrirFormularioUnico(new FrmPeliculas());
    }//GEN-LAST:event_menuPeliculasActionPerformed

    private void menuPeliculasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuPeliculasMouseClicked
        abrirFormularioUnico(new FrmPeliculas());
    }//GEN-LAST:event_menuPeliculasMouseClicked

    private void menuFuncionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuFuncionesMouseClicked
        abrirFormularioUnico(new FrmFunciones());
    }//GEN-LAST:event_menuFuncionesMouseClicked

    private void menuSalasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuSalasMouseClicked
        abrirFormularioUnico(new FrmSalas());
    }//GEN-LAST:event_menuSalasMouseClicked

    private void menuEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuEmpleadosMouseClicked
        abrirFormularioUnico(new FrmEmpleado());
    }//GEN-LAST:event_menuEmpleadosMouseClicked

    private void menuVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuVentasMouseClicked
        abrirFormularioUnico(new FrmVentaBoletos());
    }//GEN-LAST:event_menuVentasMouseClicked

    private void menuUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuUsuariosMouseClicked
       abrirFormularioUnico(new FrmUsuarios());
    }//GEN-LAST:event_menuUsuariosMouseClicked

    private void menuUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUsuariosActionPerformed
        FrmUsuarios u = new FrmUsuarios();
        pnlPrincipal.add(u);
        u.setVisible(true);
    }//GEN-LAST:event_menuUsuariosActionPerformed

    private void pnlPrincipalAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_pnlPrincipalAncestorAdded
        JPanelImage mImagen = new JPanelImage(pnlPrincipal, "/Imagenes/Pantalla Principal.jpg");
        pnlPrincipal.add(mImagen, BorderLayout.CENTER);
        pnlPrincipal.repaint();
    }//GEN-LAST:event_pnlPrincipalAncestorAdded

    private void CerrarSesionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CerrarSesionMouseClicked
          int confirm = JOptionPane.showConfirmDialog(
        this, 
        "¿Seguro que deseas cerrar sesión?", 
        "Confirmar", 
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        // Limpiar datos de sesión
        SesionUsuario.rol = null; 
        SesionUsuario.nombreUsuario = null;

        // Cerrar ventana principal
        this.dispose();

        // Abrir ventana de login
        new FrmLogin().setVisible(true);
    }
    }//GEN-LAST:event_CerrarSesionMouseClicked

    private void CerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CerrarSesionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CerrarSesionActionPerformed

    public static void main(String args[]) {
         new FrmLogin().setVisible(true);
         
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
    private javax.swing.JMenu CerrarSesion;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu menuEmpleados;
    private javax.swing.JMenu menuFunciones;
    private javax.swing.JMenu menuPeliculas;
    private javax.swing.JMenu menuSalas;
    private javax.swing.JMenu menuUsuarios;
    private javax.swing.JMenu menuVentas;
    private javax.swing.JDesktopPane pnlPrincipal;
    // End of variables declaration//GEN-END:variables
}
