
package sistemacine;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public final class FrmFunciones extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmFunciones.class.getName());

    public FrmFunciones() {
         initComponents();
         limpiarCampos();
         cargarPeliculas();
         cargarSalas();
         cargarPromociones();
         cargarTablaFunciones();
         mostrarSiguienteId();
         inicializarFechaYHora();         
         mostrarSiguienteId();  
    }
    private void seleccionarFuncionDeTabla() {
    int fila = tblFunciones.getSelectedRow();
    if (fila >= 0) {
        txtIdFunciones.setText(tblFunciones.getValueAt(fila, 0).toString());

        String fechaCompleta = tblFunciones.getValueAt(fila, 2).toString();
        String[] partesFecha = fechaCompleta.split("-");
        cboAnio.setSelectedItem(partesFecha[0]);
        cboMes.setSelectedItem(partesFecha[1]);
        cboDia.setSelectedItem(partesFecha[2]);

        String horaCompleta = tblFunciones.getValueAt(fila, 3).toString();
        String[] partesHora = horaCompleta.split(":");
        cboHora.setSelectedItem(partesHora[0]);
        cboMinuto.setSelectedItem(partesHora[1]);
        cboSegundo.setSelectedItem(partesHora[2]);

        String pelicula = tblFunciones.getValueAt(fila, 1).toString();
        for (int i = 0; i < cboPelicula.getItemCount(); i++) {
            if (cboPelicula.getItemAt(i).contains(pelicula)) {
                cboPelicula.setSelectedIndex(i);
                break;
            }
        }

        String sala = tblFunciones.getValueAt(fila, 4).toString();
        for (int i = 0; i < cboSala.getItemCount(); i++) {
            if (cboSala.getItemAt(i).contains(sala)) {
                cboSala.setSelectedIndex(i);
                break;
            }
        }

        String promo = tblFunciones.getValueAt(fila, 5).toString();
        for (int i = 0; i < cboPromociones.getItemCount(); i++) {
            if (cboPromociones.getItemAt(i).contains(promo)) {
                cboPromociones.setSelectedIndex(i);
                break;
            }
        }

        BTNGuardar.setEnabled(false);
    }
}
    
   private void inicializarFechaYHora() {
    cboAnio.removeAllItems();
    cboAnio.addItem("");
    for (int i = 2024; i <= 2030; i++) {
        cboAnio.addItem(String.valueOf(i));
    }

    cboMes.removeAllItems();
    cboMes.addItem("");
    for (int i = 1; i <= 12; i++) {
        cboMes.addItem(String.format("%02d", i));
    }

    cboDia.removeAllItems();
    cboDia.addItem("");
    for (int i = 1; i <= 31; i++) {
        cboDia.addItem(String.format("%02d", i));
    }

    cboHora.removeAllItems();
    cboHora.addItem("");
    for (int i = 0; i <= 23; i++) {
        cboHora.addItem(String.format("%02d", i));
    }

    cboMinuto.removeAllItems();
    cboMinuto.addItem("");
    for (int i = 0; i <= 59; i++) {
        cboMinuto.addItem(String.format("%02d", i));
    }

    cboSegundo.removeAllItems();
    cboSegundo.addItem("");
    for (int i = 0; i <= 59; i++) {
        cboSegundo.addItem(String.format("%02d", i));
    }
}
 
   public void mostrarSiguienteId() {
    String sql = "SELECT COALESCE(MAX(funcion_id), 0) + 1 AS siguienteId FROM funciones";

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            int siguienteId = rs.getInt("siguienteId");
            txtIdFunciones.setText(String.valueOf(siguienteId));
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al obtener el siguiente ID: " + e.getMessage());
    }
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
   
 private void cargarTablaFunciones() {
    DefaultTableModel modeloTabla = (DefaultTableModel) tblFunciones.getModel();
    modeloTabla.setRowCount(0); 

    String sql = """
        SELECT f.funcion_id,
               p.titulo AS pelicula,
               f.fecha,
               f.hora,
               s.nombre AS sala,
               pr.descripcion AS promocion
        FROM funciones f
        JOIN peliculas p ON f.pelicula_id = p.pelicula_id
        JOIN salas s ON f.sala_id = s.sala_id
        LEFT JOIN promociones pr ON f.promocion_id = pr.promocion_id
    """;

    try (Connection conn = Conexion.getConexion();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            modeloTabla.addRow(new Object[]{
                rs.getInt("funcion_id"),
                rs.getString("pelicula"),
                rs.getDate("fecha"),
                rs.getTime("hora"),
                rs.getString("sala"),
                rs.getString("promocion") != null ? rs.getString("promocion") : "Sin promoción"
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al cargar funciones: " + e.getMessage());
    }
}
 
private void guardarFuncion() {
    try {
        String fecha = cboAnio.getSelectedItem() + "-" +
                       cboMes.getSelectedItem() + "-" +
                       cboDia.getSelectedItem();

        String hora = cboHora.getSelectedItem() + ":" +
                      cboMinuto.getSelectedItem() + ":" +
                      cboSegundo.getSelectedItem();

        int peliculaId = Integer.parseInt(cboPelicula.getSelectedItem().toString().split(" - ")[0]);
        int salaId = Integer.parseInt(cboSala.getSelectedItem().toString().split(" - ")[0]);
        int promocionId = Integer.parseInt(cboPromociones.getSelectedItem().toString().split(" - ")[0]);
        Integer promocionIdFinal = (promocionId == 0) ? null : promocionId;

        String sql = "INSERT INTO funciones (pelicula_id, sala_id, fecha, hora, promocion_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, peliculaId);
            ps.setInt(2, salaId);
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.setTime(4, java.sql.Time.valueOf(hora));

            if (promocionIdFinal == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, promocionIdFinal);
            }

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Función guardada exitosamente.");
                cargarTablaFunciones();
                limpiarCampos();
                mostrarSiguienteId();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la función.");
            }
        }
    } catch (HeadlessException | NumberFormatException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar la función: " + e.getMessage());
    }
    
}


private void limpiarCampos() {
    txtIdFunciones.setText("");      
    cboSala.setSelectedIndex(0);
    cboPelicula.setSelectedIndex(0);
    cboPromociones.setSelectedIndex(0);
}

private void editarFuncion() {
    try {
        int funcionId = Integer.parseInt(txtIdFunciones.getText().trim());

        String fecha = cboAnio.getSelectedItem().toString() + "-" +
                       cboMes.getSelectedItem().toString() + "-" +
                       cboDia.getSelectedItem().toString();

        String hora = cboHora.getSelectedItem().toString() + ":" +
                      cboMinuto.getSelectedItem().toString() + ":" +
                      cboSegundo.getSelectedItem().toString();

        int peliculaId = Integer.parseInt(cboPelicula.getSelectedItem().toString().split(" - ")[0]);
        int salaId = Integer.parseInt(cboSala.getSelectedItem().toString().split(" - ")[0]);
        int promocionId = Integer.parseInt(cboPromociones.getSelectedItem().toString().split(" - ")[0]);

        Integer promocionIdFinal = (promocionId == 0) ? null : promocionId;

        String sql = "UPDATE funciones SET pelicula_id = ?, sala_id = ?, fecha = ?, hora = ?, promocion_id = ? WHERE funcion_id = ?";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, peliculaId);
            ps.setInt(2, salaId);
            ps.setDate(3, java.sql.Date.valueOf(fecha));
            ps.setTime(4, java.sql.Time.valueOf(hora));

            if (promocionIdFinal == null) {
                ps.setNull(5, java.sql.Types.INTEGER);
            } else {
                ps.setInt(5, promocionIdFinal);
            }

            ps.setInt(6, funcionId);

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Función actualizada correctamente.");
                cargarTablaFunciones();
                limpiarCampos();
                mostrarSiguienteId();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la función.");
            }
        }
    } catch (HeadlessException | NumberFormatException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al actualizar la función: " + e.getMessage());
    }
}
private void eliminarFuncion() {
    int filaSeleccionada = tblFunciones.getSelectedRow();
    
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione una función de la tabla para eliminar.");
        return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta función?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    
    if (confirmacion == JOptionPane.YES_OPTION) {
        try {
            int funcionId = Integer.parseInt(tblFunciones.getValueAt(filaSeleccionada, 0).toString());

            String sql = "DELETE FROM funciones WHERE funcion_id = ?";

            try (Connection conn = Conexion.getConexion();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, funcionId);

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Función eliminada correctamente.");
                    cargarTablaFunciones();
                    limpiarCampos();
                    mostrarSiguienteId();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la función.");
                }
            }
        } catch (HeadlessException | NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la función: " + e.getMessage());
        }
    }
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
        tblFunciones = new javax.swing.JTable();
        BTNGuardar = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        tblFunciones.setModel(new javax.swing.table.DefaultTableModel(
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
        tblFunciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFuncionesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFunciones);

        BTNGuardar.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        BTNGuardar.setText("GUARDAR");
        BTNGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNGuardarActionPerformed(evt);
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
                        .addComponent(BTNGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(BTNGuardar)))
                .addGap(71, 71, 71))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdFuncionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdFuncionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdFuncionesActionPerformed

    private void BTNGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNGuardarActionPerformed
        guardarFuncion();
    }//GEN-LAST:event_BTNGuardarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
            editarFuncion();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();

    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
            limpiarCampos();
            mostrarSiguienteId(); 
            BTNGuardar.setEnabled(true); 
            tblFunciones.clearSelection();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
            eliminarFuncion();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void cboPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeliculaActionPerformed
        
    }//GEN-LAST:event_cboPeliculaActionPerformed

    private void cboPromocionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPromocionesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPromocionesActionPerformed

    private void cboSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSalaActionPerformed
        // TODO add your handling code here:
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

    private void tblFuncionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFuncionesMouseClicked
        seleccionarFuncionDeTabla();
    }//GEN-LAST:event_tblFuncionesMouseClicked

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmFunciones().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNGuardar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
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
    private javax.swing.JTable tblFunciones;
    private javax.swing.JTextField txtIdFunciones;
    // End of variables declaration//GEN-END:variables
}
