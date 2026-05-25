package gestionvideojuegos.ui;

import gestionvideojuegos.dao.*;
import gestionvideojuegos.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Menu {

    static Scanner sc = new Scanner(System.in);
    static GeneroDAO generoDAO = new GeneroDAO();
    static PlataformaDAO plataformaDAO = new PlataformaDAO();
    static DesarrolladoraDAO desarrolladoraDAO = new DesarrolladoraDAO();
    static VideojuegoDAO videojuegoDAO = new VideojuegoDAO();
    static DlcDAO dlcDAO = new DlcDAO();
    static PremioDAO premioDAO = new PremioDAO();
    static VideojuegoPlataformaDAO videojuegoPlataformaDAO = new VideojuegoPlataformaDAO();

    public static void iniciar() {
        boolean corriendo = true;
        System.out.println("Bienvenido al sistema de gestion de videojuegos");

        while (corriendo) {
            System.out.println("\nQue desea hacer?");
            System.out.println("1. Generos");
            System.out.println("2. Plataformas");
            System.out.println("3. Desarrolladoras");
            System.out.println("4. Videojuegos");
            System.out.println("5. DLCs");
            System.out.println("6. Premios");
            System.out.println("7. Videojuego - Plataforma");
            System.out.println("0. Salir");
            System.out.print(">> ");

            int opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> menuGeneros();
                case 2 -> menuPlataformas();
                case 3 -> menuDesarrolladoras();
                case 4 -> menuVideojuegos();
                case 5 -> menuDlcs();
                case 6 -> menuPremios();
                case 7 -> menuVideojuegoPlataforma();
                case 0 -> {
                    corriendo = false;
                    System.out.println("Hasta luego!");
                }
                default -> System.out.println("Esa opcion no existe, intente de nuevo");
            }
        }
        sc.close();
    }

    static void menuGeneros() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nGeneros:");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Genero> lista = generoDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay generos todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Genero g = generoDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(g != null ? g : "No se encontro ese genero");
                }
                case 3 -> {
                    System.out.print("Nombre a buscar: ");
                    List<Genero> lista = generoDAO.buscarPorNombre(sc.nextLine());
                    if (lista.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        lista.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Descripcion: ");
                    String desc = sc.nextLine();
                    generoDAO.insertar(new Genero(0, nombre, desc));
                }
                case 5 -> {
                    System.out.print("ID del genero a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nueva descripcion: ");
                    String desc = sc.nextLine();
                    generoDAO.actualizar(new Genero(id, nombre, desc));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    generoDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuPlataformas() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nPlataformas:");
            System.out.println("1. Ver todas");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Plataforma> lista = plataformaDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay plataformas todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Plataforma p = plataformaDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(p != null ? p : "No encontrada");
                }
                case 3 -> {
                    System.out.print("Nombre a buscar: ");
                    List<Plataforma> lista = plataformaDAO.buscarPorNombre(sc.nextLine());
                    if (lista.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        lista.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Fabricante: ");
                    String fab = sc.nextLine();
                    System.out.print("Año de lanzamiento: ");
                    int anio = sc.nextInt(); sc.nextLine();
                    plataformaDAO.insertar(new Plataforma(0, nombre, fab, anio));
                }
                case 5 -> {
                    System.out.print("ID a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nuevo fabricante: ");
                    String fab = sc.nextLine();
                    System.out.print("Nuevo año: ");
                    int anio = sc.nextInt(); sc.nextLine();
                    plataformaDAO.actualizar(new Plataforma(id, nombre, fab, anio));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    plataformaDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuDesarrolladoras() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nDesarrolladoras:");
            System.out.println("1. Ver todas");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Desarrolladora> lista = desarrolladoraDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay desarrolladoras todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Desarrolladora d = desarrolladoraDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(d != null ? d : "No encontrada");
                }
                case 3 -> {
                    System.out.print("Nombre a buscar: ");
                    List<Desarrolladora> lista = desarrolladoraDAO.buscarPorNombre(sc.nextLine());
                    if (lista.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        lista.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Pais de origen: ");
                    String pais = sc.nextLine();
                    System.out.print("Año de fundacion: ");
                    int anio = sc.nextInt(); sc.nextLine();
                    desarrolladoraDAO.insertar(new Desarrolladora(0, nombre, pais, anio));
                }
                case 5 -> {
                    System.out.print("ID a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nuevo pais: ");
                    String pais = sc.nextLine();
                    System.out.print("Nuevo año: ");
                    int anio = sc.nextInt(); sc.nextLine();
                    desarrolladoraDAO.actualizar(new Desarrolladora(id, nombre, pais, anio));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    desarrolladoraDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuVideojuegos() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nVideojuegos:");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Filtrar");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Videojuego> lista = videojuegoDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay videojuegos todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Videojuego v = videojuegoDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(v != null ? v : "No encontrado");
                }
                case 3 -> {
                    System.out.println("Deje en blanco o ingrese 0 para ignorar un filtro");
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("ID Genero: ");
                    int idGenero = sc.nextInt(); sc.nextLine();
                    System.out.print("ID Desarrolladora: ");
                    int idDes = sc.nextInt(); sc.nextLine();
                    System.out.print("Precio minimo: ");
                    double precioMin = sc.nextDouble(); sc.nextLine();
                    System.out.print("Precio maximo: ");
                    double precioMax = sc.nextDouble(); sc.nextLine();
                    System.out.print("Rating minimo: ");
                    double rating = sc.nextDouble(); sc.nextLine();
                    System.out.print("Dificultad: ");
                    String dificultad = sc.nextLine();
                    System.out.print("Clasificacion edad: ");
                    String clasificacion = sc.nextLine();
                    System.out.print("Modo de juego: ");
                    String modo = sc.nextLine();

                    List<Videojuego> resultado = videojuegoDAO.filtrar(
                            nombre.isEmpty() ? null : nombre,
                            idGenero == 0 ? null : idGenero,
                            idDes == 0 ? null : idDes,
                            null,
                            precioMin == 0 ? null : precioMin,
                            precioMax == 0 ? null : precioMax,
                            rating == 0 ? null : rating,
                            dificultad.isEmpty() ? null : dificultad,
                            clasificacion.isEmpty() ? null : clasificacion,
                            modo.isEmpty() ? null : modo,
                            null
                    );

                    if (resultado.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        resultado.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("ID Genero: ");
                    int idGenero = sc.nextInt(); sc.nextLine();
                    System.out.print("ID Desarrolladora: ");
                    int idDes = sc.nextInt(); sc.nextLine();
                    System.out.print("Fecha lanzamiento (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble(); sc.nextLine();
                    System.out.print("Rating: ");
                    double rating = sc.nextDouble(); sc.nextLine();
                    System.out.print("Dificultad: ");
                    String dificultad = sc.nextLine();
                    System.out.print("Clasificacion edad: ");
                    String clasificacion = sc.nextLine();
                    System.out.print("Modo de juego: ");
                    String modo = sc.nextLine();
                    System.out.print("Descripcion: ");
                    String desc = sc.nextLine();

                    Genero genero = generoDAO.buscarPorId(idGenero);
                    Desarrolladora des = desarrolladoraDAO.buscarPorId(idDes);

                    if (genero == null || des == null)
                        System.out.println("Genero o desarrolladora no encontrados, verifique los IDs");
                    else
                        videojuegoDAO.insertar(new Videojuego(0, nombre, genero, des,
                                fecha, precio, rating, dificultad, clasificacion, modo, desc));
                }
                case 5 -> {
                    System.out.print("ID a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("ID Genero: ");
                    int idGenero = sc.nextInt(); sc.nextLine();
                    System.out.print("ID Desarrolladora: ");
                    int idDes = sc.nextInt(); sc.nextLine();
                    System.out.print("Fecha lanzamiento (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble(); sc.nextLine();
                    System.out.print("Rating: ");
                    double rating = sc.nextDouble(); sc.nextLine();
                    System.out.print("Dificultad: ");
                    String dificultad = sc.nextLine();
                    System.out.print("Clasificacion edad: ");
                    String clasificacion = sc.nextLine();
                    System.out.print("Modo de juego: ");
                    String modo = sc.nextLine();
                    System.out.print("Descripcion: ");
                    String desc = sc.nextLine();

                    Genero genero = generoDAO.buscarPorId(idGenero);
                    Desarrolladora des = desarrolladoraDAO.buscarPorId(idDes);

                    if (genero == null || des == null)
                        System.out.println("Genero o desarrolladora no encontrados");
                    else
                        videojuegoDAO.actualizar(new Videojuego(id, nombre, genero, des,
                                fecha, precio, rating, dificultad, clasificacion, modo, desc));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    videojuegoDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuDlcs() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nDLCs:");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Filtrar por rango de precio");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Dlc> lista = dlcDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay DLCs todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Dlc d = dlcDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(d != null ? d : "No encontrado");
                }
                case 3 -> {
                    System.out.print("Precio minimo: ");
                    double min = sc.nextDouble(); sc.nextLine();
                    System.out.print("Precio maximo: ");
                    double max = sc.nextDouble(); sc.nextLine();
                    List<Dlc> lista = dlcDAO.buscarPorPrecioRango(min, max);
                    if (lista.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        lista.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Precio: ");
                    double precio = sc.nextDouble(); sc.nextLine();
                    System.out.print("Fecha lanzamiento (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    System.out.print("Descripcion: ");
                    String desc = sc.nextLine();

                    Videojuego vj = videojuegoDAO.buscarPorId(idVj);
                    if (vj == null)
                        System.out.println("Videojuego no encontrado");
                    else
                        dlcDAO.insertar(new Dlc(0, vj, nombre, precio, fecha, desc));
                }
                case 5 -> {
                    System.out.print("ID del DLC a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nuevo precio: ");
                    double precio = sc.nextDouble(); sc.nextLine();
                    System.out.print("Nueva fecha (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());
                    System.out.print("Nueva descripcion: ");
                    String desc = sc.nextLine();

                    Videojuego vj = videojuegoDAO.buscarPorId(idVj);
                    if (vj == null)
                        System.out.println("Videojuego no encontrado");
                    else
                        dlcDAO.actualizar(new Dlc(id, vj, nombre, precio, fecha, desc));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    dlcDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuPremios() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nPremios:");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar premios por videojuego");
            System.out.println("4. Agregar");
            System.out.println("5. Editar");
            System.out.println("6. Eliminar");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<Premio> lista = premioDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay premios todavia");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID: ");
                    Premio p = premioDAO.buscarPorId(sc.nextInt());
                    sc.nextLine();
                    System.out.println(p != null ? p : "No encontrado");
                }
                case 3 -> {
                    System.out.print("Nombre del videojuego: ");
                    String nombre = sc.nextLine();
                    List<Premio> lista = premioDAO.buscarPorNombreDeVideojuego(nombre);
                    if (lista.isEmpty())
                        System.out.println("Sin resultados");
                    else
                        lista.forEach(System.out::println);
                }
                case 4 -> {
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("Nombre del premio: ");
                    String nombre = sc.nextLine();
                    System.out.print("Organizacion: ");
                    String org = sc.nextLine();
                    System.out.print("Año: ");
                    short anio = sc.nextShort(); sc.nextLine();
                    System.out.print("Categoria: ");
                    String cat = sc.nextLine();

                    Videojuego vj = videojuegoDAO.buscarPorId(idVj);
                    if (vj == null)
                        System.out.println("Videojuego no encontrado");
                    else
                        premioDAO.insertar(new Premio(0, vj, nombre, org, anio, cat));
                }
                case 5 -> {
                    System.out.print("ID del premio a editar: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("Nuevo nombre: ");
                    String nombre = sc.nextLine();
                    System.out.print("Nueva organizacion: ");
                    String org = sc.nextLine();
                    System.out.print("Nuevo año: ");
                    short anio = sc.nextShort(); sc.nextLine();
                    System.out.print("Nueva categoria: ");
                    String cat = sc.nextLine();

                    Videojuego vj = videojuegoDAO.buscarPorId(idVj);
                    if (vj == null)
                        System.out.println("Videojuego no encontrado");
                    else
                        premioDAO.actualizar(new Premio(id, vj, nombre, org, anio, cat));
                }
                case 6 -> {
                    System.out.print("ID a eliminar: ");
                    premioDAO.eliminar(sc.nextInt());
                    sc.nextLine();
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }


    static void menuVideojuegoPlataforma() {
        boolean corriendo = true;
        while (corriendo) {
            System.out.println("\nVideojuego - Plataforma:");
            System.out.println("1. Ver todas las relaciones");
            System.out.println("2. Agregar relacion");
            System.out.println("3. Eliminar relacion");
            System.out.println("0. Volver");
            System.out.print(">> ");

            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    List<VideojuegoPlataforma> lista = videojuegoPlataformaDAO.buscarTodos();
                    if (lista.isEmpty())
                        System.out.println("No hay relaciones registradas");
                    else
                        lista.forEach(System.out::println);
                }
                case 2 -> {
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("ID de la plataforma: ");
                    int idP = sc.nextInt(); sc.nextLine();
                    System.out.print("Fecha lanzamiento en esta plataforma (YYYY-MM-DD): ");
                    LocalDate fecha = LocalDate.parse(sc.nextLine());

                    Videojuego vj = videojuegoDAO.buscarPorId(idVj);
                    Plataforma p = plataformaDAO.buscarPorId(idP);

                    if (vj == null || p == null)
                        System.out.println("Videojuego o plataforma no encontrados");
                    else
                        videojuegoPlataformaDAO.insertar(new VideojuegoPlataforma(vj, p, fecha));
                }
                case 3 -> {
                    System.out.print("ID del videojuego: ");
                    int idVj = sc.nextInt(); sc.nextLine();
                    System.out.print("ID de la plataforma: ");
                    int idP = sc.nextInt(); sc.nextLine();
                    videojuegoPlataformaDAO.eliminar(idVj, idP);
                }
                case 0 -> corriendo = false;
                default -> System.out.println("Opcion invalida");
            }
        }
    }
}