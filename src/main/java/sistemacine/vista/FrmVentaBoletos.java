
package sistemacine.vista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import sistemacine.modelo.Conexion;

public final class FrmVentaBoletos extends javax.swing.JInternalFrame {    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmVentaBoletos.class.getName());
    
    public FrmVentaBoletos() {
        initComponents();      
        cargarTablaVentas();
        cargarPeliculas();    
        limpiarCampos();
    }
    
 
 public void cargarTablaVentas() {
    DefaultTableModel model = (DefaultTableModel) tblVentaBoletos.getModel();
    model.setRowCount(0); 

    String sql = "SELECT venta_id, nombre_cliente, pelicula, precio_unitario, cantidad, sala, total FROM ventas";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            int ventaId = rs.getInt("venta_id");            
            String nombreCliente = rs.getString("nombre_cliente");
            String pelicula = rs.getString("pelicula");
            double precioUnitario = rs.getDouble("precio_unitario");
            int cantidad = rs.getInt("cantidad");
            String sala = rs.getString("sala");
            double total = rs.getDouble("total");
            model.addRow(new Object[]{ventaId, nombreCliente, pelicula, precioUnitario, cantidad, sala, total});
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar ventas: " + e.getMessage());
    }
}
 
    public void cargarPeliculas() {
    cboPelicula.removeAllItems();
    String sql = "SELECT titulo FROM peliculas";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            String titulo = rs.getString("titulo");
            cboPelicula.addItem(titulo);  
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar películas: " + e.getMessage());
    }
}    
public void cargarSalasPorPelicula(String nombrePelicula) {
    cboSala.removeAllItems();
    String sql = "SELECT DISTINCT s.nombre FROM salas s " +
                 "JOIN funciones f ON s.sala_id = f.sala_id " +
                 "JOIN peliculas p ON f.pelicula_id = p.pelicula_id " +
                 "WHERE p.titulo = ?";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombrePelicula);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String nombreSala = rs.getString("nombre");
                cboSala.addItem(nombreSala);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar salas: " + e.getMessage());
    }
}
private void ventaBoleto() throws SQLException {
    String nombreCliente = txtNombreCliente.getText().trim();
    String pelicula = (String) cboPelicula.getSelectedItem();
    String sala = (String) cboSala.getSelectedItem();
    String cantidadStr = txtCantidad.getText().trim();
    String precioStr = txtPrecioU.getText().trim();
    if (nombreCliente.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.");
        return;
    }
    int cantidad;
    double precioUnitario;
    try {
        cantidad = Integer.parseInt(cantidadStr);
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Ingrese un número válido en el campo CANTIDAD.");
        return;
    }
    try {
        precioUnitario = Double.parseDouble(precioStr);
        if (precioUnitario <= 0) {
            JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0.");
            return;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Ingrese un número válido en el campo PRECIO.");
        return;
    }
    int disponibles = obtenerBoletosDisponibles(sala);
    if (cantidad > disponibles) {
    JOptionPane.showMessageDialog(this, "No hay suficientes boletos disponibles para esta sala. Solo quedan " + disponibles + " boletos.");
    return;
}
    double total = cantidad * precioUnitario;
    txtTotal.setText(String.format("%.2f", total));
    insertarVenta(nombreCliente, pelicula, cantidad, precioUnitario, sala, total);
    cargarTablaVentas();
    limpiarCampos();
}  

public int obtenerBoletosDisponibles(String nombreSala) {
    int capacidad = 0;
    int vendidos = 0;
    String sqlCapacidad = "SELECT capacidad FROM salas WHERE nombre = ?";
    String sqlVendidos = "SELECT SUM(cantidad) AS total_vendidos FROM ventas WHERE sala = ?";

    try (Connection con = Conexion.getConexion()) {
        // Capacidad total
        try (PreparedStatement ps = con.prepareStatement(sqlCapacidad)) {
            ps.setString(1, nombreSala);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    capacidad = rs.getInt("capacidad");
                }
            }
        }
        try (PreparedStatement ps = con.prepareStatement(sqlVendidos)) {
            ps.setString(1, nombreSala);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    vendidos = rs.getInt("total_vendidos");
                }
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al verificar disponibilidad: " + e.getMessage());
    }
    return capacidad - vendidos;
}

 public void insertarVenta(String nombreCliente, String pelicula, 
                          int cantidad, double precioUnitario, String sala, double total) throws SQLException {
    String sql = "INSERT INTO ventas (nombre_cliente, pelicula, cantidad, precio_unitario, sala, total) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection con = Conexion.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombreCliente);
        ps.setString(2, pelicula);
        ps.setInt(3, cantidad);             
        ps.setDouble(4, precioUnitario);    
        ps.setString(5, sala);
        ps.setDouble(6, total);

        int filasInsertadas = ps.executeUpdate();
        if (filasInsertadas > 0) {
            JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");
        }
    }
}
  
public void actualizarTotal() {
    try {
        int cantidad = Integer.parseInt(txtCantidad.getText());
        double precio = Double.parseDouble(txtPrecioU.getText());
        double total = cantidad * precio;
        txtTotal.setText(String.format("%.2f", total));
    } catch (NumberFormatException e) {
        txtTotal.setText("0.00");
    }
}

public void eliminarVentaSeleccionada() {
    int filaSeleccionada = tblVentaBoletos.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(this, "Por favor seleccione una venta para eliminar.");
        return;
    }
    DefaultTableModel model = (DefaultTableModel) tblVentaBoletos.getModel();
    int ventaId = (int) model.getValueAt(filaSeleccionada, 0); // Asumiendo que la columna 0 es venta_id

    int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea eliminar la venta seleccionada?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
        String sql = "DELETE FROM ventas WHERE venta_id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ventaId);
            int eliminado = ps.executeUpdate();

            if (eliminado > 0) {
                JOptionPane.showMessageDialog(this, "Venta eliminada correctamente.");
                cargarTablaVentas(); // refrescar tabla
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar la venta: " + e.getMessage());
        }
    }
}
public void limpiarCampos() {
    txtNombreCliente.setText("");
    txtCantidad.setText("");
    txtPrecioU.setText("");
    txtTotal.setText("");

    if (cboPelicula.getItemCount() > 0) cboPelicula.setSelectedIndex(0);
    if (cboSala.getItemCount() > 0) cboSala.setSelectedIndex(0);
}
public void actualizarBarraCapacidad(String nombreSala) {
    int capacidad = 0;
    int boletosVendidos = 0;
    String sqlCapacidad = "SELECT capacidad FROM salas WHERE nombre = ?";
    String sqlVendidos = "SELECT SUM(cantidad) AS total_vendidos FROM ventas WHERE sala = ?";

    try (Connection con = Conexion.getConexion()) {
        try (PreparedStatement ps1 = con.prepareStatement(sqlCapacidad)) {
            ps1.setString(1, nombreSala);
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) {
                    capacidad = rs1.getInt("capacidad");
                }
            }
        }
        try (PreparedStatement ps2 = con.prepareStatement(sqlVendidos)) {
            ps2.setString(1, nombreSala);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    boletosVendidos = rs2.getInt("total_vendidos");
                }
            }
        }
        int disponibles = capacidad - boletosVendidos;
        if (disponibles < 0) disponibles = 0;

        barraCapacidadSala.setMaximum(capacidad);
        barraCapacidadSala.setValue(boletosVendidos);
        barraCapacidadSala.setString("Vendidos: " + boletosVendidos + " / " + capacidad +
                " | Disponibles: " + disponibles);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al calcular la capacidad: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblNombreCliente = new javax.swing.JLabel();
        lblSala = new javax.swing.JLabel();
        lblCapacidadSala = new javax.swing.JLabel();
        txtNombreCliente = new javax.swing.JTextField();
        cboSala = new javax.swing.JComboBox<>();
        barraCapacidadSala = new javax.swing.JProgressBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVentaBoletos = new javax.swing.JTable();
        btnVenderBoleto = new javax.swing.JButton();
        lblCantidad = new javax.swing.JLabel();
        txtCantidad = new javax.swing.JTextField();
        lblTotal = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        lblPrecioUnitario = new javax.swing.JLabel();
        txtPrecioU = new javax.swing.JTextField();
        lblPelicula = new javax.swing.JLabel();
        cboPelicula = new javax.swing.JComboBox<>();
        btnReenbolsar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(1000, 428));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("VENTA DE BOLETOS");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 10, 158, -1));

        lblNombreCliente.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblNombreCliente.setText("NOMBRE DEL CLIENTE");
        getContentPane().add(lblNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, -1, -1));

        lblSala.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblSala.setText("SALA");
        getContentPane().add(lblSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 500, 143, -1));

        lblCapacidadSala.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCapacidadSala.setText("CAPACIDAD DE LA SALA");
        getContentPane().add(lblCapacidadSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 80, -1, -1));

        txtNombreCliente.setText("jTextField1");
        txtNombreCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreClienteActionPerformed(evt);
            }
        });
        getContentPane().add(txtNombreCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 140, 217, -1));

        cboSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSalaActionPerformed(evt);
            }
        });
        getContentPane().add(cboSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 490, 217, -1));

        barraCapacidadSala.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        barraCapacidadSala.setStringPainted(true);
        getContentPane().add(barraCapacidadSala, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 60, 890, 50));

        tblVentaBoletos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID VENTA", "CLIENTES", "PELICULA", "PRECIO U", "CANTIDAD", "SALA", "TOTAL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblVentaBoletos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVentaBoletosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblVentaBoletos);
        if (tblVentaBoletos.getColumnModel().getColumnCount() > 0) {
            tblVentaBoletos.getColumnModel().getColumn(0).setResizable(false);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 870, 470));

        btnVenderBoleto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnVenderBoleto.setText("VENDER BOLETO");
        btnVenderBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVenderBoletoActionPerformed(evt);
            }
        });
        getContentPane().add(btnVenderBoleto, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 670, 169, -1));

        lblCantidad.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCantidad.setText("CANTIDAD");
        getContentPane().add(lblCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 143, -1));

        txtCantidad.setText("jTextField1");
        txtCantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantidadActionPerformed(evt);
            }
        });
        txtCantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCantidadKeyReleased(evt);
            }
        });
        getContentPane().add(txtCantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, 217, -1));

        lblTotal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblTotal.setText("TOTAL");
        getContentPane().add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 580, 143, -1));

        txtTotal.setText("jTextField2");
        txtTotal.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                txtTotalAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        getContentPane().add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 580, 217, 30));

        lblPrecioUnitario.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPrecioUnitario.setText("PRECIO UNITARIO");
        getContentPane().add(lblPrecioUnitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 143, -1));

        txtPrecioU.setText("jTextField1");
        txtPrecioU.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                txtPrecioUAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        txtPrecioU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioUActionPerformed(evt);
            }
        });
        txtPrecioU.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrecioUKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPrecioUKeyReleased(evt);
            }
        });
        getContentPane().add(txtPrecioU, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, 217, -1));

        lblPelicula.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblPelicula.setText("PELICULA");
        getContentPane().add(lblPelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 143, -1));

        cboPelicula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboPelicula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeliculaActionPerformed(evt);
            }
        });
        getContentPane().add(cboPelicula, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 217, -1));

        btnReenbolsar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnReenbolsar.setText("REEMBOLSO");
        btnReenbolsar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReenbolsarActionPerformed(evt);
            }
        });
        getContentPane().add(btnReenbolsar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 670, 143, -1));

        btnCerrar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCerrar.setText("CERRAR");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 670, 120, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreClienteActionPerformed

    private void cboSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSalaActionPerformed
             String salaSeleccionada = (String) cboSala.getSelectedItem();
        if (salaSeleccionada != null) {
        actualizarBarraCapacidad(salaSeleccionada);
    }
    }//GEN-LAST:event_cboSalaActionPerformed

    private void btnVenderBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVenderBoletoActionPerformed
        try {
        ventaBoleto();
        } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al registrar la venta: " + ex.getMessage());
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }
        String sala = (String) cboSala.getSelectedItem();
    actualizarBarraCapacidad(sala);
 
        

    }//GEN-LAST:event_btnVenderBoletoActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
      
    }//GEN-LAST:event_txtTotalActionPerformed

    private void cboPeliculaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeliculaActionPerformed
            String peliculaSeleccionada = (String) cboPelicula.getSelectedItem();
            if (peliculaSeleccionada != null) {
             cargarSalasPorPelicula(peliculaSeleccionada);
     
}
    }//GEN-LAST:event_cboPeliculaActionPerformed

    private void btnReenbolsarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReenbolsarActionPerformed
            eliminarVentaSeleccionada();
            String sala = (String) cboSala.getSelectedItem();
            actualizarBarraCapacidad(sala);


    }//GEN-LAST:event_btnReenbolsarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtPrecioUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioUActionPerformed
      
    }//GEN-LAST:event_txtPrecioUActionPerformed

    private void txtCantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantidadActionPerformed
    
    }//GEN-LAST:event_txtCantidadActionPerformed

    private void tblVentaBoletosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVentaBoletosMouseClicked
         int fila = tblVentaBoletos.getSelectedRow();
     if (fila >= 0) {
        String sala = tblVentaBoletos.getValueAt(fila, 5).toString(); // índice 5 = sala
        cboSala.setSelectedItem(sala); // opcional: sincroniza el combo con lo seleccionado
        actualizarBarraCapacidad(sala); // muestra barra de capacidad de esa sala
    }
    }//GEN-LAST:event_tblVentaBoletosMouseClicked

    private void txtCantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyPressed
        // TODO add your handling code here: public void keyReleased(java.awt.event.KeyEvent evt) {
        
    

    }//GEN-LAST:event_txtCantidadKeyPressed

    private void txtTotalAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtTotalAncestorAdded

    }//GEN-LAST:event_txtTotalAncestorAdded

    private void txtPrecioUKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUKeyPressed
         
    }//GEN-LAST:event_txtPrecioUKeyPressed

    private void txtPrecioUAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtPrecioUAncestorAdded
     
    }//GEN-LAST:event_txtPrecioUAncestorAdded

    private void txtCantidadKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadKeyReleased
        actualizarTotal();
    }//GEN-LAST:event_txtCantidadKeyReleased

    private void txtPrecioUKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioUKeyReleased
        actualizarTotal();
    }//GEN-LAST:event_txtPrecioUKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraCapacidadSala;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnReenbolsar;
    private javax.swing.JButton btnVenderBoleto;
    private javax.swing.JComboBox<String> cboPelicula;
    private javax.swing.JComboBox<String> cboSala;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCantidad;
    private javax.swing.JLabel lblCapacidadSala;
    private javax.swing.JLabel lblNombreCliente;
    private javax.swing.JLabel lblPelicula;
    private javax.swing.JLabel lblPrecioUnitario;
    private javax.swing.JLabel lblSala;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblVentaBoletos;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtPrecioU;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
