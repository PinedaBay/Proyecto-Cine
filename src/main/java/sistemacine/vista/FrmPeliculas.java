
package sistemacine.vista;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.controlador.PeliculasController;
import sistemacine.modelo.Pelicula;
import sistemacine.modelo.PeliculasDAO;

public final class FrmPeliculas extends javax.swing.JInternalFrame {    

    private final PeliculasController controlador;
    public FrmPeliculas() {
        initComponents();
        limpiarCampos();
        cargarSiguienteId();
        PeliculasDAO modelo = new PeliculasDAO();
        controlador = new PeliculasController(this, modelo);
    }
private void cargarSiguienteId() {
    PeliculasDAO dao = new PeliculasDAO();
    try {
        int siguienteId = dao.obtenerSiguienteId();
        txtId.setText(String.valueOf(siguienteId));
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al obtener ID: " + ex.getMessage());
    }
}
private void cargarDatosDeFilaSeleccionada() {
    int fila = TablaPeliculas.getSelectedRow();
    if (fila >= 0) {
        txtId.setText(TablaPeliculas.getValueAt(fila, 0).toString()); 
        txtTitulo.setText(TablaPeliculas.getValueAt(fila, 1).toString());     
        txtDuracion.setText(TablaPeliculas.getValueAt(fila, 2).toString());   
        txtSinopsis.setText(TablaPeliculas.getValueAt(fila, 3).toString());   
        txtClasificacion.setText(TablaPeliculas.getValueAt(fila, 4).toString());
        // Opcional: Desactivar guardar y activar actualizar/eliminar
        btnGuardar.setEnabled(false);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        txtId.setEditable(false);
    }
}

public Pelicula getPeliculaDesdeFormulario() {
    Pelicula p = new Pelicula();
    if (!txtId.getText().isEmpty()) {
        p.setId(Integer.parseInt(txtId.getText()));
    }
    p.setTitulo(txtTitulo.getText());
    p.setDuracion(Integer.parseInt(txtDuracion.getText()));
    p.setSinopsis(txtSinopsis.getText());
    p.setClasificacion(txtClasificacion.getText());
    return p;
}

public void llenarTabla(List<Pelicula> peliculas) {
    DefaultTableModel modelo = (DefaultTableModel) TablaPeliculas.getModel();
    modelo.setRowCount(0); 
    for (Pelicula p : peliculas) {
        modelo.addRow(new Object[] {
            p.getId(), p.getTitulo(), p.getDuracion(), p.getSinopsis(), p.getClasificacion()
        });
    }
}
public void limpiarCampos() {
    txtId.setText("");
    txtTitulo.setText("");
    txtDuracion.setText("");
    txtSinopsis.setText("");
    txtClasificacion.setText("");
}

public void mostrarMensaje(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje);
}
   
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitulo = new javax.swing.JLabel();
        lblDuracion = new javax.swing.JLabel();
        lblClasificacion = new javax.swing.JLabel();
        lblSonopsis = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        txtDuracion = new javax.swing.JTextField();
        txtClasificacion = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtSinopsis = new javax.swing.JTextArea();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaPeliculas = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        lblTitulo1 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitulo.setText("TITULO");

        lblDuracion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblDuracion.setText("DURACION");

        lblClasificacion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblClasificacion.setText("CLASIFICACION");

        lblSonopsis.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSonopsis.setText("SINOPSIS");

        txtTitulo.setText("jTextField1");
        txtTitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTituloActionPerformed(evt);
            }
        });

        txtDuracion.setText("jTextField1");
        txtDuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDuracionActionPerformed(evt);
            }
        });

        txtClasificacion.setText("jTextField1");
        txtClasificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClasificacionActionPerformed(evt);
            }
        });

        txtSinopsis.setColumns(20);
        txtSinopsis.setLineWrap(true);
        txtSinopsis.setRows(5);
        txtSinopsis.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtSinopsis);

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnEditar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnEditar.setText("ACTUALIZAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        TablaPeliculas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID PELICULA", "TITULO", "DURACION", "SINOPSIS", "CLASIFICACION"
            }
        ));
        TablaPeliculas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaPeliculasMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaPeliculasMousePressed(evt);
            }
        });
        TablaPeliculas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaPeliculasKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(TablaPeliculas);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("GESTION DE PELICULAS PARA ADMINISTRADOR");

        lblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTitulo1.setText("ID");

        txtId.setEditable(false);
        txtId.setText("jTextField1");
        txtId.setDisabledTextColor(new java.awt.Color(204, 204, 204));
        txtId.setOpaque(true);
        txtId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnNuevo.setText("NUEVO");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(510, 510, 510)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(8, 8, 8)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(txtClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblSonopsis, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 862, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTitulo1))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTitulo))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDuracion)
                            .addComponent(txtDuracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtClasificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblClasificacion))
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(lblSonopsis))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnCerrar)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtDuracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDuracionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDuracionActionPerformed

    private void txtClasificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClasificacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClasificacionActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
            controlador.guardarPelicula();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
            limpiarCampos();
         
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtTituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTituloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTituloActionPerformed

    private void txtIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
            controlador.actualizarPelicula();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        btnGuardar.setEnabled(true); 
         PeliculasDAO dao = new PeliculasDAO();
         try {
        int siguienteId = dao.obtenerSiguienteId();
        txtId.setText(String.valueOf(siguienteId));
        } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al obtener ID: " + ex.getMessage());
         }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
            int fila = TablaPeliculas.getSelectedRow();
            if (fila == -1) {
            mostrarMensaje("Seleccione una película para eliminar.");
            return;
             }
            int id = Integer.parseInt(TablaPeliculas.getValueAt(fila, 0).toString());
            controlador.eliminarPelicula(id);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void TablaPeliculasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaPeliculasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TablaPeliculasKeyPressed

    private void TablaPeliculasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPeliculasMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TablaPeliculasMousePressed

    private void TablaPeliculasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPeliculasMouseClicked
            cargarDatosDeFilaSeleccionada();
    }//GEN-LAST:event_TablaPeliculasMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTable TablaPeliculas;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    public javax.swing.JButton btnNuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblClasificacion;
    private javax.swing.JLabel lblDuracion;
    private javax.swing.JLabel lblSonopsis;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JTextField txtClasificacion;
    private javax.swing.JTextField txtDuracion;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextArea txtSinopsis;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables

    public Object getTablaPeliculas() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
