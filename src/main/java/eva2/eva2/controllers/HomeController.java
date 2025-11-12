package eva2.eva2.controllers;

import eva2.eva2.models.Cliente;
import eva2.eva2.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private ClienteService clienteService;

    // Página principal
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Procesar selección de rol
    @PostMapping("/seleccionar-rol")
    public String seleccionarRol(
            @RequestParam String rol,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String rut,
            RedirectAttributes redirectAttributes) {

        if ("admin".equals(rol)) {
            // Validar contraseña de admin
            if ("Admin123.-".equals(password)) {
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "Contraseña incorrecta");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/";
            }
        } else if ("cliente".equals(rol)) {
            // Buscar o crear cliente por RUT
            if (rut == null || rut.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("mensaje", "Debe ingresar su RUT");
                redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
                return "redirect:/";
            }

            // Normalizar RUT: eliminar puntos y guiones, luego formatear a formato estándar
            String rutNormalizado = normalizarRut(rut);
            
            if (rutNormalizado == null) {
                redirectAttributes.addFlashAttribute("mensaje", "RUT inválido. Debe tener al menos 8 caracteres.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
                return "redirect:/";
            }

            // Buscar cliente existente por RUT
            Cliente cliente = clienteService.buscarPorRut(rutNormalizado).orElse(null);

            if (cliente == null) {
                // Cliente no existe - redirigir a formulario de registro
                redirectAttributes.addFlashAttribute("mensaje", "RUT no registrado. Por favor complete el formulario de registro.");
                redirectAttributes.addFlashAttribute("tipoMensaje", "info");
                redirectAttributes.addFlashAttribute("rutIngresado", rutNormalizado);
                return "redirect:/clientes/registro-cliente";
            }

            return "redirect:/cliente/dashboard/" + cliente.getId();
        }

        redirectAttributes.addFlashAttribute("mensaje", "Rol no válido");
        redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
        return "redirect:/";
    }

    // Dashboard de Administrador
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalClientes", clienteService.contarClientes());
        return "admin-dashboard";
    }

    // Dashboard de Cliente
    @GetMapping("/cliente/dashboard/{clienteId}")
    public String clienteDashboard(@PathVariable Long clienteId, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId).orElse(null);

        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
            return "redirect:/";
        }

        model.addAttribute("cliente", cliente);
        return "cliente-dashboard";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    // Formulario de registro para nuevos clientes
    @GetMapping("/clientes/registro-cliente")
    public String registroCliente(Model model) {
        // Crear un nuevo cliente vacío para el formulario
        Cliente cliente = new Cliente();
        
        // Si hay un RUT ingresado previamente, pre-llenarlo
        if (model.containsAttribute("rutIngresado")) {
            cliente.setRut((String) model.getAttribute("rutIngresado"));
        }
        
        model.addAttribute("cliente", cliente);
        model.addAttribute("esRegistroNuevo", true);
        return "cliente-registro";
    }

    // Guardar nuevo cliente desde registro público
    @PostMapping("/clientes/guardar-registro")
    public String guardarRegistro(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        try {
            // Guardar el cliente
            clienteService.guardarCliente(cliente);
            
            // Mensaje de éxito y redirigir a login
            redirectAttributes.addFlashAttribute("mensaje", "¡Registro exitoso! Ahora puedes ingresar con tu RUT.");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            redirectAttributes.addFlashAttribute("cliente", cliente);
            return "redirect:/clientes/registro-cliente";
        }
    }

    // Formulario para editar datos del cliente
    @GetMapping("/cliente/editar/{clienteId}")
    public String editarCliente(@PathVariable Long clienteId, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteService.obtenerClientePorId(clienteId).orElse(null);

        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/";
        }

        model.addAttribute("cliente", cliente);
        return "cliente-editar";
    }

    // Guardar cambios del cliente
    @PostMapping("/cliente/actualizar/{clienteId}")
    public String actualizarCliente(@PathVariable Long clienteId, @ModelAttribute Cliente clienteActualizado, RedirectAttributes redirectAttributes) {
        try {
            clienteService.actualizarCliente(clienteId, clienteActualizado);
            
            redirectAttributes.addFlashAttribute("mensaje", "¡Datos actualizados correctamente!");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            
            return "redirect:/cliente/dashboard/" + clienteId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "danger");
            return "redirect:/cliente/editar/" + clienteId;
        }
    }

    /**
     * Normaliza un RUT chileno eliminando puntos y guiones,
     * y luego formateándolo al formato estándar: xxxxxxxx-x
     * Acepta formatos: xx.xxx.xxx-x, xxxxxxxx-x, xxxxxxxxx
     * 
     * @param rut RUT en cualquier formato
     * @return RUT normalizado en formato xxxxxxxx-x o null si es inválido
     */
    private String normalizarRut(String rut) {
        if (rut == null) {
            return null;
        }
        
        // Eliminar todos los puntos y guiones
        String rutLimpio = rut.replaceAll("[.\\-]", "").trim();
        
        // Verificar que tenga al menos 8 caracteres (7 dígitos + 1 dígito verificador o K)
        if (rutLimpio.length() < 8) {
            return null;
        }
        
        // Separar el dígito verificador (último carácter)
        String numero = rutLimpio.substring(0, rutLimpio.length() - 1);
        String digitoVerificador = rutLimpio.substring(rutLimpio.length() - 1).toUpperCase();
        
        // Formatear al formato estándar: xxxxxxxx-x
        return numero + "-" + digitoVerificador;
    }
}
