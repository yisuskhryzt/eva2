package eva2.eva2.controllers;

import eva2.eva2.models.Cliente;
import eva2.eva2.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

//los controladores manejan las solicitudes HTTP entrantes y devuelven respuestas HTTP
//en este caso, el controlador gestiona las operaciones CRUD para la entidad Cliente
//se relacionan con las vistas (HTML) para mostrar los datos al usuario y con el servicio para procesar la logica de negocio


@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // Listar todos los clientes (solo ADMIN)
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        model.addAttribute("clientes", clientes);
        return "clientes-lista";
    }
    
    // Mostrar formulario para nuevo cliente (solo ADMIN)
    @GetMapping("/nuevo")  
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-formulario";
    }

    // Guardar nuevo cliente (solo ADMIN)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            clienteService.guardarCliente(cliente);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/clientes/nuevo";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al guardar el cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/clientes/nuevo";
        }
        return "redirect:/clientes";
    }

    // Mostrar formulario para editar cliente (solo ADMIN)
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(id).orElse(null);
        
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/clientes";
        }
        
        model.addAttribute("cliente", cliente);
        return "cliente-formulario";
    }

    // Eliminar cliente (solo ADMIN)
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminarCliente(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", "Error al eliminar el cliente: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        }
        return "redirect:/clientes";
    }
}

