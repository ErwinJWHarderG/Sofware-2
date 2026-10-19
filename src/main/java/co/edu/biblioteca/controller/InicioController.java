package co.edu.biblioteca.controller;

import co.edu.biblioteca.model.Usuario;
import co.edu.biblioteca.model.Multa;
import co.edu.biblioteca.enums.EstadoMulta;
import co.edu.biblioteca.enums.EstadoUsuario;
import java.math.BigDecimal;
import java.time.LocalDate;
import co.edu.biblioteca.service.BibliotecaService;
import co.edu.biblioteca.service.PrestamoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InicioController {

    private final BibliotecaService bibliotecaService;
    private final PrestamoService prestamoService;

    public InicioController(BibliotecaService bibliotecaService, PrestamoService prestamoService) {
        this.bibliotecaService = bibliotecaService;
        this.prestamoService = prestamoService;
    }

    @GetMapping("/")
    public String mostrarAcceso(Model model) {
        model.addAttribute("biblioteca", bibliotecaService.getBiblioteca());
        return "index";
    }

    @GetMapping("/bibliotecario")
    public String panelBibliotecario(Model model) {
        model.addAttribute("biblioteca", bibliotecaService.getBiblioteca());
        return "bibliotecario";
    }
    @GetMapping("/bibliotecario/usuarios")
    public String listarUsuariosBibliotecario(Model model) {
        model.addAttribute("usuarios", bibliotecaService.getBiblioteca().getUsuarios());
        return "bibliotecario-usuarios";
    }

    @GetMapping("/bibliotecario/multas")
    public String listarMultasPendientesBibliotecario(Model model) {
        model.addAttribute("usuarios", bibliotecaService.getBiblioteca().getUsuarios());
        model.addAttribute("multas", bibliotecaService.getBiblioteca().getMultas());
        return "bibliotecario-multas";
    }

    @PostMapping("/bibliotecario/usuarios/actualizar")
    public String actualizarUsuario(@RequestParam String identificacion,
                                    @RequestParam String nombre,
                                    @RequestParam String apellido,
                                    @RequestParam String correo,
                                    @RequestParam EstadoUsuario estado,
                                    RedirectAttributes redirectAttributes) {
        Usuario usuario = prestamoService.buscarUsuario(identificacion);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se encontró el usuario.");
        } else if (nombre.isBlank() || apellido.isBlank() || correo.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Nombre, apellido y correo son obligatorios.");
        } else {
            usuario.setNombre(nombre.trim());
            usuario.setApellido(apellido.trim());
            usuario.setCorreo(correo.trim());
            usuario.setEstado(estado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Información del usuario actualizada correctamente.");
        }
        return "redirect:/bibliotecario/usuarios";
    }

    @PostMapping("/bibliotecario/multas/registrar")
    public String registrarMulta(@RequestParam String identificacionUsuario,
                                 @RequestParam BigDecimal valor,
                                 @RequestParam String motivo,
                                 RedirectAttributes redirectAttributes) {
        Usuario usuario = prestamoService.buscarUsuario(identificacionUsuario);
        if (usuario == null || valor == null || valor.signum() <= 0 || motivo.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Verifica el usuario, el valor positivo y el motivo de la multa.");
        } else {
            int id = bibliotecaService.getBiblioteca().getMultas().stream().mapToInt(Multa::getIdMulta).max().orElse(0) + 1;
            bibliotecaService.getBiblioteca().getMultas().add(new Multa(id, valor, LocalDate.now(), EstadoMulta.PENDIENTE, motivo.trim(), usuario));
            redirectAttributes.addFlashAttribute("mensajeExito", "Multa registrada correctamente para " + usuario.getNombreCompleto() + ".");
        }
        return "redirect:/bibliotecario/multas";
    }

    @PostMapping("/bibliotecario/multas/{id}/actualizar")
    public String actualizarMulta(@PathVariable int id,
                                  @RequestParam BigDecimal valor,
                                  @RequestParam String motivo,
                                  @RequestParam EstadoMulta estado,
                                  RedirectAttributes redirectAttributes) {
        Multa encontrada = null;
        for (Multa multa : bibliotecaService.getBiblioteca().getMultas()) if (multa.getIdMulta() == id) { encontrada = multa; break; }
        if (encontrada == null || valor == null || valor.signum() <= 0 || motivo.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo actualizar la multa. Revisa los datos.");
        } else {
            encontrada.setValor(valor);
            encontrada.setMotivo(motivo.trim());
            encontrada.setEstado(estado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Multa actualizada correctamente.");
        }
        return "redirect:/bibliotecario/multas";
    }

    @GetMapping("/usuario/login")
    public String mostrarLoginUsuario() {
        return "login-usuario";
    }

    @PostMapping("/usuario/login")
    public String ingresarUsuario(@RequestParam String identificacion,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = prestamoService.buscarUsuario(identificacion);

        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError",
                    "No se encontró un usuario registrado con la identificación " + identificacion + ".");
            return "redirect:/usuario/login";
        }

        return "redirect:/usuario/" + usuario.getIdentificacion();
    }
}