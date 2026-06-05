package gestionvideojuegos.ui;

import gestionvideojuegos.dao.PlataformaDAO;
import gestionvideojuegos.dao.VideojuegoDAO;
import gestionvideojuegos.dao.VideojuegoPlataformaDAO;
import gestionvideojuegos.model.Plataforma;
import gestionvideojuegos.model.Videojuego;
import gestionvideojuegos.model.VideojuegoPlataforma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelVJPlataformas extends JPanel {

    private VideojuegoPlataformaDAO vpDAO       = new VideojuegoPlataformaDAO();
    private VideojuegoDAO           videojuegoDAO = new VideojuegoDAO();
    private PlataformaDAO           plataformaDAO = new PlataformaDAO();

    private DefaultTableModel modelo;

    // IDs de la fila seleccionada (clave primaria compuesta)
    private int idVJSeleccionado  = -1;
    private int idPlatSeleccionado = -1;

    // Campos del formulario
    private JComboBox<Videojuego>  comboVideojuego;
    private JComboBox<Plataforma>  comboPlataforma;
    private JTextField             campoFecha;

    public PanelVJPlataformas() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // ── Título ──────────────────────────────────────────────────
        JLabel titulo = new JLabel("Videojuego – Plataformas");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        // ── Tabla ───────────────────────────────────────────────────
        String[] columnas = {"Videojuego", "Plataforma", "Fecha Lanzamiento"};
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

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(25, 25, 38));

        // ── Botones inferiores ──────────────────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(13, 13, 20));
        JButton btnBuscarVJ   = ComponenteFactory.crearBoton("Buscar por Juego",      new Color(50, 50, 70));
        JButton btnBuscarPlat = ComponenteFactory.crearBoton("Buscar por Plataforma",  new Color(50, 50, 70));
        JButton btnVerTodos   = ComponenteFactory.crearBoton("Ver todos",               new Color(50, 50, 70));
        JButton btnEliminar   = ComponenteFactory.crearBoton("Eliminar",                new Color(150, 40, 40));
        JButton btnExportar   = ComponenteFactory.crearBoton("Exportar",                new Color(40, 100, 70));
        panelBotones.add(btnBuscarVJ);
        panelBotones.add(btnBuscarPlat);
        panelBotones.add(btnVerTodos);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnExportar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(new Color(18, 18, 28));
        panelIzquierdo.add(scroll,       BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

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

            String nombreVJ   = modelo.getValueAt(fila, 0).toString();
            String nombrePlat = modelo.getValueAt(fila, 1).toString();

            seleccionarComboVideojuego(nombreVJ);
            seleccionarComboPlataforma(nombrePlat);
            campoFecha.setText(modelo.getValueAt(fila, 2).toString());

            // Guardar IDs para la eliminación (clave compuesta)
            Videojuego vj = (Videojuego) comboVideojuego.getSelectedItem();
            Plataforma pl = (Plataforma)  comboPlataforma.getSelectedItem();
            idVJSeleccionado   = vj  != null ? vj.getIdVideojuego()  : -1;
            idPlatSeleccionado = pl  != null ? pl.getIdPlataforma()   : -1;
        });

        // ── Acciones ────────────────────────────────────────────────
        JButton btnGuardar = (JButton) formulario.getClientProperty("btnGuardar");
        JButton btnLimpiar = (JButton) formulario.getClientProperty("btnLimpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnEliminar.addActionListener(e -> {
            if (idVJSeleccionado == -1 || idPlatSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una relación primero");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar esta relación Videojuego-Plataforma?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                vpDAO.eliminar(idVJSeleccionado, idPlatSeleccionado);
                cargarDatos();
                limpiar();
            }
        });

        btnBuscarVJ.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Nombre del videojuego:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                vpDAO.buscarTodos().forEach(vp -> {
                    Videojuego vj = videojuegoDAO.buscarPorId(vp.getVideojuego().getIdVideojuego());
                    Plataforma pl = plataformaDAO.buscarPorId(vp.getPlataforma().getIdPlataforma());
                    if (vj != null && vj.getNombre().toLowerCase().contains(input.toLowerCase()))
                        agregarFila(vp, vj.getNombre(), pl != null ? pl.getNombre() : "?");
                });
            }
        });

        btnBuscarPlat.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Nombre de la plataforma:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                vpDAO.buscarTodos().forEach(vp -> {
                    Videojuego vj = videojuegoDAO.buscarPorId(vp.getVideojuego().getIdVideojuego());
                    Plataforma pl = plataformaDAO.buscarPorId(vp.getPlataforma().getIdPlataforma());
                    if (pl != null && pl.getNombre().toLowerCase().contains(input.toLowerCase()))
                        agregarFila(vp, vj != null ? vj.getNombre() : "?", pl.getNombre());
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
        List<Plataforma> plataformas = plataformaDAO.buscarTodos();

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

        comboPlataforma = new JComboBox<>(plataformas.toArray(new Plataforma[0]));
        comboPlataforma.setRenderer((list, value, i, sel, foc) -> {
            JLabel l = new JLabel(value != null ? value.getNombre() : "");
            l.setOpaque(true);
            l.setBackground(sel ? new Color(60, 80, 180) : new Color(35, 35, 50));
            l.setForeground(new Color(220, 220, 230));
            l.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return l;
        });
        comboPlataforma.setBackground(new Color(35, 35, 50));
        comboPlataforma.setForeground(new Color(220, 220, 230));
        comboPlataforma.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        campoFecha = ComponenteFactory.crearCampo();

        JButton btnGuardar = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar = ComponenteFactory.crearBotonLimpiar();

        JPanel panel = ComponenteFactory.crearPanelFormulario();

        panel.add(ComponenteFactory.crearLabel("Videojuego:"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboVideojuego);
        panel.add(Box.createVerticalStrut(15));

        panel.add(ComponenteFactory.crearLabel("Plataforma:"));
        panel.add(Box.createVerticalStrut(5));
        panel.add(comboPlataforma);
        panel.add(Box.createVerticalStrut(15));

        ComponenteFactory.agregarCampo(panel, "Fecha Lanzamiento (YYYY-MM-DD):", campoFecha);

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
        Plataforma pl = (Plataforma)  comboPlataforma.getSelectedItem();

        if (vj == null || pl == null) {
            JOptionPane.showMessageDialog(this, "Selecciona videojuego y plataforma"); return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(campoFecha.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato YYYY-MM-DD"); return;
        }

        VideojuegoPlataforma vp = new VideojuegoPlataforma(vj, pl, fecha);

        try {
            // Esta tabla solo permite insertar o eliminar (no tiene UPDATE porque la PK es compuesta)
            vpDAO.insertar(vp);
            cargarDatos();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        vpDAO.buscarTodos().forEach(vp -> {
            Videojuego vj = videojuegoDAO.buscarPorId(vp.getVideojuego().getIdVideojuego());
            Plataforma pl = plataformaDAO.buscarPorId(vp.getPlataforma().getIdPlataforma());
            String nombreVJ = vj != null ? vj.getNombre() : "ID " + vp.getVideojuego().getIdVideojuego();
            String nombrePl = pl != null ? pl.getNombre() : "ID " + vp.getPlataforma().getIdPlataforma();
            agregarFila(vp, nombreVJ, nombrePl);
        });
    }

    private void agregarFila(VideojuegoPlataforma vp, String nombreVJ, String nombrePlat) {
        modelo.addRow(new Object[]{
                nombreVJ,
                nombrePlat,
                vp.getFechaLanzamientoPlataforma()
        });
    }

    private void limpiar() {
        if (comboVideojuego.getItemCount() > 0) comboVideojuego.setSelectedIndex(0);
        if (comboPlataforma.getItemCount()  > 0) comboPlataforma.setSelectedIndex(0);
        campoFecha.setText("");
        idVJSeleccionado   = -1;
        idPlatSeleccionado = -1;
    }

    private void seleccionarComboVideojuego(String nombre) {
        for (int i = 0; i < comboVideojuego.getItemCount(); i++) {
            if (comboVideojuego.getItemAt(i).getNombre().equals(nombre)) {
                comboVideojuego.setSelectedIndex(i);
                return;
            }
        }
    }

    private void seleccionarComboPlataforma(String nombre) {
        for (int i = 0; i < comboPlataforma.getItemCount(); i++) {
            if (comboPlataforma.getItemAt(i).getNombre().equals(nombre)) {
                comboPlataforma.setSelectedIndex(i);
                return;
            }
        }
    }

    private void exportar() {
        String ruta = System.getProperty("user.home") + "/Downloads/reporte_vj_plataformas.txt";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(ruta))) {
            bw.write("Videojuego | Plataforma | Fecha Lanzamiento");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    if (j > 0) bw.write(" | ");
                    bw.write(String.valueOf(modelo.getValueAt(i, j)));
                }
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado en Descargas/reporte_vj_plataformas.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }
}
