
package sistemacine.vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.controlador.FuncionesController;
import sistemacine.modelo.Conexion;
import sistemacine.modelo.Funcion;
import sistemacine.modelo.FuncionesDAO;

public final class FrmFunciones extends javax.swing.JInternalFrame{
    
    private final FuncionesController controlador;
    public FrmFunciones() {
         initComponents();
         limpiarCampos();
         cargarSiguienteId();
         FuncionesDAO modelo = new FuncionesDAO();
         controlador = new FuncionesController(this, modelo);
    }
  private void cargarPeliculas() {
    cboPelicula.removeAllItems();
    String sql = "SELECT pelicula_id, titulo FROM peliculas";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            int id = rs.getInt("pelicula_id");
            String titulo = rs.getString("titulo");           
            cboPelicula.addItem(id + " - " + titulo);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error cargando películas: " + e.getMessage());
    }
}
private void cargarSalas() {
    cboSala.removeAllItems();
    String sql = "SELECT sala_id, nombre FROM salas";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            int id = rs.getInt("sala_id");
            String nombre = rs.getString("nombre");
            cboSala.addItem(id + " - " + nombre);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error cargando salas: " + e.getMessage());
    }
}


private void cargarSiguienteId() {
    FuncionesDAO id = new FuncionesDAO();
    try {
        int siguienteId = id.obtenerSiguienteId();
        txtIdFunciones.setText(String.valueOf(siguienteId));
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al obtener ID: " + ex.getMessage());
    }
}
private void cargarDatosDeFilaSeleccionada() {
    int fila = TablaFunciones.getSelectedRow();
    if (fila >= 0) {
        txtIdFunciones.setText(TablaFunciones.getValueAt(fila, 0).toString()); 
        cboPelicula.setSelectedItem(TablaFunciones.getValueAt(fila, 1).toString());     
         String fechaCompleta = TablaFunciones.getValueAt(fila, 2).toString();
        String[] partesFecha = fechaCompleta.split("-");
        if (partesFecha.length == 3) {
            cboAnio.setSelectedItem(partesFecha[0]);
            cboMes.setSelectedItem(partesFecha[1]);
            cboDia.setSelectedItem(partesFecha[2]);
        }
        String horaCompleta = TablaFunciones.getValueAt(fila, 3).toString();
        String[] partesHora = horaCompleta.split(":");
        if (partesHora.length == 3) {
            cboHora.setSelectedItem(partesHora[0]);
            cboMinuto.setSelectedItem(partesHora[1]);
            cboSegundo.setSelectedItem(partesHora[2]);
        }          
        cboSala.setSelectedItem(TablaFunciones.getValueAt(fila, 4).toString());
        cboPromociones.setSelectedItem(TablaFunciones.getValueAt(fila, 5).toString()); 
        btnGuardar.setEnabled(false);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
        txtIdFunciones.setEditable(false);
    }
}


public Funcion getFuncionDesdeFormulario() {
    Funcion f = new Funcion();
    if (!txtIdFunciones.getText().isEmpty()) {
        f.setId(Integer.parseInt(txtIdFunciones.getText()));
    }
    f.setpelicula(cboPelicula.getSelectedItem().toString());

    int anio = Integer.parseInt(cboAnio.getSelectedItem().toString());
    int mes = Integer.parseInt(cboMes.getSelectedItem().toString());
    int dia = Integer.parseInt(cboDia.getSelectedItem().toString());
    LocalDate fecha = LocalDate.of(anio, mes, dia);
    f.setFecha(fecha); 
    
    String hora = cboHora.getSelectedItem().toString();
    String minuto = cboMinuto.getSelectedItem().toString();
    String segundo = cboSegundo.getSelectedItem().toString();
    if (hora.length() == 1) hora = "0" + hora;
    if (minuto.length() == 1) minuto = "0" + minuto;
    if (segundo.length() == 1) segundo = "0" + segundo;
    String horaCompleta = hora + ":" + minuto + ":" + segundo;
    java.sql.Time horaSQL = java.sql.Time.valueOf(horaCompleta);
    f.setHora(horaSQL);    

    f.setsala(cboSala.getSelectedItem().toString());
    f.setpromociones(cboPromociones.getSelectedItem().toString());
    return f;
}


public void llenarTabla(List<Funcion> Funciones) {
    DefaultTableModel modelo = (DefaultTableModel) TablaFunciones.getModel();
    modelo.setRowCount(0); 
    for (Funcion f : Funciones) {
        modelo.addRow(new Object[] {
            f.getId(), f.getpelicula(), f.getfecha(), f.gethora(), f.getsala(), f.getpromociones()
        });
    }
}
private void cargarPromociones() {
    cboPromociones.removeAllItems();
    String sql = "SELECT promocion_id, descripcion FROM promociones";
    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        cboPromociones.addItem("0 - Sin promoción"); 
        while (rs.next()) {
            int id = rs.getInt("promocion_id");
            String descripcion = rs.getString("descripcion");
            cboPromociones.addItem(id + " - " + descripcion);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error cargando promociones: " + e.getMessage());
    }
}

public void limpiarCampos() {
    txtIdFunciones.setText("");      
    cboSala.setSelectedIndex(0);
    cboPelicula.setSelectedIndex(0);
    cboPromociones.setSelectedIndex(0);
}

public void mostrarMensaje(String mensaje) {
    JOptionPane.showMessageDialog(this, mensaje);
}
 

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblFuncion = new javax.swing.JLabel();
        lblPelicula = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        lblSala = new javax.swing.JLabel();
        txtIdFunciones = new javax.swing.JTextField();
        cboPelicula = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaFunciones = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        lblSala1 = new javax.swing.JLabel();
        cboPromociones = new javax.swing.JComboBox<>();
        cboSala = new javax.swing.JComboBox<>();
        cboAnio = new javax.swing.JComboBox<>();
        cboMes = new javax.swing.JComboBox<>();
        cboDia = new javax.swing.JComboBox<>();
        cboHora = new javax.swing.JComboBox<>();
        cboMinuto = new javax.swing.JComboBox<>();
        cboSegundo = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("GESTION DE FUNCIONES");

        lblFuncion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFuncion.setText("ID FUNCION");

        lblPelicula.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblPelicula.setText("PELICULA");

        lblFecha.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblFecha.setText("FECHA");

        lblHora.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblHora.setText("HORA");

        lblSala.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSala.setText("SALA");

        txtIdFunciones.setText("jTextField1");
        txtIdFunciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdFuncionesActionPerformed(evt);
            }
        });

        cboPelicula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeliculaActionPerformed(evt);
            }
        });

        TablaFunciones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID FUNCION", "PELICULA", "FECHA", "HORA", "SALA", "PROMOCIONES"
            }
        ));
        TablaFunciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaFuncionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TablaFunciones);

        btnGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCerrar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
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

        lblSala1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblSala1.setText("PROMOCIONES");

        cboPromociones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboPromociones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPromocionesActionPerformed(evt);
            }
        });

        cboSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSalaActionPerformed(evt);
            }
        });

        cboAnio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAnioActionPerformed(evt);
            }
        });

        cboMes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMesActionPerformed(evt);
            }
        });

        cboDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDiaActionPerformed(evt);
            }
        });

        cboHora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHoraActionPerformed(evt);
            }
        });

        cboMinuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMinutoActionPerformed(evt);
            }
        });

        cboSegundo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSegundoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblFecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHora, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPelicula, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblFuncion, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSala, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblSala1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtIdFunciones)
                    .addComponent(cboPelicula, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboHora, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cboAnio, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboMes, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboMinuto, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboSegundo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDia, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(cboSala, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboPromociones, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(183, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFuncion)
                            .addComponent(txtIdFunciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPelicula)
                            .addComponent(cboPelicula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFecha)
                            .addComponent(cboAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHora)
                            .addComponent(cboHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboMinuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboSegundo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSala)
                            .addComponent(cboSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSala1)
                            .addComponent(cboPromociones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(93, 93, 93)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(btnEliminar))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnEditar)
                        .addComponent(btnLimpiar)
                        .addComponent(btnCerrar)
                        .addComponent(btnNuevo)
                        .addComponent(btnGuardar)))
                .addGap(71, 71, 71))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdFuncionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdFuncionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdFuncionesActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
            controlador.guardarFunciones();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
            controlador.actualizarFunciones();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
            btnGuardar.setEnabled(true); 
            FuncionesDAO id = new FuncionesDAO();
            try {
            int siguienteId = id.obtenerSiguienteId();
            txtIdFunciones.setText(String.valueOf(siguienteId));
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID: " + ex.getMessage());
            }
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
            int fila = TablaFunciones.getSelectedRow();
            if (fila == -1) {
            mostrarMensaje("Seleccione una funcion para eliminar.");
            return;
             }
            int id = Integer.parseInt(TablaFunciones.getValueAt(fila, 0).toString());
            controlador.eliminarFunciones(id);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cboPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeliculaActionPerformed
            cargarPeliculas();
    }//GEN-LAST:event_cboPeliculaActionPerformed

    private void cboPromocionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPromocionesActionPerformed
        cargarPromociones();
    }//GEN-LAST:event_cboPromocionesActionPerformed

    private void cboSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSalaActionPerformed
        cargarSalas();
    }//GEN-LAST:event_cboSalaActionPerformed

    private void cboAnioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAnioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboAnioActionPerformed

    private void cboMesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMesActionPerformed

    private void cboDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboDiaActionPerformed

    private void cboHoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHoraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboHoraActionPerformed

    private void cboMinutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinutoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboMinutoActionPerformed

    private void cboSegundoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSegundoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboSegundoActionPerformed

    private void TablaFuncionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaFuncionesMouseClicked
       
    }//GEN-LAST:event_TablaFuncionesMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaFunciones;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cboAnio;
    private javax.swing.JComboBox<String> cboDia;
    private javax.swing.JComboBox<String> cboHora;
    private javax.swing.JComboBox<String> cboMes;
    private javax.swing.JComboBox<String> cboMinuto;
    private javax.swing.JComboBox<String> cboPelicula;
    private javax.swing.JComboBox<String> cboPromociones;
    private javax.swing.JComboBox<String> cboSala;
    private javax.swing.JComboBox<String> cboSegundo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblFuncion;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblPelicula;
    private javax.swing.JLabel lblSala;
    private javax.swing.JLabel lblSala1;
    private javax.swing.JTextField txtIdFunciones;
    // End of variables declaration//GEN-END:variables

}
