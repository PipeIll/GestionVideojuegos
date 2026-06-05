package gestionvideojuegos.ui.Resumen;

import gestionvideojuegos.dao.PanelResumenDAO;
import gestionvideojuegos.ui.ComponenteFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.Map;

// Panel principal del dashboard con estadisticas generales del sistema
// Se puede exportar a un archivo .doc (RTF) apto para Word y LibreOffice
public class PanelResumen extends JPanel {

    // Paleta de colores consistente con el resto de la app
    private static final Color BG_MAIN     = new Color(18, 18, 28);
    private static final Color BG_CARD     = new Color(25, 25, 38);
    private static final Color BG_DARK     = new Color(13, 13, 20);
    private static final Color COLOR_ACENT = new Color(99, 102, 241);
    private static final Color COLOR_TEXT  = new Color(220, 220, 230);
    private static final Color COLOR_MUTED = new Color(120, 120, 140);
    private static final Color COLOR_GREEN = new Color(40, 167, 100);
    private static final Color COLOR_AMBER = new Color(200, 150, 50);

    private final PanelResumenDAO dao = new PanelResumenDAO();

    // Panel central con scroll donde se renderizan todas las tarjetas
    private JPanel contenido;

    public PanelResumen() {
        setLayout(new BorderLayout());
        setBackground(BG_MAIN);

        add(construirCabecera(), BorderLayout.NORTH);

        contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setBackground(BG_MAIN);
        contenido.setBorder(new EmptyBorder(10, 20, 20, 20));

        JScrollPane scroll = new JScrollPane(contenido);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(BG_MAIN);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        cargarEstadisticas();
    }

    // ─── Cabecera con título y boton de exportar ──────────────────────────────

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBackground(BG_DARK);
        cabecera.setBorder(new EmptyBorder(18, 20, 12, 20));

        JLabel titulo = new JLabel("Resumen del Sistema");
        titulo.setForeground(COLOR_TEXT);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel subtitulo = new JLabel("Estadisticas en tiempo real de la base de datos");
        subtitulo.setForeground(COLOR_MUTED);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setBackground(BG_DARK);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(subtitulo);

        JButton btnRefrescar = ComponenteFactory.crearBoton("↻  Refrescar", new Color(50, 50, 70));
        btnRefrescar.setMaximumSize(new Dimension(130, 36));
        btnRefrescar.setPreferredSize(new Dimension(130, 36));

        JButton btnExportar = ComponenteFactory.crearBoton("⬇  Exportar .doc", new Color(40, 100, 70));
        btnExportar.setMaximumSize(new Dimension(150, 36));
        btnExportar.setPreferredSize(new Dimension(150, 36));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setBackground(BG_DARK);
        botones.add(btnRefrescar);
        botones.add(btnExportar);

        cabecera.add(textos, BorderLayout.WEST);
        cabecera.add(botones, BorderLayout.EAST);

        btnRefrescar.addActionListener(e -> {
            contenido.removeAll();
            cargarEstadisticas();
            contenido.revalidate();
            contenido.repaint();
        });

        btnExportar.addActionListener(e -> exportarResumen());

        return cabecera;
    }

    // ─── Carga de datos y construccion del contenido ──────────────────────────

    private void cargarEstadisticas() {
        // Conteos generales
        int juegos           = dao.contarVideojuegos();
        int generos          = dao.contarGeneros();
        int plataformas      = dao.contarPlataformas();
        int desarrolladoras  = dao.contarDesarrolladoras();
        int dlcs             = dao.contarDlcs();
        int premios          = dao.contarPremios();
        double precioPromedio  = dao.obtenerPrecioPromedio();
        double ratingPromedio  = dao.obtenerRatingPromedio();
        double dlcPromedio     = dao.promedioDlcsPorJuego();
        double premioPromedio  = dao.promedioPremiosPorJuego();

        // ── Fila 1: tarjetas de conteos ──────────────────────────────────────
        JPanel filaTarjetas = new JPanel(new GridLayout(1, 6, 12, 0));
        filaTarjetas.setBackground(BG_MAIN);
        filaTarjetas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        filaTarjetas.setBorder(new EmptyBorder(0, 0, 14, 0));

        filaTarjetas.add(tarjeta("Videojuegos",    String.valueOf(juegos),          COLOR_ACENT));
        filaTarjetas.add(tarjeta("Generos",        String.valueOf(generos),          COLOR_GREEN));
        filaTarjetas.add(tarjeta("Plataformas",    String.valueOf(plataformas),      COLOR_AMBER));
        filaTarjetas.add(tarjeta("Desarrolladoras",String.valueOf(desarrolladoras),  new Color(200, 80, 80)));
        filaTarjetas.add(tarjeta("DLCs",           String.valueOf(dlcs),             new Color(80, 160, 200)));
        filaTarjetas.add(tarjeta("Premios",        String.valueOf(premios),          new Color(180, 130, 200)));

        // ── Fila 2: promedios ─────────────────────────────────────────────────
        JPanel filaPromedios = new JPanel(new GridLayout(1, 4, 12, 0));
        filaPromedios.setBackground(BG_MAIN);
        filaPromedios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        filaPromedios.setBorder(new EmptyBorder(0, 0, 14, 0));

        filaPromedios.add(tarjeta("Precio Promedio",  String.format("$ %.2f", precioPromedio),  COLOR_ACENT));
        filaPromedios.add(tarjeta("Rating Promedio",  String.format("%.2f / 10", ratingPromedio), COLOR_GREEN));
        filaPromedios.add(tarjeta("DLCs / Juego",     String.format("%.2f", dlcPromedio),         COLOR_AMBER));
        filaPromedios.add(tarjeta("Premios / Juego",  String.format("%.2f", premioPromedio),       new Color(180, 130, 200)));

        // ── Fila 3: rankings ──────────────────────────────────────────────────
        JPanel filaRankings = new JPanel(new GridLayout(1, 3, 12, 0));
        filaRankings.setBackground(BG_MAIN);
        filaRankings.setBorder(new EmptyBorder(0, 0, 14, 0));

        filaRankings.add(rankingPanel("Top Generos por Juegos",       dao.topGenerosPorJuegos(), " juego(s)"));
        filaRankings.add(rankingPanel("Top Desarrolladoras",          dao.topDesarrolladorasPorJuegos(), " juego(s)"));
        filaRankings.add(rankingPanel("Top 3 Juegos por Rating",      dao.topJuegosPorRating()));

        // ── Fila 4: distribuciones ────────────────────────────────────────────
        JPanel filaDistrib = new JPanel(new GridLayout(1, 2, 12, 0));
        filaDistrib.setBackground(BG_MAIN);
        filaDistrib.setBorder(new EmptyBorder(0, 0, 14, 0));

        filaDistrib.add(rankingPanel("Distribucion por Dificultad", dao.distribucionPorDificultad(), " juego(s)"));
        filaDistrib.add(rankingPanel("Distribucion por Modo de Juego", dao.distribucionPorModo(), " juego(s)"));

        contenido.add(filaTarjetas);
        contenido.add(filaPromedios);
        contenido.add(filaRankings);
        contenido.add(filaDistrib);
    }

    // ─── Componentes reutilizables ────────────────────────────────────────────

    // Tarjeta simple de metrica: etiqueta + valor grande
    private JPanel tarjeta(String etiqueta, String valor, Color colorValor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(colorValor);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblValor.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setForeground(COLOR_MUTED);
        lblEtiqueta.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblEtiqueta.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(lblValor);
        card.add(Box.createVerticalStrut(6));
        card.add(lblEtiqueta);
        card.add(Box.createVerticalGlue());
        return card;
    }

    // Panel de ranking para Map<String, Integer>
    private JPanel rankingPanel(String titulo, Map<String, Integer> datos, String sufijo) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));

        if (datos.isEmpty()) {
            JLabel vacio = new JLabel("Sin datos disponibles");
            vacio.setForeground(COLOR_MUTED);
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 12));
            card.add(vacio);
        } else {
            int rank = 1;
            for (Map.Entry<String, Integer> e : datos.entrySet()) {
                card.add(filaRanking(rank + ". " + e.getKey(), e.getValue() + sufijo));
                card.add(Box.createVerticalStrut(5));
                rank++;
            }
        }
        card.add(Box.createVerticalGlue());
        return card;
    }

    // Panel de ranking para Map<String, Double> (usado en top por rating)
    private JPanel rankingPanel(String titulo, Map<String, Double> datos) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(COLOR_TEXT);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        card.add(lblTitulo);
        card.add(Box.createVerticalStrut(10));

        if (datos.isEmpty()) {
            JLabel vacio = new JLabel("Sin datos disponibles");
            vacio.setForeground(COLOR_MUTED);
            vacio.setFont(new Font("SansSerif", Font.ITALIC, 12));
            card.add(vacio);
        } else {
            int rank = 1;
            for (Map.Entry<String, Double> e : datos.entrySet()) {
                card.add(filaRanking(rank + ". " + e.getKey(), String.format("%.2f", e.getValue())));
                card.add(Box.createVerticalStrut(5));
                rank++;
            }
        }
        card.add(Box.createVerticalGlue());
        return card;
    }

    // Fila individual de un ranking: nombre a la izquierda, valor a la derecha
    private JPanel filaRanking(String nombre, String valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setBackground(BG_CARD);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(COLOR_TEXT);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(COLOR_ACENT);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblNombre, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        return fila;
    }

    // ─── Exportar ─────────────────────────────────────────────────────────────

    private void exportarResumen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar resumen como...");
        chooser.setSelectedFile(new File("resumen_videojuegos.doc"));

        int resultado = chooser.showSaveDialog(this);
        if (resultado != JFileChooser.APPROVE_OPTION) return;

        String ruta = chooser.getSelectedFile().getAbsolutePath();
        if (!ruta.endsWith(".doc") && !ruta.endsWith(".rtf")) {
            ruta += ".doc";
        }

        ResumenDocxExporter exporter = new ResumenDocxExporter();
        String archivo = exporter.exportar(ruta);

        if (archivo != null) {
            JOptionPane.showMessageDialog(this,
                    "Resumen exportado correctamente:\n" + archivo,
                    "Exportacion exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error al exportar el resumen.",
                    "Error de exportacion",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}