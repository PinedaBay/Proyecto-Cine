package sistemacine.vista;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
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
    for (JInternalFrame frame : desktopPane.getAllFrames()) {
        frame.dispose(); 
    }
    desktopPane.add(nuevoFormulario);
    nuevoFormulario.setVisible(true);
    int x = (desktopPane.getWidth() - nuevoFormulario.getWidth())/2;
    int y = (desktopPane.getHeight() - nuevoFormulario.getHeight())/2;
    nuevoFormulario.setLocation(x, y);
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuPeliculas = new javax.swing.JMenu();
        menuFunciones = new javax.swing.JMenu();
        menuSalas = new javax.swing.JMenu();
        menuEmpleados = new javax.swing.JMenu();
        menuVentas = new javax.swing.JMenu();
        menuUsuarios = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktopPane.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout desktopPaneLayout = new javax.swing.GroupLayout(desktopPane);
        desktopPane.setLayout(desktopPaneLayout);
        desktopPaneLayout.setHorizontalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 908, Short.MAX_VALUE)
        );
        desktopPaneLayout.setVerticalGroup(
            desktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 605, Short.MAX_VALUE)
        );

        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);

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

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVentasActionPerformed
        FrmVentaBoletos v = new FrmVentaBoletos();
        desktopPane.add(v);
        v.setVisible(true);
    }//GEN-LAST:event_menuVentasActionPerformed

    private void menuEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEmpleadosActionPerformed
        FrmEmpleado e = new FrmEmpleado();
        desktopPane.add(e);
        e.setVisible(true);
    }//GEN-LAST:event_menuEmpleadosActionPerformed

    private void menuSalasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSalasActionPerformed
        FrmSalas s = new FrmSalas();
        desktopPane.add(s);
        s.setVisible(true);
    }//GEN-LAST:event_menuSalasActionPerformed

    private void menuFuncionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFuncionesActionPerformed
        FrmFunciones f = new FrmFunciones();
        desktopPane.add(f);
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
        desktopPane.add(u);
        u.setVisible(true);
    }//GEN-LAST:event_menuUsuariosActionPerformed

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
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu menuEmpleados;
    private javax.swing.JMenu menuFunciones;
    private javax.swing.JMenu menuPeliculas;
    private javax.swing.JMenu menuSalas;
    private javax.swing.JMenu menuUsuarios;
    private javax.swing.JMenu menuVentas;
    // End of variables declaration//GEN-END:variables
}
