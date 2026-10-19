package co.edu.biblioteca.controller;

import co.edu.biblioteca.model.Usuario;
import co.edu.biblioteca.service.LibroService;
import co.edu.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final PrestamoService prestamoService;
    private final LibroService libroService;

    public UsuarioController(PrestamoService prestamoService, LibroService libroService) {
        this.prestamoService = prestamoService;
        this.libroService = libroService;
    }

    @GetMapping("/usuario/{identificacion}")
    public String panelUsuario(@PathVariable String identificacion,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        Usuario usuario = prestamoService.buscarUsuario(identificacion);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado.");
            return "redirect:/usuario/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("tieneMultasPendientes", prestamoService.usuarioTieneMultasPendientes(usuario));
        model.addAttribute("cantidadPrestamosActivos", prestamoService.contarPrestamosActivos(usuario));
        model.addAttribute("prestamosUsuario", prestamoService.listarPrestamosPorUsuario(usuario));

        return "usuario-inicio";
    }

    @GetMapping("/usuario/{identificacion}/catalogo")
    public String catalogoUsuario(@PathVariable String identificacion,
                                  @RequestParam(required = false) String titulo,
                                  @RequestParam(required = false) String autor,
                                  @RequestParam(required = false) String isbn,
                                  @RequestParam(required = false) String categoria,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = prestamoService.buscarUsuario(identificacion);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado.");
            return "redirect:/usuario/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("libros", libroService.buscarLibros(titulo, autor, isbn, categoria));
        model.addAttribute("tituloFiltro", titulo == null ? "" : titulo);
        model.addAttribute("autorFiltro", autor == null ? "" : autor);
        model.addAttribute("isbnFiltro", isbn == null ? "" : isbn);
        model.addAttribute("categoriaFiltro", categoria == null ? "" : categoria);
        model.addAttribute("tieneMultasPendientes", prestamoService.usuarioTieneMultasPendientes(usuario));

        return "usuario-catalogo";
    }

    @PostMapping("/usuario/{identificacion}/solicitar/{isbn}")
    public String solicitarPrestamo(@PathVariable String identificacion,
                                    @PathVariable String isbn,
                                    RedirectAttributes redirectAttributes) {

        String mensaje = prestamoService.solicitarPrestamoComoUsuario(identificacion, isbn);

        if (mensaje.startsWith("Préstamo solicitado correctamente")) {
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", mensaje);
        }

        return "redirect:/usuario/" + identificacion + "/catalogo";
    }

    @GetMapping("/usuario/{identificacion}/prestamos")
    public String prestamosUsuario(@PathVariable String identificacion,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        Usuario usuario = prestamoService.buscarUsuario(identificacion);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Usuario no encontrado.");
            return "redirect:/usuario/login";
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("prestamosUsuario", prestamoService.listarPrestamosPorUsuario(usuario));
        model.addAttribute("tieneMultasPendientes", prestamoService.usuarioTieneMultasPendientes(usuario));

        return "usuario-prestamos";
    }
}