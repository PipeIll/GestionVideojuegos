package gestionvideojuegos.ui;

import gestionvideojuegos.dao.PlataformaDAO;
import gestionvideojuegos.model.Plataforma;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelPlataformas extends JPanel {  // <- falta esto

    private PlataformaDAO plataformaDAO = new PlataformaDAO();
    public PanelPlataformas() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // titulo
        JLabel titulo = new JLabel("Plataformas");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        List<Plataforma> lista = plataformaDAO.buscarTodos();
        Object[][] datos = new Object[lista.size()][4];
        for(int i = 0; i < lista.size(); i++){
            datos[i][0] = lista.get(i).getIdPlataforma();
            datos[i][1] = lista.get(i).getNombre();
            datos[i][2] = lista.get(i).getFabricante();
            datos[i][3] = lista.get(i).getAñoLanzamiento();
        }

        // tabla
        String[] columnas = {"ID", "Nombre", "Fabricante", "Año Lanzamiento"};
        JTable tabla = new JTable(datos, columnas);
        tabla.setBackground(new Color(25, 25, 38));
        tabla.setForeground(new Color(220, 220, 230));
        tabla.setGridColor(new Color(40, 40, 55));
        tabla.getTableHeader().setBackground(new Color(13, 13, 20));
        tabla.getTableHeader().setForeground(new Color(150, 150, 170));
        tabla.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBackground(new Color(18, 18, 28));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(25, 25, 38));

        add(titulo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
}