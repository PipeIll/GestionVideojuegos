package gestionvideojuegos.ui;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {
    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public Sidebar(CardLayout cardLayout, JPanel panelPrincipal) {
        this.cardLayout = cardLayout;
        this.panelPrincipal = panelPrincipal;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(13, 13, 20));
        setPreferredSize(new Dimension(200, 0));

        // titulo
        JLabel titulo = new JLabel("MIS VIDEOJUEGOS");
        titulo.setForeground(new Color(99, 102, 241));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));

        JLabel subtitulo = new JLabel("Database");
        subtitulo.setForeground(new Color(120, 120, 140));
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel navLabel = new JLabel("NAVEGACION");
        navLabel.setForeground(new Color(100, 100, 120));
        navLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        navLabel.setAlignmentX(LEFT_ALIGNMENT);
        navLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));

        add(titulo);
        add(subtitulo);
        add(navLabel);

        // botones de navegacion
        agregarBoton("Resumen");
        agregarBoton("Juegos");
        agregarBoton("Desarrolladoras");
        agregarBoton("Generos");
        agregarBoton("Plataformas");
        agregarBoton("DLCs");
        agregarBoton("Premios");
        agregarBoton("VJ - Plataforma");

        add(Box.createVerticalGlue());
    }

    private void agregarBoton(String nombre) {
        JButton btn = new JButton(nombre);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBackground(new Color(13, 13, 20));
        btn.setForeground(new Color(200, 200, 220));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 0));
        btn.addActionListener(e -> cardLayout.show(panelPrincipal, nombre));
        add(btn);
        add(Box.createVerticalStrut(5));
    }
}
