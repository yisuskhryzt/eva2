package eva2.eva2.controllers;

import eva2.eva2.models.Cliente;
import eva2.eva2.models.Mesa;
import eva2.eva2.models.Reserva;
import eva2.eva2.services.ClienteService;
import eva2.eva2.services.MesaService;
import eva2.eva2.services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MesaService mesaService;

    // ========== FUNCIONES PARA ADMINISTRADOR ==========

    // Listar todas las reservas (solo ADMIN)
    @GetMapping
    public String listarReservas(Model model) {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        model.addAttribute("reservas", reservas);
        return "reservas-lista";
    }

    // Mostrar formulario para nueva reserva (solo ADMIN)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("mesas", mesaService.obtenerMesasDisponibles());
        return "reserva-formulario";
    }

    // Guardar nueva reserva (solo ADMIN)
    @PostMapping("/guardar")
    public String guardarReserva(
            @RequestParam Long clienteId,
            @RequestParam Long mesaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Obtener cliente y mesa
            Cliente cliente = clienteService.obtenerClientePorId(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            
            Mesa mesa = mesaService.obtenerMesaPorId(mesaId)
                    .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
            
            // Crear y guardar reserva
            Reserva reserva = new Reserva();
            reserva.setCliente(cliente);
            reserva.setMesa(mesa);
            reserva.setFechaHora(fechaHora);
            
            reservaService.guardarReserva(reserva);
            
            redirectAttributes.addFlashAttribute("mensaje", "Reserva guardada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/reservas";
    }

    // Mostrar formulario para editar reserva (solo ADMIN)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Reserva reserva = reservaService.obtenerReservaPorId(id).orElse(null);
        
        if (reserva == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Reserva no encontrada");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/reservas";
        }
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("mesas", mesaService.obtenerTodasLasMesas());
        return "reserva-formulario";
    }

    // Actualizar reserva (solo ADMIN)
    @PostMapping("/actualizar/{id}")
    public String actualizarReserva(
            @PathVariable Long id,
            @RequestParam Long clienteId,
            @RequestParam Long mesaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        
        try {
            reservaService.actualizarReserva(id, clienteId, mesaId, fechaHora);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva actualizada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/reservas/editar/" + id;
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/reservas/editar/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al actualizar la reserva: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/reservas/editar/" + id;
        }
        
        return "redirect:/reservas";
    }

    // Eliminar reserva (solo ADMIN)
    @GetMapping("/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservaService.cancelarReserva(id);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva eliminada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar la reserva: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/reservas";
    }

    // ========== FUNCIONES PARA CLIENTE ==========

    // Ver reservas propias del cliente (CLIENTE)
    @GetMapping("/mis-reservas/{clienteId}")
    public String verMisReservas(@PathVariable Long clienteId, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId).orElse(null);
        
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/";
        }
        
        List<Reserva> reservas = reservaService.obtenerReservasPorCliente(clienteId);
        model.addAttribute("reservas", reservas);
        model.addAttribute("cliente", cliente);
        return "mis-reservas";
    }

    // Formulario para nueva reserva del cliente (CLIENTE)
    @GetMapping("/nueva/{clienteId}")
    public String nuevaReservaCliente(@PathVariable Long clienteId, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId).orElse(null);
        
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/";
        }
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("mesas", mesaService.obtenerMesasDisponibles());
        model.addAttribute("reserva", new Reserva());
        return "reserva-cliente-formulario";
    }

    // Guardar reserva del cliente (CLIENTE)
    @PostMapping("/guardar-cliente/{clienteId}")
    public String guardarReservaCliente(
            @PathVariable Long clienteId,
            @RequestParam Long mesaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHora,
            RedirectAttributes redirectAttributes) {
        
        try {
            reservaService.crearReserva(clienteId, mesaId, fechaHora);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva creada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/reservas/mis-reservas/" + clienteId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/reservas/nueva/" + clienteId;
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/reservas/nueva/" + clienteId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al crear la reserva: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/reservas/nueva/" + clienteId;
        }
    }

    // Cancelar reserva propia (CLIENTE)
    @GetMapping("/cancelar/{id}/{clienteId}")
    public String cancelarReservaCliente(
            @PathVariable Long id, 
            @PathVariable Long clienteId, 
            RedirectAttributes redirectAttributes) {
        
        try {
            reservaService.cancelarReservaPorCliente(id, clienteId);
            redirectAttributes.addFlashAttribute("mensaje", "Reserva cancelada exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al cancelar la reserva: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        
        return "redirect:/reservas/mis-reservas/" + clienteId;
    }
}
