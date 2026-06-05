package gestionvideojuegos.ui;

import gestionvideojuegos.dao.DesarrolladoraDAO;
import gestionvideojuegos.dao.GeneroDAO;
import gestionvideojuegos.dao.VideojuegoDAO;
import gestionvideojuegos.model.Desarrolladora;
import gestionvideojuegos.model.Genero;
import gestionvideojuegos.model.Videojuego;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class PanelVideojuegos extends JPanel {

    private VideojuegoDAO    videojuegoDAO    = new VideojuegoDAO();
    private GeneroDAO        generoDAO        = new GeneroDAO();
    private DesarrolladoraDAO desarrolladoraDAO = new DesarrolladoraDAO();

    private DefaultTableModel modelo;
    private int idSeleccionado = -1;

    // Campos del formulario
    private JTextField     campoNombre;
    private JComboBox<Genero>        comboGenero;
    private JComboBox<Desarrolladora> comboDesarrolladora;
    private JTextField     campoFecha;       // formato YYYY-MM-DD
    private JTextField     campoPrecio;
    private JTextField     campoRating;
    private JComboBox<String> comboDificultad;
    private JComboBox<String> comboClasificacion;
    private JComboBox<String> comboModo;
    private JTextField     campoDescripcion;

    public PanelVideojuegos() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // ── Título ──────────────────────────────────────────────────
        JLabel titulo = new JLabel("Videojuegos");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        // ── Tabla ───────────────────────────────────────────────────
        String[] columnas = {"ID", "Nombre", "Género", "Desarrolladora",
                "Fecha", "Precio", "Rating", "Dificultad",
                "Clasificación", "Modo", "Descripción"};
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

        // ── Botones inferiores de la tabla ──────────────────────────
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        panelBotones.setBackground(new Color(13, 13, 20));
        JButton btnBuscarId     = ComponenteFactory.crearBoton("Buscar por ID",     new Color(50, 50, 70));
        JButton btnBuscarNombre = ComponenteFactory.crearBoton("Buscar por Nombre", new Color(50, 50, 70));
        JButton btnVerTodos     = ComponenteFactory.crearBoton("Ver todos",          new Color(50, 50, 70));
        JButton btnEliminar     = ComponenteFactory.crearBoton("Eliminar",           new Color(150, 40, 40));
        JButton btnExportar     = ComponenteFactory.crearBoton("Exportar",           new Color(40, 100, 70));
        panelBotones.add(btnBuscarId);
        panelBotones.add(btnBuscarNombre);
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
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            campoNombre.setText(modelo.getValueAt(fila, 1).toString());
            seleccionarComboNombre(comboGenero,        modelo.getValueAt(fila, 2).toString());
            seleccionarComboNombre(comboDesarrolladora, modelo.getValueAt(fila, 3).toString());
            campoFecha.setText(modelo.getValueAt(fila, 4).toString());
            campoPrecio.setText(modelo.getValueAt(fila, 5).toString());
            campoRating.setText(modelo.getValueAt(fila, 6).toString());
            seleccionarComboString(comboDificultad,    modelo.getValueAt(fila, 7).toString());
            seleccionarComboString(comboClasificacion, modelo.getValueAt(fila, 8).toString());
            seleccionarComboString(comboModo,          modelo.getValueAt(fila, 9).toString());
            campoDescripcion.setText(modelo.getValueAt(fila, 10).toString());
        });

        // ── Acciones ────────────────────────────────────────────────
        JButton btnGuardar = (JButton) formulario.getClientProperty("btnGuardar");
        JButton btnLimpiar = (JButton) formulario.getClientProperty("btnLimpiar");

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnEliminar.addActionListener(e -> {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un videojuego primero");
                return;
            }
            int ok = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar este videojuego?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                videojuegoDAO.eliminar(idSeleccionado);
                cargarDatos();
                limpiar();
            }
        });

        btnBuscarId.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el ID:");
            if (input != null && !input.isEmpty()) {
                try {
                    Videojuego v = videojuegoDAO.buscarPorId(Integer.parseInt(input));
                    modelo.setRowCount(0);
                    if (v != null) agregarFila(v);
                    else JOptionPane.showMessageDialog(this, "No encontrado");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser un número");
                }
            }
        });

        btnBuscarNombre.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "Ingresa el nombre:");
            if (input != null && !input.isEmpty()) {
                modelo.setRowCount(0);
                videojuegoDAO.filtrar(input, null, null, null, null, null, null,
                        null, null, null, null).forEach(this::agregarFila);
            }
        });

        btnVerTodos.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportar());

        cargarDatos();
    }

    // ── Construye el panel de formulario ────────────────────────────
    private JPanel construirFormulario() {
        // Datos para los JComboBox
        List<Genero>         generos         = generoDAO.buscarTodos();
        List<Desarrolladora> desarrolladoras = desarrolladoraDAO.buscarTodos();

        comboGenero = new JComboBox<>(generos.toArray(new Genero[0]));
        comboDesarrolladora = new JComboBox<>(desarrolladoras.toArray(new Desarrolladora[0]));

        // Renderer para que el combo muestre el nombre del objeto en lugar de toString()
        comboGenero.setRenderer((list, value, i, sel, foc) -> {
            JLabel l = new JLabel(value != null ? value.getNombre() : "");
            l.setOpaque(true);
            l.setBackground(sel ? new Color(60, 80, 180) : new Color(35, 35, 50));
            l.setForeground(new Color(220, 220, 230));
            l.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return l;
        });
        comboDesarrolladora.setRenderer((list, value, i, sel, foc) -> {
            JLabel l = new JLabel(value != null ? value.getNombre() : "");
            l.setOpaque(true);
            l.setBackground(sel ? new Color(60, 80, 180) : new Color(35, 35, 50));
            l.setForeground(new Color(220, 220, 230));
            l.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            return l;
        });

        estilizarCombo(comboGenero);
        estilizarCombo(comboDesarrolladora);

        campoNombre      = ComponenteFactory.crearCampo();
        campoFecha       = ComponenteFactory.crearCampo(); // YYYY-MM-DD
        campoPrecio      = ComponenteFactory.crearCampo();
        campoRating      = ComponenteFactory.crearCampo();
        campoDescripcion = ComponenteFactory.crearCampo();

        comboDificultad    = crearComboString("Fácil", "Normal", "Difícil", "Muy difícil");
        comboClasificacion = crearComboString("E", "E10+", "T", "M", "AO");
        comboModo          = crearComboString("Un jugador", "Multijugador", "Cooperativo", "MMO");

        JButton btnGuardar = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar = ComponenteFactory.crearBotonLimpiar();

        JPanel panel = ComponenteFactory.crearPanelFormulario();
        ComponenteFactory.agregarCampo(panel, "Nombre:", campoNombre);
        agregarCombo(panel, "Género:", comboGenero);
        agregarCombo(panel, "Desarrolladora:", comboDesarrolladora);
        ComponenteFactory.agregarCampo(panel, "Fecha (YYYY-MM-DD):", campoFecha);
        ComponenteFactory.agregarCampo(panel, "Precio:", campoPrecio);
        ComponenteFactory.agregarCampo(panel, "Rating:", campoRating);
        agregarCombo(panel, "Dificultad:", comboDificultad);
        agregarCombo(panel, "Clasificación:", comboClasificacion);
        agregarCombo(panel, "Modo:", comboModo);
        ComponenteFactory.agregarCampo(panel, "Descripción:", campoDescripcion);
        panel.add(Box.createVerticalGlue());
        panel.add(btnGuardar);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnLimpiar);

        // Guardamos referencias a los botones para acceder desde el constructor
        panel.putClientProperty("btnGuardar", btnGuardar);
        panel.putClientProperty("btnLimpiar", btnLimpiar);

        return panel;
    }

    // ── Lógica de datos ──────────────────────────────────────────────
    private void guardar() {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio"); return;
        }
        Genero        genero = (Genero)        comboGenero.getSelectedItem();
        Desarrolladora des   = (Desarrolladora) comboDesarrolladora.getSelectedItem();
        if (genero == null || des == null) {
            JOptionPane.showMessageDialog(this, "Selecciona género y desarrolladora"); return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(campoFecha.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa el formato YYYY-MM-DD"); return;
        }
        double precio, rating;
        try {
            precio = Double.parseDouble(campoPrecio.getText().trim());
            rating = Double.parseDouble(campoRating.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y Rating deben ser números"); return;
        }

        Videojuego v = new Videojuego(
                idSeleccionado == -1 ? 0 : idSeleccionado,
                nombre, genero, des, fecha, precio, rating,
                (String) comboDificultad.getSelectedItem(),
                (String) comboClasificacion.getSelectedItem(),
                (String) comboModo.getSelectedItem(),
                campoDescripcion.getText().trim()
        );

        try {
            if (idSeleccionado == -1) videojuegoDAO.insertar(v);
            else                      videojuegoDAO.actualizar(v);
            cargarDatos();
            limpiar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        videojuegoDAO.buscarTodos().forEach(this::agregarFila);
    }

    private void agregarFila(Videojuego v) {
        modelo.addRow(new Object[]{
                v.getIdVideojuego(),
                v.getNombre(),
                v.getGenero()        != null ? v.getGenero().getNombre()         : "",
                v.getDesarrolladora() != null ? v.getDesarrolladora().getNombre() : "",
                v.getFechaLanzamiento(),
                v.getPrecio(),
                v.getRating(),
                v.getDificultad(),
                v.getClasificacionEdad(),
                v.getModoJuego(),
                v.getDescripcion()
        });
    }

    private void limpiar() {
        campoNombre.setText("");
        campoFecha.setText("");
        campoPrecio.setText("");
        campoRating.setText("");
        campoDescripcion.setText("");
        if (comboGenero.getItemCount() > 0)        comboGenero.setSelectedIndex(0);
        if (comboDesarrolladora.getItemCount() > 0) comboDesarrolladora.setSelectedIndex(0);
        comboDificultad.setSelectedIndex(0);
        comboClasificacion.setSelectedIndex(0);
        comboModo.setSelectedIndex(0);
        idSeleccionado = -1;
    }

    private void exportar() {
        String ruta = System.getProperty("user.home") + "/Downloads/reporte_videojuegos.txt";
        try (java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(ruta))) {
            bw.write("ID | Nombre | Género | Desarrolladora | Fecha | Precio | Rating | Dificultad | Clasificación | Modo | Descripción");
            bw.newLine();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                for (int j = 0; j < modelo.getColumnCount(); j++) {
                    if (j > 0) bw.write(" | ");
                    bw.write(String.valueOf(modelo.getValueAt(i, j)));
                }
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Exportado en Descargas/reporte_videojuegos.txt");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
        }
    }

    // ── Helpers de UI ────────────────────────────────────────────────
    private void agregarCombo(JPanel panel, String etiqueta, JComboBox<?> combo) {
        JLabel lbl = ComponenteFactory.crearLabel(etiqueta);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(5));
        panel.add(combo);
        panel.add(Box.createVerticalStrut(15));
    }

    private JComboBox<String> crearComboString(String... opciones) {
        JComboBox<String> combo = new JComboBox<>(opciones);
        estilizarCombo(combo);
        return combo;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setBackground(new Color(35, 35, 50));
        combo.setForeground(new Color(220, 220, 230));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    }

    /** Selecciona en el combo el item cuyo getNombre() coincide con el texto. */
    private <T> void seleccionarComboNombre(JComboBox<T> combo, String nombre) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            String n = item instanceof Genero        ? ((Genero) item).getNombre()
                    : item instanceof Desarrolladora ? ((Desarrolladora) item).getNombre()
                      : item.toString();
            if (n.equals(nombre)) { combo.setSelectedIndex(i); return; }
        }
    }

    private void seleccionarComboString(JComboBox<String> combo, String valor) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).equals(valor)) { combo.setSelectedIndex(i); return; }
        }
    }
}