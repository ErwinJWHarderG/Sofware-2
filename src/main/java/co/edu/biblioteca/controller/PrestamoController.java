package co.edu.biblioteca.controller;

import co.edu.biblioteca.dto.PrestamoForm;
import co.edu.biblioteca.service.BibliotecaService;
import co.edu.biblioteca.service.LibroService;
import co.edu.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;
    private final BibliotecaService bibliotecaService;
    private final LibroService libroService;

    public PrestamoController(PrestamoService prestamoService, BibliotecaService bibliotecaService, LibroService libroService) {
        this.prestamoService = prestamoService;
        this.bibliotecaService = bibliotecaService;
        this.libroService = libroService;
    }

    @GetMapping
    public String mostrarFormulario(Model model) {
        model.addAttribute("prestamoForm", new PrestamoForm());
        model.addAttribute("usuarios", bibliotecaService.getBiblioteca().getUsuarios());
        model.addAttribute("bibliotecarios", bibliotecaService.getBiblioteca().getBibliotecarios());
        model.addAttribute("libros", libroService.listarLibros());
        model.addAttribute("prestamos", prestamoService.listarPrestamos());
        return "prestamos";
    }

    @PostMapping("/{id}/devolver")
    public String registrarDevolucion(@PathVariable int id, RedirectAttributes redirectAttributes) {
        String mensaje = prestamoService.registrarDevolucion(id);
        if (mensaje.startsWith("Devolución registrada correctamente")) redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        else redirectAttributes.addFlashAttribute("mensajeError", mensaje);
        return "redirect:/prestamos";
    }

    @PostMapping("/registrar")
    public String registrarPrestamo(@ModelAttribute PrestamoForm prestamoForm, RedirectAttributes redirectAttributes) {
        String mensaje = prestamoService.registrarPrestamo(prestamoForm);

        if (mensaje.startsWith("Préstamo registrado correctamente")) {
            redirectAttributes.addFlashAttribute("mensajeExito", mensaje);
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", mensaje);
        }

        return "redirect:/prestamos";
    }
}
