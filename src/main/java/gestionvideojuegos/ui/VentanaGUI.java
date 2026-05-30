    package gestionvideojuegos.ui;

    import javax.swing.*;
    import java.awt.*;

    public class VentanaGUI extends JFrame {

        public VentanaGUI(){
            setTitle("Gestion de Videojuegos");
            setSize(900, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Generos", PanelGeneros());
            tabs.addTab("Plataformas", new JPanel());
            tabs.addTab("Desarrolladoras", new JPanel());
            tabs.addTab("Videojuegos", new JPanel());
            tabs.addTab("DLCs", new JPanel());
            tabs.addTab("Premios", new JPanel());
            tabs.addTab("VideoJuego - Plataforma", new JPanel());

            add(tabs);
        }

        private JPanel PanelGeneros(){
            JPanel panel = new JPanel(new BorderLayout());

            String[] columnas = {"ID", "Nombre", "Descripción"};
            Object[][] datos = {};
            JTable tabla = new JTable(datos, columnas);
            JScrollPane scroll =new JScrollPane(tabla);

            panel.add(scroll, BorderLayout.CENTER);
            return panel;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new VentanaGUI().setVisible(true);
            });
        }
    }
