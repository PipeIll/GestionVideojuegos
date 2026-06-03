package gestionvideojuegos.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import gestionvideojuegos.dao.GeneroDAO;
import gestionvideojuegos.model.Genero;

public class PanelGeneros extends JPanel {  // <- falta esto

    private GeneroDAO generoDAO = new GeneroDAO();
    public PanelGeneros() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 28));

        // titulo
        JLabel titulo = new JLabel("Generos");
        titulo.setForeground(new Color(220, 220, 230));
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));

        List<Genero> lista = generoDAO.buscarTodos();
        Object[][] datos = new Object[lista.size()][3];
        for(int i = 0; i < lista.size(); i++){
            datos[i][0] = lista.get(i).getIdGenero();
            datos[i][1] = lista.get(i).getNombre();
            datos[i][2] = lista.get(i).getDescripcion();
        }

        // tabla
        String[] columnas = {"ID", "Nombre", "Descripcion"};
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

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(500);

        JPanel formulario = new JPanel();
        formulario.setLayout(new BoxLayout(formulario, BoxLayout.Y_AXIS));
        formulario.setBackground(new Color(25, 25, 38));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JTextField campoNombre = ComponenteFactory.crearCampo();
        JTextField campoDesc = ComponenteFactory.crearCampo();
        JButton btnGuardar = ComponenteFactory.crearBotonGuardar();
        JButton btnLimpiar = ComponenteFactory.crearBotonLimpiar();
        JButton btnEliminar = ComponenteFactory.crearBotonEliminar();
        JButton btnExportar = ComponenteFactory.crearBotonExportar();

        formulario.add(ComponenteFactory.crearLabel("Nombre:"));
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(campoNombre);
        formulario.add(Box.createVerticalStrut(15));
        formulario.add(ComponenteFactory.crearLabel("Descripcion:"));
        formulario.add(Box.createVerticalStrut(5));
        formulario.add(campoDesc);
        formulario.add(Box.createVerticalGlue());
        formulario.add(btnGuardar);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnLimpiar);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnEliminar);
        formulario.add(Box.createVerticalStrut(8));
        formulario.add(btnExportar);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, formulario);
        split.setDividerLocation(600);
        split.setResizeWeight(0.7);
        split.setBackground(new Color(18, 18, 28));

        add(titulo, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);


    }
}