package gestionvideojuegos.ui;

import javax.swing.*;
import java.awt.*;

public class ComponenteFactory {

    public static JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setBackground(new Color(35, 35, 50));
        campo.setForeground(new Color(220, 220, 230));
        campo.setCaretColor(new Color(220, 220, 230));
        return campo;
    }

    public static JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setForeground(new Color(220, 220, 230));
        return label;
    }

    public static JButton crearBotonGuardar() {
        return crearBoton("Guardar", new Color(60, 80, 180));
    }

    public static JButton crearBotonLimpiar() {
        return crearBoton("Limpiar", new Color(50, 50, 70));
    }

    public static JButton crearBotonEliminar() {
        return crearBoton("Eliminar", new Color(150, 40, 40));
    }

    public static JButton crearBotonExportar() {
        return crearBoton("Exportar", new Color(40, 100, 70));
    }

    public static JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return btn;
    }

}
