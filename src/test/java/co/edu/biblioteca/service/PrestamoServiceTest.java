
package co.edu.biblioteca.service;

import co.edu.biblioteca.dto.PrestamoForm;
import co.edu.biblioteca.enums.EstadoEjemplar;
import co.edu.biblioteca.enums.EstadoPrestamo;
import co.edu.biblioteca.model.Biblioteca;
import co.edu.biblioteca.model.Bibliotecario;
import co.edu.biblioteca.model.Ejemplar;
import co.edu.biblioteca.model.Libro;
import co.edu.biblioteca.model.Prestamo;
import co.edu.biblioteca.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoServiceTest {

    private BibliotecaService bibliotecaService;
    private LibroService libroService;
    private PrestamoService prestamoService;
    private Biblioteca biblioteca;

    @BeforeEach
    void setUp() {
        bibliotecaService = new BibliotecaService();
        libroService = new LibroService(bibliotecaService);
        prestamoService = new PrestamoService(
                bibliotecaService,
                libroService
        );
        biblioteca = bibliotecaService.getBiblioteca();
    }

    @Test
    void registraPrestamoCuandoUsuarioYLibroCumplenLasReglas() {
        int prestamosIniciales = prestamoService.listarPrestamos().size();

        PrestamoForm form = formulario("1001", "2001", "978-001");

        String resultado = prestamoService.registrarPrestamo(form);

        assertTrue(resultado.startsWith(
                "Préstamo registrado correctamente."
        ));

        assertEquals(
                prestamosIniciales + 1,
                prestamoService.listarPrestamos().size()
        );

        Prestamo prestamo =
                prestamoService.listarPrestamos().get(prestamosIniciales);

        assertEquals(EstadoPrestamo.ACTIVO, prestamo.getEstado());
        assertEquals(
                EstadoEjemplar.PRESTADO,
                prestamo.getEjemplar().getEstado()
        );
        assertEquals(
                LocalDate.now().plusDays(14),
                prestamo.getFechaDevolucion()
        );
    }

    @Test
    void rechazaUsuarioInactivo() {
        String resultado = prestamoService.registrarPrestamo(
                formulario("1004", "2001", "978-001")
        );

        assertEquals(
                "No se puede registrar el préstamo porque el usuario está inactivo.",
                resultado
        );
    }

    @Test
    void rechazaUsuarioConMultaPendiente() {
        String resultado = prestamoService.registrarPrestamo(
                formulario("1005", "2001", "978-001")
        );

        assertEquals(
                "No se puede registrar el préstamo porque el usuario tiene multas pendientes.",
                resultado
        );
    }

    @Test
    void rechazaCuandoUsuarioAlcanzaElMaximoDePrestamosActivos() {
        Usuario usuario = prestamoService.buscarUsuario("1001");
        Bibliotecario bibliotecario =
                prestamoService.buscarBibliotecario("2001");
        Libro libro = libroService.buscarPorIsbn("978-001");
        Ejemplar ejemplar = libro.getEjemplares().get(0);

        for (int i = 0; i < 3; i++) {
            prestamoService.listarPrestamos().add(
                    new Prestamo(
                            20 + i,
                            LocalDate.now(),
                            LocalDate.now().plusDays(14),
                            EstadoPrestamo.ACTIVO,
                            "Prueba",
                            usuario,
                            bibliotecario,
                            libro,
                            ejemplar
                    )
            );
        }

        String resultado = prestamoService.registrarPrestamo(
                formulario("1001", "2001", "978-001")
        );

        assertEquals(
                "No se puede registrar el préstamo porque el usuario alcanzó el máximo de préstamos activos.",
                resultado
        );
    }

    @Test
    void rechazaLibroSinEjemplaresDisponibles() {
        String resultado = prestamoService.registrarPrestamo(
                formulario("1001", "2001", "978-004")
        );

        assertEquals(
                "No se puede registrar el préstamo porque no hay ejemplares disponibles del libro seleccionado.",
                resultado
        );
    }

    private PrestamoForm formulario(
            String identificacionUsuario,
            String identificacionBibliotecario,
            String isbn
    ) {
        PrestamoForm form = new PrestamoForm();
        form.setIdentificacionUsuario(identificacionUsuario);
        form.setIdentificacionBibliotecario(identificacionBibliotecario);
        form.setIsbnLibro(isbn);
        form.setObservaciones("Prueba automatizada");

        return form;
    }
}
