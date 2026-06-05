package gestionvideojuegos.ui;

import gestionvideojuegos.dao.PremioDAO;
import gestionvideojuegos.dao.VideojuegoDAO;
import gestionvideojuegos.model.Premio;
import gestionvideojuegos.model.Videojuego;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelPremios extends JPanel {

    private PremioDAO premioDAO = new PremioDAO();
    private VideojuegoDAO videojuegoDAO = new VideojuegoDAO();

    private DefaultTableModel modelo;
    private int idSeleccionado = -1;

    // Campos del formulario
    private JComboBox<Videojuego> comboVideojuego;
    private JTextField campoNombre;
    private JTextField campoOrganizacion;
    private JTextField campoAnio;
    private JTextField campoCategoria;

    public PanelPremios() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // ── Título ──────────────────────────────────────────────────
        JLabel titulo = new JLabel("Premios");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        // ── Tabla ───────────────────────────────────────────────────
        String[] columnas = {"ID", "Videojuego", "Nombre Premio", "Organización", "Año", "Categoría"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setBackground(new Color(25, 25, 38));
        tabla.setForeground(new Color(220, 220, 230));
        tabla.setGridColor(new Color(40, 40, 55));
        tabla.getTableHeader().setBackground(new Color(13, 13, 20));
        tabla.getTableHeader().setForeground(new Color(150, 150, 170));
        tabla.setRowHeight(30);
        tabla.setSelectionBackground(new Color(60, 80, 180));
        tabla.getColumnModel().getColumn(0).setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(25, 25, 38));

        // ── Botones inferiores ──────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(13, 13, 20));
        JButton btnBuscarId   = ComponenteFactory.crearBoton("Buscar por ID",    new Color(50, 50, 70));
        JButton btnBuscarVJ   = ComponenteFactory.crearBoton("Buscar por Juego", new Color(50, 50, 70));
        JButton btnVerTodos   = ComponenteFactory.crearBoton("Ver todos",         new Color(50, 50, 70));
        JButton btnEliminar   = ComponenteFactory.crearBoton("Eliminar",          new Color(150, 40, 40));
        JButton btnExportar   = ComponenteFactory.crearBoton("Exportar",          new Color(40, 100, 70));
        panelBotones.add(btnBuscarId);
        panelBotones.add(btnBuscarVJ);
        panelBotones.add(btnVerTodos);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(new Color(18, 18, 28));
        panelIzquierdo.add(scroll,        BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones,  BorderLayout.SOUTH);

        // ── Formulario derecho ──────────────────────────────────────
        JPanel formulario = construirFormulario();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, formulario);
        split.setDividerLocation(620);
        split.setResizeWeight(0.65);

        add(titulo, BorderLayout.NORTH);
        add(split,  BorderLayout.CENTER);

        // ── Listener: clic en fila → rellena formulario ─────────────
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila == -1) return;
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            seleccionarComboVideojuego(modelo.getValueAt(fila, 1).toString());
            campoNombre.setText(modelo.getValueAt(fila, 2).toString());
            campoOrganizacion.setText(modelo.getValueAt(fila, 3).toString());
            campoAnio.setText(modelo.getValueAt(fila, 4).toString());
            campoCategoria.setText(modelo.getValueAt(fila, 5).toString());
        });

        // ── Acciones ────────────────────────────────────────────────
        JButton btnGuardar = (JButton) formulario.getClientProperty("btnGuardar");
        JButton btnLimpiar = (JButton) formulario.getClientProperty("btnLimpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un premio primero");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este premio?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                premioDAO.eliminar(idSeleccionado);
                cargarDatos();
                limpiar();
            }
        });

        btnBuscarId.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el ID:");
            if (input != null && !input.isEmpty()) {
                try {
                    Premio p = premioDAO.buscarPorId(Integer.parseInt(input));
                    modelo.setRowCount(0);
                    if (p != null) agregarFila(p);
                    else JOptionPane.showMessageDialog(this, "No encontrado");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número");
                }
            }
        });

        btnBuscarVJ.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Nombre del videojuego:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                premioDAO.buscarPorNombreDeVideojuego(input)
                        .forEach(p -> {
                            Videojuego vj = videojuegoDAO.buscarPorId(p.getVideojuego().getIdVideojuego());
                            String nombreVJ = vj != null ? vj.getNombre() : "ID " + p.getVideojuego().getIdVideojuego();
                            agregarFila(p, nombreVJ);
                        });
            }
        });

        btnVerTodos.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportar());

        cargarDatos();
    }

    // ── Construye el panel de formulario ────────────────────────────
    private JPanel construirFormulario() {
        List<Videojuego> videojuegos = videojuegoDAO.buscarTodos();
        comboVideojuego = new JComboBox<>(videojuegos.toArray(new Videojuego[0]));
        comboVideojuego.setRenderer((list, value, i, sel, foc) -> {
            JLabel l = new JLabel(value != null ? value.getNombre() : "");
            l.setOpaque(true);
            l.setBackground(sel ? new Color(60, 80, 180) : new Color(35, 35, 50));
            l.setForeground(new Color(220, 220, 230));
            l.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return l;
        });
        comboVideojuego.setBackground(new Color(35, 35, 50));
        comboVideojuego.setForeground(new Color(220, 220, 230));
        comboVideojuego.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        campoNombre       = ComponenteFactory.crearCampo();
        campoOrganizacion = ComponenteFactory.crearCampo();
        campoAnio         = ComponenteFactory.crearCampo();
        campoCategoria    = ComponenteFactory.crearCampo();

        JButton btnGuardar = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar = ComponenteFactory.crearBotonLimpiar();

        JPanel panel = ComponenteFactory.crearPanelFormulario();

        panel.add(ComponenteFactory.crearLabel("Videojuego:"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboVideojuego);
        panel.add(Box.createVerticalStrut(15));

        ComponenteFactory.agregarCampo(panel, "Nombre del Premio:", campoNombre);
        ComponenteFactory.agregarCampo(panel, "Organización:",      campoOrganizacion);
        ComponenteFactory.agregarCampo(panel, "Año (ej. 2023):",    campoAnio);
        ComponenteFactory.agregarCampo(panel, "Categoría:",         campoCategoria);
        panel.add(Box.createVerticalGlue());
        panel.add(btnGuardar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnLimpiar);

        panel.putClientProperty("btnGuardar", btnGuardar);
        panel.putClientProperty("btnLimpiar", btnLimpiar);

        return panel;
    }

    // ── Lógica de datos ──────────────────────────────────────────────
    private void guardar() {
        Videojuego vj = (Videojuego) comboVideojuego.getSelectedItem();
        if (vj == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un videojuego"); return;
        }
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del premio es obligatorio"); return;
        }
        String organizacion = campoOrganizacion.getText().trim();
        short anio;
        try {
            anio = Short.parseShort(campoAnio.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un número entero"); return;
        }
        String categoria = campoCategoria.getText().trim();

        Premio premio = new Premio(
                idSeleccionado == -1 ? 0 : idSeleccionado,
                vj, nombre, organizacion, anio, categoria
        );

        try {
            if (idSeleccionado == -1) premioDAO.insertar(premio);
            else                      premioDAO.actualizar(premio);
            cargarDatos();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        for (Premio p : premioDAO.buscarTodos()) {
            Videojuego vj = videojuegoDAO.buscarPorId(p.getVideojuego().getIdVideojuego());
            String nombreVJ = vj != null ? vj.getNombre() : "ID " + p.getVideojuego().getIdVideojuego();
            agregarFila(p, nombreVJ);
        }
    }

    private void agregarFila(Premio p) {
        Videojuego vj = videojuegoDAO.buscarPorId(p.getVideojuego().getIdVideojuego());
        String nombreVJ = vj != null ? vj.getNombre() : "ID " + p.getVideojuego().getIdVideojuego();
        agregarFila(p, nombreVJ);
    }

    private void agregarFila(Premio p, String nombreVJ) {
        modelo.addRow(new Object[]{
                p.getIdPremio(),
                nombreVJ,
                p.getNombrePremio(),
                p.getOrganizacion(),
                p.getAño(),
                p.getCategoria()
        });
    }

    private void limpiar() {
        if (comboVideojuego.getItemCount() > 0) comboVideojuego.setSelectedIndex(0);
        campoNombre.setText("");
        campoOrganizacion.setText("");
        campoAnio.setText("");
        campoCategoria.setText("");
        idSeleccionado = -1;
    }

    private void seleccionarComboVideojuego(String nombre) {
        for (int i = 0; i < comboVideojuego.getItemCount(); i++) {
            if (comboVideojuego.getItemAt(i).getNombre().equals(nombre)) {
                comboVideojuego.setSelectedIndex(i);
                return;
            }
        }
    }

    private void exportar() {
        String ruta = System.getProperty("user.home") + "/Downloads/reporte_premios.txt";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(ruta))) {
            bw.write("ID | Videojuego | Nombre Premio | Organización | Año | Categoría");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    if (j > 0) bw.write(" | ");
                    bw.write(String.valueOf(modelo.getValueAt(i, j)));
                }
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado en Descargas/reporte_premios.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}