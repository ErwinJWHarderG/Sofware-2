package co.edu.biblioteca.service;

import co.edu.biblioteca.dto.PrestamoForm;
import co.edu.biblioteca.enums.EstadoPrestamo;
import co.edu.biblioteca.enums.EstadoUsuario;
import co.edu.biblioteca.enums.EstadoEjemplar;
import co.edu.biblioteca.model.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private static final int MAXIMO_PRESTAMOS_ACTIVOS = 3;
    private static final int DIAS_PRESTAMO = 14;

    private final BibliotecaService bibliotecaService;
    private final LibroService libroService;

    public PrestamoService(BibliotecaService bibliotecaService, LibroService libroService) {
        this.bibliotecaService = bibliotecaService;
        this.libroService = libroService;
    }

    public List<Prestamo> listarPrestamos() {
        return bibliotecaService.getBiblioteca().getPrestamos();
    }

    public String registrarPrestamo(PrestamoForm form) {
        Usuario usuario = buscarUsuario(form.getIdentificacionUsuario());
        Bibliotecario bibliotecario = buscarBibliotecario(form.getIdentificacionBibliotecario());
        Libro libro = libroService.buscarPorIsbn(form.getIsbnLibro());

        if (usuario == null) {
            return "No se encontró el usuario seleccionado.";
        }

        if (bibliotecario == null) {
            return "No se encontró el bibliotecario seleccionado.";
        }

        if (libro == null) {
            return "No se encontró el libro seleccionado.";
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            return "No se puede registrar el préstamo porque el usuario está inactivo.";
        }

        if (usuarioTieneMultasPendientes(usuario)) {
            return "No se puede registrar el préstamo porque el usuario tiene multas pendientes.";
        }

        if (contarPrestamosActivos(usuario) >= MAXIMO_PRESTAMOS_ACTIVOS) {
            return "No se puede registrar el préstamo porque el usuario alcanzó el máximo de préstamos activos.";
        }

        Ejemplar ejemplarDisponible = libro.buscarPrimerEjemplarDisponible();
        if (ejemplarDisponible == null) {
            return "No se puede registrar el préstamo porque no hay ejemplares disponibles del libro seleccionado.";
        }

        int nuevoId = listarPrestamos().size() + 1;
        Prestamo prestamo = new Prestamo(
                nuevoId,
                LocalDate.now(),
                LocalDate.now().plusDays(DIAS_PRESTAMO),
                EstadoPrestamo.ACTIVO,
                form.getObservaciones(),
                usuario,
                bibliotecario,
                libro,
                ejemplarDisponible
        );

        ejemplarDisponible.marcarPrestado();
        listarPrestamos().add(prestamo);

        return "Préstamo registrado correctamente. Usuario: " + usuario.getNombreCompleto()
                + ". Ejemplar: " + ejemplarDisponible.getCodigoEjemplar()
                + ". Fecha: " + prestamo.getFechaPrestamo()
                + ". Fecha de devolución: " + prestamo.getFechaDevolucion()
                + ". Estado: ACTIVO.";
    }


    public String registrarDevolucion(int idPrestamo) {
        Prestamo prestamo = null;
        for (Prestamo p : listarPrestamos()) {
            if (p.getIdPrestamo() == idPrestamo) { prestamo = p; break; }
        }
        if (prestamo == null) return "No se encontró el préstamo seleccionado.";
        if (!prestamo.estaActivo()) return "Este préstamo ya fue devuelto o no está activo.";
        prestamo.setEstado(EstadoPrestamo.DEVUELTO);
        if (prestamo.getEjemplar() != null) prestamo.getEjemplar().marcarDevuelto();
        return "Devolución registrada correctamente. Usuario: " + prestamo.getUsuario().getNombreCompleto()
                + ". Ejemplar: " + prestamo.getEjemplar().getCodigoEjemplar()
                + ". Fecha: " + LocalDate.now() + ". Estado: DEVUELTO.";
    }

    public Prestamo buscarPrestamo(int idPrestamo) {
        for (Prestamo p : listarPrestamos()) if (p.getIdPrestamo() == idPrestamo) return p;
        return null;
    }

    public Usuario buscarUsuario(String identificacion) {
        for (Usuario usuario : bibliotecaService.getBiblioteca().getUsuarios()) {
            if (usuario.getIdentificacion().equals(identificacion)) {
                return usuario;
            }
        }
        return null;
    }

    public Bibliotecario buscarBibliotecario(String identificacion) {
        for (Bibliotecario bibliotecario : bibliotecaService.getBiblioteca().getBibliotecarios()) {
            if (bibliotecario.getIdentificacion().equals(identificacion)) {
                return bibliotecario;
            }
        }
        return null;
    }

    public int contarPrestamosActivos(Usuario usuario) {
        int contador = 0;
        for (Prestamo prestamo : listarPrestamos()) {
            if (prestamo.getUsuario().getIdentificacion().equals(usuario.getIdentificacion()) && prestamo.estaActivo()) {
                contador++;
            }
        }
        return contador;
    }

    public boolean usuarioTieneMultasPendientes(Usuario usuario) {
        for (Multa multa : bibliotecaService.getBiblioteca().getMultas()) {
            if (multa.getUsuario().getIdentificacion().equals(usuario.getIdentificacion()) && multa.estaPendiente()) {
                return true;
            }
        }
        return false;
    }
    public List<Prestamo> listarPrestamosPorUsuario(Usuario usuario) {
        List<Prestamo> prestamosUsuario = new ArrayList<>();

        for (Prestamo prestamo : listarPrestamos()) {
            if (prestamo.getUsuario().getIdentificacion().equals(usuario.getIdentificacion())) {
                prestamosUsuario.add(prestamo);
            }
        }

        return prestamosUsuario;
    }

    public String solicitarPrestamoComoUsuario(String identificacionUsuario, String isbnLibro) {
        Usuario usuario = buscarUsuario(identificacionUsuario);
        Libro libro = libroService.buscarPorIsbn(isbnLibro);

        if (usuario == null) {
            return "No se encontró el usuario que intenta solicitar el préstamo.";
        }

        if (libro == null) {
            return "No se encontró el libro solicitado.";
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            return "No puedes solicitar préstamos porque tu usuario está inactivo.";
        }

        if (usuarioTieneMultasPendientes(usuario)) {
            return "No puedes solicitar este préstamo porque tienes multas pendientes.";
        }

        if (contarPrestamosActivos(usuario) >= MAXIMO_PRESTAMOS_ACTIVOS) {
            return "No puedes solicitar este préstamo porque alcanzaste el máximo de préstamos activos.";
        }

        Ejemplar ejemplarDisponible = libro.buscarPrimerEjemplarDisponible();

        if (ejemplarDisponible == null) {
            return "No puedes solicitar este libro porque no hay ejemplares disponibles.";
        }

        Bibliotecario bibliotecarioAsignado = obtenerBibliotecarioPorDefecto();

        if (bibliotecarioAsignado == null) {
            return "No se puede registrar la solicitud porque no hay bibliotecarios registrados en el sistema.";
        }

        int nuevoId = listarPrestamos().size() + 1;

        Prestamo prestamo = new Prestamo(
                nuevoId,
                LocalDate.now(),
                LocalDate.now().plusDays(DIAS_PRESTAMO),
                EstadoPrestamo.ACTIVO,
                "Solicitud realizada por el usuario desde el catálogo web.",
                usuario,
                bibliotecarioAsignado,
                libro,
                ejemplarDisponible
        );

        ejemplarDisponible.marcarPrestado();
        listarPrestamos().add(prestamo);

        return "Préstamo solicitado correctamente. Usuario: " + usuario.getNombreCompleto()
                + ". Ejemplar: " + ejemplarDisponible.getCodigoEjemplar()
                + ". Fecha: " + prestamo.getFechaPrestamo()
                + ". Fecha de devolución: " + prestamo.getFechaDevolucion()
                + ". Estado: ACTIVO.";
    }

    private Bibliotecario obtenerBibliotecarioPorDefecto() {
        if (bibliotecaService.getBiblioteca().getBibliotecarios().isEmpty()) {
            return null;
        }

        return bibliotecaService.getBiblioteca().getBibliotecarios().get(0);
    }
}
