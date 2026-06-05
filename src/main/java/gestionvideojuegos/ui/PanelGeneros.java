package gestionvideojuegos.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import gestionvideojuegos.dao.GeneroDAO;
import gestionvideojuegos.model.Genero;

public class PanelGeneros extends JPanel {

    private GeneroDAO generoDAO = new GeneroDAO();
    private DefaultTableModel modelo;
    private int idSeleccionado = -1;

    public PanelGeneros() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        JLabel titulo = new JLabel("Generos");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        // tabla
        String[] columnas = {"ID", "Nombre", "Descripcion"};
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

        cargarDatos();

        // botones izquierda
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));        panelBotones.setBackground(new Color(13, 13, 20));
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
        JTextField campoNombre = ComponenteFactory.crearCampo();
        JTextField campoDesc   = ComponenteFactory.crearCampo();
        JButton btnGuardar     = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar     = ComponenteFactory.crearBotonLimpiar();

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(new Color(25, 25, 38));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        formulario.add(ComponenteFactory.crearLabel("Nombre:"));
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(campoNombre);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(ComponenteFactory.crearLabel("Descripcion:"));
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(campoDesc);
        formulario.add(Box.createVerticalGlue());
        formulario.add(btnGuardar);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnLimpiar);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, formulario);
        split.setDividerLocation(650);
        split.setResizeWeight(0.7);

        add(titulo, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        // listener tabla
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                campoNombre.setText(modelo.getValueAt(fila, 1).toString());
                campoDesc.setText(modelo.getValueAt(fila, 2).toString());
            }
        });

        // acciones
        btnGuardar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String desc   = campoDesc.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            if (idSeleccionado == -1) {
                generoDAO.insertar(new Genero(0, nombre, desc));
            } else {
                generoDAO.actualizar(new Genero(idSeleccionado, nombre, desc));
            }
            cargarDatos();
            limpiarFormulario(campoNombre, campoDesc);
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario(campoNombre, campoDesc));

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un genero primero");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Eliminar este genero?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                generoDAO.eliminar(idSeleccionado);
                cargarDatos();
                limpiarFormulario(campoNombre, campoDesc);
            }
        });

        btnBuscarId.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el ID:");
            if (input != null && !input.isEmpty()) {
                Genero g = generoDAO.buscarPorId(Integer.parseInt(input));
                modelo.setRowCount(0);
                if (g != null)
                    modelo.addRow(new Object[]{g.getIdGenero(), g.getNombre(), g.getDescripcion()});
            }
        });

        btnBuscarNombre.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el nombre:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                generoDAO.buscarPorNombre(input)
                        .forEach(g -> modelo.addRow(new Object[]{g.getIdGenero(), g.getNombre(), g.getDescripcion()}));
            }
        });

        btnVerTodos.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportar());
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        generoDAO.buscarTodos()
                .forEach(g -> modelo.addRow(new Object[]{g.getIdGenero(), g.getNombre(), g.getDescripcion()}));
    }

    private void limpiarFormulario(JTextField campoNombre, JTextField campoDesc) {
        campoNombre.setText("");
        campoDesc.setText("");
        idSeleccionado = -1;
    }

    private void exportar() {
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                new java.io.FileWriter("reporte_generos.txt"))) {
            bw.write("ID | Nombre | Descripcion");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                bw.write(modelo.getValueAt(i, 0) + " | " +
                        modelo.getValueAt(i, 1) + " | " +
                        modelo.getValueAt(i, 2));
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado como reporte_generos.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}