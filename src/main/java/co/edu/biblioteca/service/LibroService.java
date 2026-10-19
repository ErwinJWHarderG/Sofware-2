
package co.edu.biblioteca.service;

import co.edu.biblioteca.enums.EstadoEjemplar;
import co.edu.biblioteca.enums.TipoEjemplar;
import co.edu.biblioteca.model.Ejemplar;
import co.edu.biblioteca.model.Libro;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LibroService {

    private final BibliotecaService bibliotecaService;

    public LibroService(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    public List<Libro> listarLibros() {
        return bibliotecaService.getBiblioteca().getLibros();
    }

    public List<Libro> buscarLibros(
            String titulo, String autor, String isbn, String categoria) {

        String t = normalizar(titulo);
        String a = normalizar(autor);
        String i = normalizar(isbn);
        String c = normalizar(categoria);

        return listarLibros().stream()
                .filter(l -> coincide(l.getTitulo(), t))
                .filter(l -> coincide(l.getAutor(), a))
                .filter(l -> coincide(l.getIsbn(), i))
                .filter(l -> coincide(l.getCategoria(), c))
                .collect(Collectors.toList());
    }

    private String normalizar(String valor) {
        return valor == null
                ? ""
                : valor.trim().toLowerCase(Locale.ROOT);
    }

    private boolean coincide(String campo, String filtro) {
        return filtro.isEmpty()
                || (campo != null
                && campo.toLowerCase(Locale.ROOT).contains(filtro));
    }

    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }

        for (Libro libro : listarLibros()) {
            if (libro.getIsbn() != null
                    && libro.getIsbn().equalsIgnoreCase(isbn.trim())) {
                return libro;
            }
        }

        return null;
    }

    public void guardarLibro(Libro libro) {
        Libro existente = buscarPorIsbn(libro.getIsbn());

        if (existente == null) {
            libro.agregarEjemplar(new Ejemplar(
                    generarSiguienteCodigoEjemplar(),
                    EstadoEjemplar.DISPONIBLE,
                    "Sin asignar",
                    TipoEjemplar.FISICO
            ));

            listarLibros().add(libro);
        } else {
            existente.setTitulo(libro.getTitulo());
            existente.setAutor(libro.getAutor());
            existente.setEditorial(libro.getEditorial());
            existente.setAnioPublicacion(libro.getAnioPublicacion());
            existente.setCategoria(libro.getCategoria());
        }
    }

    /**
     * Agrega varios ejemplares físicos a un libro existente.
     * Los nuevos ejemplares quedan disponibles y tienen códigos únicos.
     */
    public String agregarEjemplares(String isbn, int cantidad) {
        Libro libro = buscarPorIsbn(isbn);

        if (libro == null) {
            return "No se encontró el libro seleccionado.";
        }

        if (cantidad < 1 || cantidad > 500) {
            return "La cantidad debe estar entre 1 y 500 ejemplares.";
        }

        for (int i = 0; i < cantidad; i++) {
            Ejemplar ejemplar = new Ejemplar(
                    generarSiguienteCodigoEjemplar(),
                    EstadoEjemplar.DISPONIBLE,
                    "Sin asignar",
                    TipoEjemplar.FISICO
            );

            libro.agregarEjemplar(ejemplar);
        }

        return "Se agregaron " + cantidad
                + " ejemplares al libro \"" + libro.getTitulo() + "\".";
    }

    /**
     * Busca el siguiente código EJ numérico que no esté utilizado
     * por ningún libro de la biblioteca.
     */
    private String generarSiguienteCodigoEjemplar() {
        Set<String> codigosExistentes = new HashSet<>();

        for (Libro libro : listarLibros()) {
            for (Ejemplar ejemplar : libro.getEjemplares()) {
                if (ejemplar.getCodigoEjemplar() != null) {
                    codigosExistentes.add(
                            ejemplar.getCodigoEjemplar().trim()
                    );
                }
            }
        }

        int numero = 1;

        while (codigosExistentes.contains(
                String.format(Locale.ROOT, "EJ-%03d", numero))) {
            numero++;
        }

        return String.format(Locale.ROOT, "EJ-%03d", numero);
    }

    public boolean eliminarLibro(String isbn) {
        Libro libro = buscarPorIsbn(isbn);

        if (libro == null) {
            return false;
        }

        for (Ejemplar ejemplar : libro.getEjemplares()) {
            if (ejemplar.getEstado() == EstadoEjemplar.PRESTADO) {
                return false;
            }
        }

        listarLibros().remove(libro);
        return true;
    }
}
