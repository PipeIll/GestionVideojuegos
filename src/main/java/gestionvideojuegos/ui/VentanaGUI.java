package gestionvideojuegos.ui;

import javax.swing.*;
import java.awt.*;

public class VentanaGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelPrincipal;

    public VentanaGUI() {
        setTitle("Mis Videojuegos - Database");
        setSize(1100, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(18, 18, 28));

        cardLayout = new CardLayout();
        panelPrincipal = new JPanel(cardLayout);
        panelPrincipal.setBackground(new Color(18, 18, 28));

        Sidebar sidebar = new Sidebar(cardLayout, panelPrincipal);
        add(sidebar, BorderLayout.WEST);
        add(panelPrincipal, BorderLayout.CENTER);

        panelPrincipal.add(new PanelGeneros(), "Generos");
        panelPrincipal.add(new PanelPlataformas(), "Plataformas");
        panelPrincipal.add(new PanelDesarrolladoras(), "Desarrolladoras");

        JPanel temp = new JPanel();
        temp.setBackground(new Color(255, 0, 0));
        panelPrincipal.add(temp, "Resumen");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaGUI().setVisible(true));
    }
}
