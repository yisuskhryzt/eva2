package eva2.eva2.services;

import eva2.eva2.models.Mesa;
import eva2.eva2.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Obtener todas las mesas
     */
    public List<Mesa> obtenerTodasLasMesas() {
        return mesaRepository.findAll();
    }

    /**
     * Obtener mesa por ID
     */
    public Optional<Mesa> obtenerMesaPorId(Long id) {
        return mesaRepository.findById(id);
    }

    /**
     * Obtener solo mesas disponibles
     */
    public List<Mesa> obtenerMesasDisponibles() {
        return mesaRepository.findByDisponible(true);
    }

    /**
     * Obtener mesas por capacidad mínima
     */
    public List<Mesa> obtenerMesasPorCapacidadMinima(int capacidad) {
        return mesaRepository.findByCapacidadGreaterThanEqual(capacidad);
    }

    /**
     * Obtener mesas disponibles con capacidad mínima
     */
    public List<Mesa> obtenerMesasDisponiblesPorCapacidad(int capacidad) {
        return mesaRepository.findByDisponible(true).stream()
                .filter(mesa -> mesa.getCapacidad() >= capacidad)
                .collect(Collectors.toList());
    }

    /**
     * Guardar o actualizar mesa
     */
    public Mesa guardarMesa(Mesa mesa) {
        // Validar número de mesa
        if (mesa.getNumero() <= 0) {
            throw new IllegalArgumentException("El número de mesa debe ser mayor a 0");
        }

        // Validar capacidad
        if (mesa.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }

        // Validar que el número de mesa no esté duplicado
        if (mesa.getId() == null && numeroMesaExiste(mesa.getNumero())) {
            throw new IllegalArgumentException("Ya existe una mesa con el número " + mesa.getNumero());
        }

        // Si es una mesa nueva, establecer como disponible por defecto
        if (mesa.getId() == null) {
            mesa.setDisponible(true);
        }

        return mesaRepository.save(mesa);
    }

    /**
     * Actualizar mesa existente
     */
    public Mesa actualizarMesa(Long id, Mesa mesaActualizada) {
        Mesa mesaExistente = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + id));

        // Validar si se está cambiando el número y si ya existe
        if (mesaExistente.getNumero() != mesaActualizada.getNumero()) {
            if (numeroMesaExisteExcluyendo(mesaActualizada.getNumero(), id)) {
                throw new IllegalArgumentException("Ya existe otra mesa con el número " + mesaActualizada.getNumero());
            }
        }

        // Actualizar campos
        mesaExistente.setNumero(mesaActualizada.getNumero());
        mesaExistente.setCapacidad(mesaActualizada.getCapacidad());
        mesaExistente.setDisponible(mesaActualizada.isDisponible());

        return guardarMesa(mesaExistente);
    }

    /**
     * Cambiar disponibilidad de una mesa
     */
    public Mesa cambiarDisponibilidad(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + id));

        mesa.setDisponible(!mesa.isDisponible());
        return mesaRepository.save(mesa);
    }

    /**
     * Marcar mesa como disponible
     */
    public Mesa marcarComoDisponible(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + id));

        mesa.setDisponible(true);
        return mesaRepository.save(mesa);
    }

    /**
     * Marcar mesa como no disponible
     */
    public Mesa marcarComoNoDisponible(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + id));

        mesa.setDisponible(false);
        return mesaRepository.save(mesa);
    }

    /**
     * Eliminar mesa por ID
     */
    public void eliminarMesa(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + id));

        // Verificar si tiene reservas
        if (mesa.getReservas() != null && !mesa.getReservas().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la mesa porque tiene reservas asociadas. Elimine primero las reservas.");
        }

        mesaRepository.deleteById(id);
    }

    /**
     * Verificar si una mesa existe
     */
    public boolean existeMesa(Long id) {
        return mesaRepository.existsById(id);
    }

    /**
     * Verificar si un número de mesa ya existe
     */
    public boolean numeroMesaExiste(int numero) {
        return mesaRepository.findAll().stream()
                .anyMatch(m -> m.getNumero() == numero);
    }

    /**
     * Verificar si un número de mesa existe excluyendo una mesa específica (para edición)
     */
    public boolean numeroMesaExisteExcluyendo(int numero, Long mesaId) {
        return mesaRepository.findAll().stream()
                .filter(m -> !m.getId().equals(mesaId))
                .anyMatch(m -> m.getNumero() == numero);
    }

    /**
     * Contar total de mesas
     */
    public long contarMesas() {
        return mesaRepository.count();
    }

    /**
     * Contar mesas disponibles
     */
    public long contarMesasDisponibles() {
        return mesaRepository.findByDisponible(true).size();
    }

    /**
     * Contar mesas no disponibles
     */
    public long contarMesasNoDisponibles() {
        return mesaRepository.findByDisponible(false).size();
    }

    /**
     * Obtener capacidad total del restaurante
     */
    public int obtenerCapacidadTotal() {
        return mesaRepository.findAll().stream()
                .mapToInt(Mesa::getCapacidad)
                .sum();
    }

    /**
     * Obtener capacidad disponible del restaurante
     */
    public int obtenerCapacidadDisponible() {
        return mesaRepository.findByDisponible(true).stream()
                .mapToInt(Mesa::getCapacidad)
                .sum();
    }
}
