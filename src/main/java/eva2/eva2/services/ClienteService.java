package eva2.eva2.services;

import eva2.eva2.models.Cliente;
import eva2.eva2.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//lo servicios permiten la logica de negocio entre el controlador y el repositorio
//por esto se entiende que es el intermediario entre ambos
//aqui se realizan las validaciones necesarias antes de enviar los datos al repositorio
//se relacionan con el repositorio para acceder a los datos y con el controlador para procesar las solicitudes


@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Obtener todos los clientes
     */
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    /**
     * Obtener cliente por ID
     */
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    /**
     * Guardar o actualizar cliente
     */
    public Cliente guardarCliente(Cliente cliente) {
        // Validar que el RUT no esté vacío
        if (cliente.getRut() == null || cliente.getRut().trim().isEmpty()) {
            throw new IllegalArgumentException("El RUT del cliente es obligatorio");
        }

        // Validar que el nombre no esté vacío
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        // Validar que el email no esté vacío
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email del cliente es obligatorio");
        }

        // Validar formato de email básico
        if (!cliente.getEmail().contains("@")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }

        // Validar que el RUT no exista ya (si es nuevo o si cambió el RUT)
        if (cliente.getId() == null) {
            // Cliente nuevo
            if (rutExiste(cliente.getRut())) {
                throw new IllegalArgumentException("Ya existe un cliente con el RUT: " + cliente.getRut());
            }
        } else {
            // Cliente existente - verificar si cambió el RUT
            if (rutExisteExcluyendoCliente(cliente.getRut(), cliente.getId())) {
                throw new IllegalArgumentException("Ya existe otro cliente con el RUT: " + cliente.getRut());
            }
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Actualizar cliente existente
     */
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Actualizar campos
        clienteExistente.setNombre(clienteActualizado.getNombre());
        clienteExistente.setEmail(clienteActualizado.getEmail());
        
        if (clienteActualizado.getTelefono() != null) {
            clienteExistente.setTelefono(clienteActualizado.getTelefono());
        }

        return guardarCliente(clienteExistente);
    }

    /**
     * Eliminar cliente por ID
     */
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Verificar si tiene reservas activas
        if (cliente.getReservas() != null && !cliente.getReservas().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el cliente porque tiene reservas asociadas. Elimine primero las reservas.");
        }

        clienteRepository.deleteById(id);
    }

    /**
     * Verificar si un cliente existe
     */
    public boolean existeCliente(Long id) {
        return clienteRepository.existsById(id);
    }

    /**
     * Contar total de clientes
     */
    public long contarClientes() {
        return clienteRepository.count();
    }

    /**
     * Buscar cliente por RUT
     */
    public Optional<Cliente> buscarPorRut(String rut) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getRut() != null && c.getRut().equalsIgnoreCase(rut))
                .findFirst();
    }

    /**
     * Buscar cliente por email
     */
    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findAll().stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    /**
     * Validar si un email ya existe (útil para evitar duplicados)
     */
    public boolean emailExiste(String email) {
        return clienteRepository.findAll().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Validar si un email ya existe excluyendo un cliente específico (para edición)
     */
    public boolean emailExisteExcluyendoCliente(String email, Long clienteId) {
        return clienteRepository.findAll().stream()
                .filter(c -> !c.getId().equals(clienteId))
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Validar si un RUT ya existe
     */
    public boolean rutExiste(String rut) {
        return clienteRepository.findAll().stream()
                .anyMatch(c -> c.getRut() != null && c.getRut().equalsIgnoreCase(rut));
    }

    /**
     * Validar si un RUT ya existe excluyendo un cliente específico (para edición)
     */
    public boolean rutExisteExcluyendoCliente(String rut, Long clienteId) {
        return clienteRepository.findAll().stream()
                .filter(c -> !c.getId().equals(clienteId))
                .anyMatch(c -> c.getRut() != null && c.getRut().equalsIgnoreCase(rut));
    }
}
