# Gestion de Videojuegos — Segundo Parcial POO

Proyecto desarrollado para el segundo parcial de Programacion Orientada a Objetos. El objetivo es persistir informacion de un modelo en una base de datos remota, aplicando el patron DAO y buenas practicas de versionamiento.

El tema elegido es la gestion de videojuegos, que permite modelar categorias (generos), niveles de dificultad y multiples caracteristicas como plataformas, desarrolladoras, DLCs y premios.

---

## Que hace el proyecto

El sistema permite al usuario, a traves de un menu interactivo en consola:

- Agregar nuevos registros a la base de datos
- Consultar un registro especifico
- Consultar todos los registros de una tabla
- Filtrar registros por criterios como nombre, genero, dificultad, precio y mas

Todo esto conectado a una base de datos PostgreSQL alojada en Neon (servidor remoto).

---

## Estructura del proyecto

```
GestionVideojuegos/
├── .env                  # Variables de entorno — NO se sube a Git
├── .gitignore            # Excluye .env y archivos compilados
├── README.md             # Este archivo
├── docs/
│   └── diagrama.png      # Diagrama de clases del proyecto
├── pom.xml               # Dependencias Maven
└── src/
    └── main/
        └── java/
            └── gestionvideojuegos/
                ├── config/
                │   └── ConexionDB.java         # Maneja la conexion a Neon
                ├── model/
                │   ├── Genero.java
                │   ├── Plataforma.java
                │   ├── Desarrolladora.java
                │   ├── Videojuego.java
                │   ├── VideojuegoPlataforma.java
                │   ├── Dlc.java
                │   └── Premio.java
                ├── dao/
                │   ├── GeneroDAO.java
                │   ├── PlataformaDAO.java
                │   ├── DesarrolladoraDAO.java
                │   ├── VideojuegoDAO.java
                │   ├── VideojuegoPlataformaDAO.java
                │   ├── DlcDAO.java
                │   └── PremioDAO.java
                ├── ui/
                │   └── Menu.java               # Menu principal del sistema
                └── Main.java                   # Punto de entrada
```

### Que hace cada carpeta

- **config**: maneja la conexion a la base de datos. Un solo lugar para cambiar las credenciales si es necesario.
- **model**: una clase Java por cada tabla de la base de datos. Representa los objetos del mundo real con sus atributos, constructor, getters y setters.
- **dao**: una clase por cada modelo con los metodos CRUD (insertar, buscar, actualizar, eliminar). Aqui vive todo el SQL del proyecto.
- **ui**: el menu interactivo del sistema. Se comunica con los DAOs para mostrar y recibir informacion del usuario.
- **Main.java**: arranca la aplicacion.

---

## Base de datos

Se usa PostgreSQL a traves de Neon como servidor remoto. Las tablas del modelo son:

| Tabla | Descripcion |
|-------|-------------|
| genero | Categorias de videojuegos (RPG, Accion, Terror...) |
| plataforma | Consolas y plataformas (PS5, Xbox, PC...) |
| desarrolladora | Empresas que desarrollan los juegos |
| videojuego | Tabla principal con toda la informacion del juego |
| videojuego_plataforma | Relacion entre juegos y plataformas |
| dlc | Contenido descargable de cada juego |
| premio | Premios recibidos por cada juego |

---

## Como correr el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/tuUsuario/GestionVideojuegos.git
cd GestionVideojuegos
```

### 2. Configurar las variables de entorno

Crear un archivo `.env` en la raiz del proyecto con este contenido:

```
DB_URL=jdbc:postgresql://[host]/[nombre_bd]?sslmode=require
DB_USER=[usuario]
DB_PASSWORD=[password]
```

Las credenciales reales no se comparten en este repositorio por seguridad. Consultar al autor del proyecto para obtenerlas.

### 3. Instalar dependencias

```bash
mvn install
```

### 4. Correr el proyecto

Ejecutar `Main.java` desde IntelliJ IDEA o con:

```bash
mvn exec:java -Dexec.mainClass="gestionvideojuegos.Main"
```

---

## Dependencias

```xml
<!-- Driver PostgreSQL para la conexion a Neon -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.3</version>
</dependency>

<!-- Dotenv para leer variables de entorno desde el archivo .env -->
<dependency>
    <groupId>io.github.cdimascio</groupId>
    <artifactId>dotenv-java</artifactId>
    <version>3.0.0</version>
</dependency>
```

---

## Patron de diseno usado

Se aplica el patron **DAO (Data Access Object)** para separar la logica de acceso a datos del resto de la aplicacion. Esto permite que si cambia la base de datos, solo se modifica el DAO correspondiente sin tocar el menu ni los modelos.

```
Menu (UI)  →  DAO  →  ConexionDB  →  Neon (PostgreSQL)
```

---

## Diagrama de clases

![Diagrama de clases](docs/diagrama.png)

---

## Credenciales

Las credenciales de la base de datos NO estan en el codigo ni en el repositorio. Se manejan a traves de variables de entorno definidas en un archivo `.env` local que esta excluido del control de versiones mediante `.gitignore`.

---

## Autor

Felipe — Programacion Orientada a Objetos, 2026