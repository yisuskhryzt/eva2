package eva2.eva2.controllers;

import eva2.eva2.models.Mesa;
import eva2.eva2.services.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    // Listar todas las mesas (solo ADMIN)
    @GetMapping
    public String listarMesas(Model model) {
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        model.addAttribute("mesas", mesas);
        return "mesas-lista";
    }

    // Mostrar formulario para nueva mesa (solo ADMIN)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("mesa", new Mesa());
        return "mesa-formulario";
    }

    // Guardar nueva mesa (solo ADMIN)
    @PostMapping("/guardar")
    public String guardarMesa(@ModelAttribute Mesa mesa, RedirectAttributes redirectAttributes) {
        try {
            mesaService.guardarMesa(mesa);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa guardada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/mesas/nuevo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar la mesa: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/mesas/nuevo";
        }
        return "redirect:/mesas";
    }

    // Mostrar formulario para editar mesa (solo ADMIN)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Mesa mesa = mesaService.obtenerMesaPorId(id).orElse(null);
        
        if (mesa == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Mesa no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/mesas";
        }
        
        model.addAttribute("mesa", mesa);
        return "mesa-formulario";
    }

    // Cambiar disponibilidad de mesa (solo ADMIN)
    @GetMapping("/cambiar-disponibilidad/{id}")
    public String cambiarDisponibilidad(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Mesa mesa = mesaService.cambiarDisponibilidad(id);
            String estado = mesa.isDisponible() ? "disponible" : "no disponible";
            redirectAttributes.addFlashAttribute("mensaje", "Mesa marcada como " + estado);
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cambiar disponibilidad: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/mesas";
    }

    // Eliminar mesa (solo ADMIN)
    @GetMapping("/eliminar/{id}")
    public String eliminarMesa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mesaService.eliminarMesa(id);
            redirectAttributes.addFlashAttribute("mensaje", "Mesa eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la mesa: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/mesas";
    }

    // Ver mesas disponibles (ADMIN y CLIENTE)
    @GetMapping("/disponibles")
    public String listarMesasDisponibles(Model model) {
        List<Mesa> mesasDisponibles = mesaService.obtenerMesasDisponibles();
        model.addAttribute("mesas", mesasDisponibles);
        return "mesas-disponibles";
    }
}
