# Biblioteca Universitaria Web

Proyecto MVP para la entrega final de Ingeniería de Software I.

## Descripción

Este sistema representa una biblioteca universitaria cuyo proceso principal es el registro del préstamo de un libro.

El proyecto permite:

- Registrar préstamos de libros.
- Validar usuarios activos.
- Validar multas pendientes.
- Validar disponibilidad de ejemplares.
- Controlar un máximo de préstamos activos por usuario.
- Gestionar libros mediante un CRUD básico: listar, crear, actualizar y eliminar.

## Tecnología utilizada

- Java 17
- Maven
- Spring Boot 3.3.5
- Thymeleaf
- HTML
- CSS

## Cómo ejecutar el proyecto

1. Abrir la carpeta del proyecto en IntelliJ IDEA.
2. Esperar a que Maven descargue las dependencias.
3. Ejecutar la clase principal:

```text
src/main/java/co/edu/biblioteca/BibliotecaApplication.java
```

4. Abrir el navegador en:

```text
http://localhost:8080
```

## Pantallas funcionales

### Pantalla 1: Registrar préstamo

Ruta:

```text
http://localhost:8080/prestamos
```

Corresponde a la transacción principal del negocio: registrar el préstamo de un libro.

Validaciones implementadas:

1. El usuario debe existir.
2. El bibliotecario debe existir.
3. El libro debe existir.
4. El usuario debe estar ACTIVO.
5. El usuario no debe tener multas PENDIENTES.
6. El usuario no debe superar 3 préstamos activos.
7. El libro debe tener al menos un ejemplar DISPONIBLE.
8. Al registrar el préstamo, el ejemplar cambia a estado PRESTADO.

### Pantalla 2: CRUD de libros

Ruta:

```text
http://localhost:8080/libros
```

Permite:

- Listar libros.
- Crear libros.
- Editar libros.
- Eliminar libros.

Nota: un libro no se puede eliminar si tiene un ejemplar prestado.

## Datos de prueba

El sistema carga datos en memoria al iniciar.

### Usuarios demo

| Identificación | Nombre | Tipo | Estado |
|---|---|---|---|
| 1001 | Juan Pérez | ESTUDIANTE | ACTIVO |
| 1002 | Ana Gómez | ESTUDIANTE | ACTIVO |
| 1003 | Carlos Ruiz | DOCENTE | ACTIVO |
| 1004 | Laura Martínez | ESTUDIANTE | INACTIVO |
| 1005 | Mateo López | ESTUDIANTE | ACTIVO con multa pendiente |

### Bibliotecarios demo

| Identificación | Nombre | Turno |
|---|---|---|
| 2001 | Marta López | MAÑANA |
| 2002 | Pedro Ramírez | TARDE |

### Libros demo

| ISBN | Título | Categoría |
|---|---|---|
| 978-001 | Ingeniería de Software | Software |
| 978-002 | Bases de Datos | Bases de Datos |
| 978-003 | Programación en Java | Programación |
| 978-004 | Sistemas Operativos | Sistemas |
| 978-005 | Algoritmos | Algoritmos |

## Flujo feliz para el video

1. Entrar a `http://localhost:8080`.
2. Ir a `Registrar préstamo`.
3. Seleccionar el usuario `1001 - Juan Pérez`.
4. Seleccionar un libro con ejemplares disponibles, por ejemplo `Ingeniería de Software`.
5. Seleccionar un bibliotecario.
6. Escribir una observación.
7. Presionar `Registrar préstamo`.
8. El sistema muestra mensaje de éxito y el préstamo aparece en la tabla.

## Flujo alterno bloqueante para el video

Opción 1: usuario con multa pendiente.

1. Ir a `Registrar préstamo`.
2. Seleccionar el usuario `1005 - Mateo López`.
3. Seleccionar cualquier libro disponible.
4. Seleccionar un bibliotecario.
5. Presionar `Registrar préstamo`.
6. El sistema bloquea la operación porque el usuario tiene multas pendientes.

Opción 2: usuario inactivo.

1. Seleccionar el usuario `1004 - Laura Martínez`.
2. Intentar registrar un préstamo.
3. El sistema bloquea la operación porque el usuario está inactivo.

## Trazabilidad básica desde modelos hasta código

| Modelo / requisito | Implementación en código |
|---|---|
| Usuario | `model/Usuario.java` |
| Bibliotecario | `model/Bibliotecario.java` |
| Libro | `model/Libro.java` |
| Ejemplar | `model/Ejemplar.java` |
| Préstamo | `model/Prestamo.java` |
| Multa | `model/Multa.java` |
| Biblioteca | `model/Biblioteca.java` |
| Registrar préstamo | `controller/PrestamoController.java` y `service/PrestamoService.java` |
| CRUD de libros | `controller/LibroController.java` y `service/LibroService.java` |
| Validar disponibilidad | `Libro.buscarPrimerEjemplarDisponible()` |
| Validar multas | `PrestamoService.usuarioTieneMultasPendientes()` |
| Validar máximo de préstamos | `PrestamoService.contarPrestamosActivos()` |
| Interfaz de préstamo | `templates/prestamos.html` |
| Interfaz CRUD | `templates/libros.html` y `templates/formulario-libro.html` |

## Requisitos no funcionales mínimos

- Rendimiento: el sistema responde rápido porque usa listas en memoria.
- Validaciones en servidor: las reglas principales se validan en `PrestamoService`.
- Validaciones en cliente: los campos obligatorios usan `required` en HTML.
- Accesibilidad básica: los formularios usan etiquetas `label` asociadas a los campos.
- Usabilidad: navegación sencilla entre Inicio, Registrar préstamo y Gestionar libros.

## Importante

Este MVP no usa base de datos. Los datos se guardan en memoria con listas de Java, por lo tanto se reinician cada vez que se detiene la aplicación.


## Funcionalidades incorporadas en esta versión

Se añadieron mejoras para los requisitos F-01, F-03, F-04, F-07 y F-08, conservando la lógica de préstamos existente (F-02):

- **F-01:** búsqueda del catálogo por título, autor, ISBN y categoría; mensaje cuando no hay coincidencias.
- **F-02:** se conserva el registro de préstamos y sus validaciones; las confirmaciones incluyen usuario, ejemplar, fecha y estado. El plazo configurado es de 14 días.
- **F-03:** edición de nombre, apellido, correo y estado de usuarios desde la lista del bibliotecario.
- **F-04:** visualización del estado de cada ejemplar (DISPONIBLE, PRESTADO o DANADO) y del total disponible.
- **F-07:** registro de multas y actualización de valor, motivo y estado (PENDIENTE/PAGADA).
- **F-08:** confirmación de préstamos y acción para registrar devoluciones, actualizar el estado del préstamo y liberar el ejemplar.

### Ejecución

Requisitos: Java 17 y Maven. Desde la carpeta `biblioteca-universitaria`, ejecutar `mvn spring-boot:run`. La aplicación usa datos de prueba en memoria; los cambios realizados se pierden al reiniciar.

**Nota de verificación:** en el entorno de preparación no estaba instalado Maven, así que no fue posible ejecutar `mvn test` ni comprobar el arranque de Spring Boot aquí. Se recomienda ejecutar el proyecto localmente y probar las operaciones anteriores antes de la entrega.
