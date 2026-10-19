package co.edu.biblioteca.controller;

import co.edu.biblioteca.enums.EstadoMulta;
import co.edu.biblioteca.enums.EstadoUsuario;
import co.edu.biblioteca.model.Bibliotecario;
import co.edu.biblioteca.model.Multa;
import co.edu.biblioteca.model.Usuario;
import co.edu.biblioteca.service.BibliotecaService;
import co.edu.biblioteca.service.PrestamoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class InicioController {

    private final BibliotecaService bibliotecaService;
    private final PrestamoService prestamoService;

    public InicioController(BibliotecaService bibliotecaService, PrestamoService prestamoService) {
        this.bibliotecaService = bibliotecaService;
        this.prestamoService = prestamoService;
    }

    @GetMapping("/")
    public String mostrarAcceso(Model model, HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if ("BIBLIOTECARIO".equals(rol)) {
            return "redirect:/bibliotecario";
        } else if ("USUARIO".equals(rol)) {
            Usuario user = (Usuario) session.getAttribute("usuarioLogueado");
            return "redirect:/usuario/" + user.getIdentificacion();
        }
        model.addAttribute("biblioteca", bibliotecaService.getBiblioteca());
        return "index";
    }

    @PostMapping("/login")
    public String ingresarConId(@RequestParam String identificacion,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (identificacion == null || identificacion.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Por favor ingrese un ID válido.");
            return "redirect:/";
        }

        String idTrim = identificacion.trim();

        // 1. Verificar si es Bibliotecario
        Bibliotecario bibliotecario = prestamoService.buscarBibliotecario(idTrim);
        if (bibliotecario != null) {
            session.setAttribute("usuarioLogueado", bibliotecario);
            session.setAttribute("rol", "BIBLIOTECARIO");
            return "redirect:/bibliotecario";
        }

        // 2. Verificar si es Usuario (Estudiante / Docente)
        Usuario usuario = prestamoService.buscarUsuario(idTrim);
        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("rol", "USUARIO");
            return "redirect:/usuario/" + usuario.getIdentificacion();
        }

        // 3. ID no encontrado
        redirectAttributes.addFlashAttribute("mensajeError",
                "Identificación no registrada. Ejemplos válidos: 2001 (Bibliotecario) o 1001 (Usuario).");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/usuario/login")
    public String redireccionarLoginAntiguo() {
        return "redirect:/";
    }

    // RUTAS DE BIBLIOTECARIO PROTEGIDAS POR SESIÓN

    @GetMapping("/bibliotecario")
    public String panelBibliotecario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso denegado. Debe iniciar sesión como Bibliotecario.");
            return "redirect:/";
        }
        model.addAttribute("biblioteca", bibliotecaService.getBiblioteca());
        model.addAttribute("bibliotecario", session.getAttribute("usuarioLogueado"));
        return "bibliotecario";
    }

    @GetMapping("/bibliotecario/usuarios")
    public String listarUsuariosBibliotecario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso denegado.");
            return "redirect:/";
        }
        model.addAttribute("usuarios", bibliotecaService.getBiblioteca().getUsuarios());
        return "bibliotecario-usuarios";
    }

    @GetMapping("/bibliotecario/multas")
    public String listarMultasPendientesBibliotecario(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso denegado.");
            return "redirect:/";
        }
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
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) return "redirect:/";

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
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) return "redirect:/";

        Usuario usuario = prestamoService.buscarUsuario(identificacionUsuario);
        if (usuario == null || valor == null || valor.signum() <= 0 || motivo.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "Verifica el usuario, el valor positivo y el motivo.");
        } else {
            int id = bibliotecaService.getBiblioteca().getMultas().stream().mapToInt(Multa::getIdMulta).max().orElse(0) + 1;
            bibliotecaService.getBiblioteca().getMultas().add(new Multa(id, valor, LocalDate.now(), EstadoMulta.PENDIENTE, motivo.trim(), usuario));
            redirectAttributes.addFlashAttribute("mensajeExito", "Multa registrada correctamente.");
        }
        return "redirect:/bibliotecario/multas";
    }

    @PostMapping("/bibliotecario/multas/{id}/actualizar")
    public String actualizarMulta(@PathVariable int id,
                                  @RequestParam BigDecimal valor,
                                  @RequestParam String motivo,
                                  @RequestParam EstadoMulta estado,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        if (!"BIBLIOTECARIO".equals(session.getAttribute("rol"))) return "redirect:/";

        Multa encontrada = null;
        for (Multa multa : bibliotecaService.getBiblioteca().getMultas()) {
            if (multa.getIdMulta() == id) {
                encontrada = multa;
                break;
            }
        }
        if (encontrada == null || valor == null || valor.signum() <= 0 || motivo.isBlank()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No se pudo actualizar la multa.");
        } else {
            encontrada.setValor(valor);
            encontrada.setMotivo(motivo.trim());
            encontrada.setEstado(estado);
            redirectAttributes.addFlashAttribute("mensajeExito", "Multa actualizada correctamente.");
        }
        return "redirect:/bibliotecario/multas";
    }
}