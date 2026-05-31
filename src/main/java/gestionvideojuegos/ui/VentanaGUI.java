    package gestionvideojuegos.ui;


    import gestionvideojuegos.dao.GeneroDAO;
    import gestionvideojuegos.model.Genero;


    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.util.List;

    import javax.swing.*;
    import java.awt.*;

    public class VentanaGUI extends JFrame {

        private GeneroDAO generoDAO = new GeneroDAO();

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

            List<Genero> lista = generoDAO.buscarTodos();

            String[] columnas = {"ID", "Nombre", "Descripción"};
            Object[][] datos = new Object[lista.size()][3];
            for (int i = 0; i < lista.size(); i++ ){
                datos[i][0] = lista.get(i).getIdGenero();
                datos[i][1] = lista.get(i).getNombre();
                datos[i][2] = lista.get(i).getDescripcion();
            }
            JTable tabla = new JTable(datos, columnas);
            JScrollPane scroll =new JScrollPane(tabla);

            tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
            tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabla.getColumnModel().getColumn(2).setPreferredWidth(700);

            panel.add(scroll, BorderLayout.CENTER);
            panel.add(BotonesGeneros(),BorderLayout.SOUTH);
            return panel;
        }
        private JPanel BotonesGeneros() {
            JPanel panelBotones = new JPanel();
            JButton agregar = new JButton("Agregar");
            JButton buscarId = new JButton("Buscar por ID");
            JButton eliminar = new JButton("Eliminar");
            JButton exportar = new JButton("Exportar");

            panelBotones.add(agregar);
            panelBotones.add(buscarId);
            panelBotones.add(eliminar);
            panelBotones.add(exportar);

            agregar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){

                }
            });

            return panelBotones;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new VentanaGUI().setVisible(true);
            });
        }
    }
