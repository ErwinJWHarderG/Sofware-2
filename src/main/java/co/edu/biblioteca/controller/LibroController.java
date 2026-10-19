
package co.edu.biblioteca.controller;

import co.edu.biblioteca.model.Libro;
import co.edu.biblioteca.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public String listarLibros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String categoria,
            Model model) {

        model.addAttribute(
                "libros",
                libroService.buscarLibros(titulo, autor, isbn, categoria)
        );

        model.addAttribute("tituloFiltro", titulo == null ? "" : titulo);
        model.addAttribute("autorFiltro", autor == null ? "" : autor);
        model.addAttribute("isbnFiltro", isbn == null ? "" : isbn);
        model.addAttribute("categoriaFiltro", categoria == null ? "" : categoria);

        return "libros";
    }

    @GetMapping("/nuevo")
    public String nuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("modo", "crear");
        return "formulario-libro";
    }

    @GetMapping("/editar/{isbn}")
    public String editarLibro(
            @PathVariable String isbn,
            Model model,
            RedirectAttributes redirectAttributes) {

        Libro libro = libroService.buscarPorIsbn(isbn);

        if (libro == null) {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "No se encontró el libro seleccionado."
            );
            return "redirect:/libros";
        }

        model.addAttribute("libro", libro);
        model.addAttribute("modo", "editar");

        return "formulario-libro";
    }

    @PostMapping("/guardar")
    public String guardarLibro(
            @ModelAttribute Libro libro,
            RedirectAttributes redirectAttributes) {

        libroService.guardarLibro(libro);

        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "Libro guardado correctamente."
        );

        return "redirect:/libros";
    }

    @GetMapping("/ejemplares/{isbn}")
    public String gestionarEjemplares(
            @PathVariable String isbn,
            Model model,
            RedirectAttributes redirectAttributes) {

        Libro libro = libroService.buscarPorIsbn(isbn);

        if (libro == null) {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "No se encontró el libro seleccionado."
            );
            return "redirect:/libros";
        }

        model.addAttribute("libro", libro);

        return "gestionar-ejemplares";
    }

    @PostMapping("/ejemplares/{isbn}/agregar")
    public String agregarEjemplares(
            @PathVariable String isbn,
            @RequestParam int cantidad,
            RedirectAttributes redirectAttributes) {

        String resultado = libroService.agregarEjemplares(isbn, cantidad);

        if (resultado.startsWith("Se agregaron ")) {
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    resultado
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    resultado
            );
        }

        return "redirect:/libros/ejemplares/" + isbn;
    }

    @PostMapping("/eliminar/{isbn}")
    public String eliminarLibro(
            @PathVariable String isbn,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = libroService.eliminarLibro(isbn);

        if (eliminado) {
            redirectAttributes.addFlashAttribute(
                    "mensajeExito",
                    "Libro eliminado correctamente."
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "mensajeError",
                    "No se puede eliminar el libro porque no existe o tiene ejemplares prestados."
            );
        }

        return "redirect:/libros";
    }
}
