package gestionvideojuegos.ui;

import gestionvideojuegos.dao.DesarrolladoraDAO;
import gestionvideojuegos.model.Desarrolladora;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelDesarrolladoras extends JPanel {

    private DesarrolladoraDAO desarrolladoraDAO = new DesarrolladoraDAO();
    private DefaultTableModel modelo;
    private int idSeleccionado = -1;

    public PanelDesarrolladoras() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // titulo
        JLabel titulo = new JLabel("Desarrolladoras");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        // tabla
        String[] columnas = {"ID", "Nombre", "País Origen", "Año Fundación"};
        modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(25, 25, 38));
        tabla.setForeground(new Color(220, 220, 230));
        tabla.setGridColor(new Color(40, 40, 55));
        tabla.getTableHeader().setBackground(new Color(13, 13, 20));
        tabla.getTableHeader().setForeground(new Color(150, 150, 170));
        tabla.setRowHeight(30);
        tabla.setSelectionBackground(new Color(60, 80, 180));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(25, 25, 38));

        // botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(13, 13, 20));
        JButton btnBuscarId     = ComponenteFactory.crearBoton("Buscar por ID", new Color(50, 50, 70));
        JButton btnBuscarNombre = ComponenteFactory.crearBoton("Buscar por Nombre", new Color(50, 50, 70));
        JButton btnVerTodos     = ComponenteFactory.crearBoton("Ver todos", new Color(50, 50, 70));
        JButton btnEliminar     = ComponenteFactory.crearBoton("Eliminar", new Color(150, 40, 40));
        JButton btnExportar     = ComponenteFactory.crearBoton("Exportar", new Color(40, 100, 70));
        panelBotones.add(btnBuscarId);
        panelBotones.add(btnBuscarNombre);
        panelBotones.add(btnVerTodos);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(new Color(18, 18, 28));
        panelIzquierdo.add(scroll, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        // formulario derecha
        JTextField campoNombre          = ComponenteFactory.crearCampo();
        JTextField campoPaisOrigen      = ComponenteFactory.crearCampo();
        JTextField campoAñoFundacion    = ComponenteFactory.crearCampo();
        JButton btnGuardar              = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar              = ComponenteFactory.crearBotonLimpiar();

        JPanel formulario = ComponenteFactory.crearPanelFormulario();
        ComponenteFactory.agregarCampo(formulario, "Nombre:", campoNombre);
        ComponenteFactory.agregarCampo(formulario, "País Origen:", campoPaisOrigen);
        ComponenteFactory.agregarCampo(formulario, "Año Fundación:", campoAñoFundacion);
        formulario.add(Box.createVerticalGlue());
        formulario.add(btnGuardar);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnLimpiar);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, formulario);
        split.setDividerLocation(650);
        split.setResizeWeight(0.7);

        add(titulo, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        // listener tabla -> poblar formulario al seleccionar
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                campoNombre.setText(modelo.getValueAt(fila, 1).toString());
                campoPaisOrigen.setText(modelo.getValueAt(fila, 2).toString());
                campoAñoFundacion.setText(modelo.getValueAt(fila, 3).toString());
            }
        });

        // guardar (insertar o actualizar)
        btnGuardar.addActionListener(e -> {
            String nombre    = campoNombre.getText().trim();
            String pais      = campoPaisOrigen.getText().trim();
            String añoTexto  = campoAñoFundacion.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            if (añoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El año de fundación es obligatorio");
                return;
            }

            int año;
            try {
                año = Integer.parseInt(añoTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número (ej: 1989)");
                campoAñoFundacion.requestFocus();
                return;
            }

            try {
                if (idSeleccionado == -1) {
                    desarrolladoraDAO.insertar(new Desarrolladora(0, nombre, pais, año));
                } else {
                    desarrolladoraDAO.actualizar(new Desarrolladora(idSeleccionado, nombre, pais, año));
                }
                cargarDatos();
                limpiarFormulario(campoNombre, campoPaisOrigen, campoAñoFundacion);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
            }
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario(campoNombre, campoPaisOrigen, campoAñoFundacion));

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una desarrolladora primero");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar esta desarrolladora?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    desarrolladoraDAO.eliminar(idSeleccionado);
                    cargarDatos();
                    limpiarFormulario(campoNombre, campoPaisOrigen, campoAñoFundacion);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            }
        });

        btnBuscarId.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el ID:");
            if (input != null && !input.isEmpty()) {
                try {
                    Desarrolladora d = desarrolladoraDAO.buscarPorId(Integer.parseInt(input));
                    modelo.setRowCount(0);
                    if (d != null)
                        modelo.addRow(new Object[]{d.getIdDesarrolladora(), d.getNombre(), d.getPaisOrigen(), d.getAñoFundacion()});
                    else
                        JOptionPane.showMessageDialog(this, "No se encontró ninguna desarrolladora con ese ID");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número");
                }
            }
        });

        btnBuscarNombre.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el nombre:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                desarrolladoraDAO.buscarPorNombre(input)
                        .forEach(d -> modelo.addRow(new Object[]{d.getIdDesarrolladora(), d.getNombre(), d.getPaisOrigen(), d.getAñoFundacion()}));
            }
        });

        btnVerTodos.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportar());

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        desarrolladoraDAO.buscarTodos()
                .forEach(d -> modelo.addRow(new Object[]{d.getIdDesarrolladora(), d.getNombre(), d.getPaisOrigen(), d.getAñoFundacion()}));
    }

    private void limpiarFormulario(JTextField campoNombre, JTextField campoPais, JTextField campoAño) {
        campoNombre.setText("");
        campoPais.setText("");
        campoAño.setText("");
        idSeleccionado = -1;
    }

    private void exportar() {
        String ruta = System.getProperty("user.home") + "/Downloads/reporte_desarrolladoras.txt";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                new java.io.FileWriter(ruta))) {
            bw.write("ID | Nombre | País Origen | Año Fundación");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                bw.write(modelo.getValueAt(i, 0) + " | " +
                        modelo.getValueAt(i, 1) + " | " +
                        modelo.getValueAt(i, 2) + " | " +
                        modelo.getValueAt(i, 3));
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado en Descargas/reporte_desarrolladoras.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}
