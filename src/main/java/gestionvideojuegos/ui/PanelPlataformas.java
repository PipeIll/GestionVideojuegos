package gestionvideojuegos.ui;

import gestionvideojuegos.dao.PlataformaDAO;
import gestionvideojuegos.model.Genero;
import gestionvideojuegos.model.Plataforma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelPlataformas extends JPanel {

    private PlataformaDAO plataformaDAO = new PlataformaDAO();
    private DefaultTableModel modelo;
    private int idSeleccionado = -1;


    public PanelPlataformas() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // titulo
        JLabel titulo = new JLabel("Plataformas");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));


        // tabla
        String[] columnas = {"ID", "Nombre", "Fabricante", "Año Lanzamiento"};
        modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);        tabla.setBackground(new Color(25, 25, 38));
        tabla.setForeground(new Color(220, 220, 230));
        tabla.setGridColor(new Color(40, 40, 55));
        tabla.getTableHeader().setBackground(new Color(13, 13, 20));
        tabla.getTableHeader().setForeground(new Color(150, 150, 170));
        tabla.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBackground(new Color(18, 18, 28));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(25, 25, 38));


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
        JTextField campoFabricante   = ComponenteFactory.crearCampo();
        JTextField campoAñoLanzamiento   = ComponenteFactory.crearCampo();
        JButton btnGuardar     = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar     = ComponenteFactory.crearBotonLimpiar();

        JPanel formulario = ComponenteFactory.crearPanelFormulario();
        ComponenteFactory.agregarCampo(formulario, "Nombre:", campoNombre);
        ComponenteFactory.agregarCampo(formulario, "Fabricante:", campoFabricante);
        ComponenteFactory.agregarCampo(formulario, "Año Lanzamiento:", campoAñoLanzamiento);
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
                campoFabricante.setText(modelo.getValueAt(fila, 2).toString());
                campoAñoLanzamiento.setText(modelo.getValueAt(fila, 3).toString());

            }
        });

        // acciones
        btnGuardar.addActionListener(e -> {
            String nombre = campoNombre.getText().trim();
            String fabricante = campoFabricante.getText().trim();
            String anioTexto = campoAñoLanzamiento.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
                return;
            }
            if (anioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El año es obligatorio");
                return;
            }

            int anio = Integer.parseInt(anioTexto);

            if (idSeleccionado == -1) {
                plataformaDAO.insertar(new Plataforma(0, nombre, fabricante, anio));
            } else {
                plataformaDAO.actualizar(new Plataforma(idSeleccionado, nombre, fabricante, anio));
            }
            cargarDatos();
            limpiarFormulario(campoNombre, campoFabricante, campoAñoLanzamiento);
        });

        btnLimpiar.addActionListener(e -> limpiarFormulario(campoNombre, campoFabricante, campoAñoLanzamiento));

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una plataforma primero");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Eliminar esta plataforma?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                plataformaDAO.eliminar(idSeleccionado);
                cargarDatos();
                limpiarFormulario(campoNombre, campoFabricante, campoAñoLanzamiento);
            }
        });

        btnBuscarId.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el ID:");
            if (input != null && !input.isEmpty()) {
                Plataforma p = plataformaDAO.buscarPorId(Integer.parseInt(input));
                modelo.setRowCount(0);
                if (p != null)
                    modelo.addRow(new Object[]{p.getIdPlataforma(), p.getNombre(), p.getFabricante(), p.getAñoLanzamiento()});
            }
        });

        btnBuscarNombre.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el nombre:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                plataformaDAO.buscarPorNombre(input)
                        .forEach(p -> modelo.addRow(new Object[]{p.getIdPlataforma(), p.getNombre(), p.getFabricante(), p.getAñoLanzamiento()}));
            }
        });

        btnVerTodos.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportar());

        cargarDatos();
    }
    private void cargarDatos() {
        modelo.setRowCount(0);
        plataformaDAO.buscarTodos()
                .forEach(p -> modelo.addRow(new Object[]{p.getIdPlataforma(), p.getNombre(), p.getFabricante(), p.getAñoLanzamiento()}));
    }

    private void limpiarFormulario(JTextField campoNombre, JTextField campoFabricante, JTextField campoAño) {
        campoNombre.setText("");
        campoFabricante.setText("");
        campoAño.setText("");
        idSeleccionado = -1;
    }

    private void exportar() {
        String ruta = System.getProperty("user.home") + "/Downloads/reporte_plataformas.txt";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(
                new java.io.FileWriter(ruta))) {
            bw.write("ID | Nombre | Fabricante | Año Lanzamiento");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                bw.write(modelo.getValueAt(i, 0) + " | " +
                        modelo.getValueAt(i, 1) + " | " +
                        modelo.getValueAt(i, 2) + " | " +
                        modelo.getValueAt(i, 3));
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado en Descargas/reporte_plataformas.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }

}