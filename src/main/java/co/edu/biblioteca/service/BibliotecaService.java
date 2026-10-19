package co.edu.biblioteca.service;

import co.edu.biblioteca.enums.*;
import co.edu.biblioteca.model.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BibliotecaService {

    private final Biblioteca biblioteca;

    public BibliotecaService() {
        biblioteca = new Biblioteca(1, "Biblioteca Universitaria", "Universidad del Quindío", "7359300", "Lunes a viernes 8:00 a.m. - 6:00 p.m.");
        cargarDatosDePrueba();
    }

    public Biblioteca getBiblioteca() {
        return biblioteca;
    }

    private void cargarDatosDePrueba() {
        Usuario usuario1 = new Usuario("1001", "Juan", "Pérez", "juan@uniquindio.edu.co", TipoUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO);
        Usuario usuario2 = new Usuario("1002", "Ana", "Gómez", "ana@uniquindio.edu.co", TipoUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO);
        Usuario usuario3 = new Usuario("1003", "Carlos", "Ruiz", "carlos@uniquindio.edu.co", TipoUsuario.DOCENTE, EstadoUsuario.ACTIVO);
        Usuario usuario4 = new Usuario("1004", "Laura", "Martínez", "laura@uniquindio.edu.co", TipoUsuario.ESTUDIANTE, EstadoUsuario.INACTIVO);
        Usuario usuario5 = new Usuario("1005", "Mateo", "López", "mateo@uniquindio.edu.co", TipoUsuario.ESTUDIANTE, EstadoUsuario.ACTIVO);

        biblioteca.getUsuarios().add(usuario1);
        biblioteca.getUsuarios().add(usuario2);
        biblioteca.getUsuarios().add(usuario3);
        biblioteca.getUsuarios().add(usuario4);
        biblioteca.getUsuarios().add(usuario5);

        biblioteca.getBibliotecarios().add(new Bibliotecario("2001", "Marta", "López", "Bibliotecaria", Turno.MANANA));
        biblioteca.getBibliotecarios().add(new Bibliotecario("2002", "Pedro", "Ramírez", "Auxiliar de biblioteca", Turno.TARDE));

        Libro libro1 = new Libro("978-001", "Ingeniería de Software", "Ian Sommerville", "Pearson", 2011, "Software");
        libro1.agregarEjemplar(new Ejemplar("EJ-001", EstadoEjemplar.DISPONIBLE, "Estante A1", TipoEjemplar.FISICO));
        libro1.agregarEjemplar(new Ejemplar("EJ-002", EstadoEjemplar.DISPONIBLE, "Estante A1", TipoEjemplar.FISICO));

        Libro libro2 = new Libro("978-002", "Bases de Datos", "Abraham Silberschatz", "McGraw Hill", 2019, "Bases de Datos");
        libro2.agregarEjemplar(new Ejemplar("EJ-003", EstadoEjemplar.DISPONIBLE, "Estante B2", TipoEjemplar.FISICO));
        libro2.agregarEjemplar(new Ejemplar("EJ-004", EstadoEjemplar.PRESTADO, "Estante B2", TipoEjemplar.FISICO));

        Libro libro3 = new Libro("978-003", "Programación en Java", "Deitel", "Pearson", 2020, "Programación");
        libro3.agregarEjemplar(new Ejemplar("EJ-005", EstadoEjemplar.DISPONIBLE, "Estante C3", TipoEjemplar.FISICO));

        Libro libro4 = new Libro("978-004", "Sistemas Operativos", "Andrew Tanenbaum", "Pearson", 2015, "Sistemas");
        libro4.agregarEjemplar(new Ejemplar("EJ-006", EstadoEjemplar.DANADO, "Estante D1", TipoEjemplar.FISICO));

        Libro libro5 = new Libro("978-005", "Algoritmos", "Thomas Cormen", "MIT Press", 2022, "Algoritmos");
        libro5.agregarEjemplar(new Ejemplar("EJ-007", EstadoEjemplar.DISPONIBLE, "Estante E1", TipoEjemplar.FISICO));

        biblioteca.getLibros().add(libro1);
        biblioteca.getLibros().add(libro2);
        biblioteca.getLibros().add(libro3);
        biblioteca.getLibros().add(libro4);
        biblioteca.getLibros().add(libro5);

        biblioteca.getPrestamos().add(new Prestamo(1, LocalDate.now().minusDays(3), LocalDate.now().plusDays(5),
                EstadoPrestamo.ACTIVO, "Préstamo inicial de prueba", usuario2, biblioteca.getBibliotecarios().get(0), libro2, libro2.getEjemplares().get(1)));

        biblioteca.getMultas().add(new Multa(1, new BigDecimal("5000"), LocalDate.now().minusDays(2),
                EstadoMulta.PENDIENTE, "Retraso en devolución", usuario5));
        biblioteca.getMultas().add(new Multa(2, new BigDecimal("3000"), LocalDate.now().minusDays(10),
                EstadoMulta.PAGADA, "Retraso anterior", usuario1));
    }
}
