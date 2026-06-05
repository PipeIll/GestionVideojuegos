package gestionvideojuegos.ui.Resumen;

import gestionvideojuegos.dao.PanelResumenDAO;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;

// Genera un archivo .docx con el resumen del sistema usando formato RTF-fallback simple
// Si quieres un .docx real con apache POI, agrega la dependencia en pom.xml:
//   <groupId>org.apache.poi</groupId> / <artifactId>poi-ooxml</artifactId> / <version>5.2.5</version>
// Por ahora exporta en formato RTF (.doc legible en Word, LibreOffice, etc.)
public class ResumenDocxExporter {

    private final PanelResumenDAO dao;

    public ResumenDocxExporter() {
        this.dao = new PanelResumenDAO();
    }

    // Exporta el resumen como RTF (abre en Word/LibreOffice sin dependencias adicionales)
    // Retorna la ruta del archivo generado o null si falla
    public String exportar(String rutaSalida) {
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(rutaSalida), "UTF-8"))) {

            writer.println("{\\rtf1\\ansi\\ansicpg1252\\deff0");
            writer.println("{\\fonttbl{\\f0\\fswiss\\fcharset0 Arial;}{\\f1\\fswiss\\fcharset0 Arial;}}");
            writer.println("{\\colortbl ;\\red30\\green30\\blue30;\\red99\\green102\\blue241;\\red80\\green80\\blue80;}");
            writer.println("\\widowctrl\\hyphauto");
            writer.println("\\margl1800\\margr1800\\margt1440\\margb1440");

            // ── Título ──────────────────────────────────────────────
            writer.println("\\pard\\sb400\\sa200\\qc{\\f0\\fs40\\b\\cf2 Resumen del Sistema de Gestion de Videojuegos}\\par");
            writer.println("\\pard\\sb0\\sa200\\qc{\\f0\\fs22\\cf3 Generado el " + LocalDate.now() + "}\\par");
            writer.println("\\pard\\sb200\\sa200\\qc{\\f0\\fs22\\cf3 Proyecto: Gestion de Videojuegos — POO 2026 — Felipe}\\par");

            // ── Linea separadora simulada ────────────────────────────
            writer.println("\\pard\\sb300\\sa300{\\f0\\fs16\\cf3 _______________________________________________________________}\\par");

            // ── Descripcion del proyecto ─────────────────────────────
            writer.println("\\pard\\sb200\\sa100{\\f0\\fs28\\b\\cf2 Descripcion del Proyecto}\\par");
            writer.println("\\pard\\sb100\\sa200\\sl276\\slmult1{\\f0\\fs22\\cf1 " +
                    "Este sistema permite gestionar una base de datos de videojuegos conectada a PostgreSQL " +
                    "a traves de Neon (servidor remoto). Aplica el patron DAO para separar la logica de acceso " +
                    "a datos de la interfaz grafica. Fue desarrollado como proyecto del segundo parcial de " +
                    "Programacion Orientada a Objetos." +
                    "}\\par");
            writer.println("\\pard\\sb100\\sa200\\sl276\\slmult1{\\f0\\fs22\\cf1 " +
                    "La interfaz grafica (Swing) incluye paneles para Generos, Plataformas, Desarrolladoras, " +
                    "Videojuegos, DLCs, Premios y la relacion Videojuego-Plataforma, ademas de este panel de resumen " +
                    "con estadisticas en tiempo real." +
                    "}\\par");

            // ── Estadisticas generales ───────────────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Estadisticas Generales}\\par");

            int juegos      = dao.contarVideojuegos();
            int generos     = dao.contarGeneros();
            int plataformas = dao.contarPlataformas();
            int desarrolladoras = dao.contarDesarrolladoras();
            int dlcs        = dao.contarDlcs();
            int premios     = dao.contarPremios();
            double precioPromedio  = dao.obtenerPrecioPromedio();
            double ratingPromedio  = dao.obtenerRatingPromedio();
            double dlcPromedio     = dao.promedioDlcsPorJuego();
            double premioPromedio  = dao.promedioPremiosPorJuego();

            escribirFila(writer, "Total de videojuegos",    String.valueOf(juegos));
            escribirFila(writer, "Total de generos",        String.valueOf(generos));
            escribirFila(writer, "Total de plataformas",    String.valueOf(plataformas));
            escribirFila(writer, "Total de desarrolladoras",String.valueOf(desarrolladoras));
            escribirFila(writer, "Total de DLCs",           String.valueOf(dlcs));
            escribirFila(writer, "Total de premios",        String.valueOf(premios));
            escribirFila(writer, "Precio promedio (USD)",   String.format("$ %.2f", precioPromedio));
            escribirFila(writer, "Rating promedio",         String.format("%.2f / 10", ratingPromedio));
            escribirFila(writer, "DLCs promedio por juego", String.format("%.2f", dlcPromedio));
            escribirFila(writer, "Premios promedio por juego", String.format("%.2f", premioPromedio));

            // ── Top generos ──────────────────────────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Top 5 Generos con mas Juegos}\\par");
            Map<String, Integer> topGeneros = dao.topGenerosPorJuegos();
            if (topGeneros.isEmpty()) {
                writer.println("\\pard\\sb80\\sa80{\\f0\\fs22\\cf3 Sin datos disponibles}\\par");
            } else {
                int rank = 1;
                for (Map.Entry<String, Integer> e : topGeneros.entrySet()) {
                    writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                            rank + ". " + escapar(e.getKey()) + "  \u2014  " + e.getValue() + " juego(s)}\\par");
                    rank++;
                }
            }

            // ── Top desarrolladoras ──────────────────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Top 5 Desarrolladoras con mas Juegos}\\par");
            Map<String, Integer> topDev = dao.topDesarrolladorasPorJuegos();
            if (topDev.isEmpty()) {
                writer.println("\\pard\\sb80\\sa80{\\f0\\fs22\\cf3 Sin datos disponibles}\\par");
            } else {
                int rank = 1;
                for (Map.Entry<String, Integer> e : topDev.entrySet()) {
                    writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                            rank + ". " + escapar(e.getKey()) + "  \u2014  " + e.getValue() + " juego(s)}\\par");
                    rank++;
                }
            }

            // ── Top juegos por rating ────────────────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Top 3 Juegos por Rating}\\par");
            Map<String, Double> topRating = dao.topJuegosPorRating();
            if (topRating.isEmpty()) {
                writer.println("\\pard\\sb80\\sa80{\\f0\\fs22\\cf3 Sin datos disponibles}\\par");
            } else {
                int rank = 1;
                for (Map.Entry<String, Double> e : topRating.entrySet()) {
                    writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                            rank + ". " + escapar(e.getKey()) + "  \u2014  " + String.format("%.2f", e.getValue()) + "}\\par");
                    rank++;
                }
            }

            // ── Distribucion por dificultad ──────────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Distribucion por Dificultad}\\par");
            Map<String, Integer> difMap = dao.distribucionPorDificultad();
            for (Map.Entry<String, Integer> e : difMap.entrySet()) {
                writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                        escapar(e.getKey()) + ": " + e.getValue() + " juego(s)}\\par");
            }
            if (difMap.isEmpty()) {
                writer.println("\\pard\\sb80\\sa80{\\f0\\fs22\\cf3 Sin datos disponibles}\\par");
            }

            // ── Distribucion por modo de juego ───────────────────────
            writer.println("\\pard\\sb300\\sa100{\\f0\\fs28\\b\\cf2 Distribucion por Modo de Juego}\\par");
            Map<String, Integer> modoMap = dao.distribucionPorModo();
            for (Map.Entry<String, Integer> e : modoMap.entrySet()) {
                writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                        escapar(e.getKey()) + ": " + e.getValue() + " juego(s)}\\par");
            }
            if (modoMap.isEmpty()) {
                writer.println("\\pard\\sb80\\sa80{\\f0\\fs22\\cf3 Sin datos disponibles}\\par");
            }

            // ── Pie ──────────────────────────────────────────────────
            writer.println("\\pard\\sb400\\sa200{\\f0\\fs16\\cf3 _______________________________________________________________}\\par");
            writer.println("\\pard\\sb100\\sa100\\qc{\\f0\\fs20\\cf3 Generado automaticamente por el Sistema de Gestion de Videojuegos — POO 2026}\\par");

            writer.println("}");

            System.out.println("Resumen exportado correctamente en: " + rutaSalida);
            return rutaSalida;

        } catch (IOException e) {
            System.err.println("Error al exportar resumen: " + e.getMessage());
            return null;
        }
    }

    // Escribe una fila de estadistica con etiqueta y valor
    private void escribirFila(PrintWriter writer, String etiqueta, String valor) {
        writer.println("\\pard\\sb80\\sa80\\li360{\\f0\\fs22\\cf1 " +
                "\\b " + escapar(etiqueta) + ":\\b0  " + escapar(valor) + "}\\par");
    }

    // Escapa caracteres especiales para RTF
    private String escapar(String texto) {
        if (texto == null) return "";
        return texto
                .replace("\\", "\\\\")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("\n", " ");
    }
}
