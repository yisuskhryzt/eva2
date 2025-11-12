package eva2.eva2.services;

import eva2.eva2.models.Cliente;
import eva2.eva2.models.Mesa;
import eva2.eva2.models.Reserva;
import eva2.eva2.repository.ClienteRepository;
import eva2.eva2.repository.MesaRepository;
import eva2.eva2.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Obtener todas las reservas
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Obtener reserva por ID
     */
    public Optional<Reserva> obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id);
    }

    /**
     * Obtener reservas de un cliente específico
     */
    public List<Reserva> obtenerReservasPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId);
    }

    /**
     * Obtener reservas futuras de un cliente
     */
    public List<Reserva> obtenerReservasFuturasDeCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId).stream()
                .filter(reserva -> reserva.getFechaHora().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    /**
     * Obtener reservas pasadas de un cliente
     */
    public List<Reserva> obtenerReservasPasadasDeCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId).stream()
                .filter(reserva -> reserva.getFechaHora().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    /**
     * Obtener todas las reservas futuras
     */
    public List<Reserva> obtenerReservasFuturas() {
        return reservaRepository.findByFechaHoraAfter(LocalDateTime.now());
    }

    /**
     * Obtener reservas de una mesa específica
     */
    public List<Reserva> obtenerReservasPorMesa(Long mesaId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));
        return reservaRepository.findByMesa(mesa);
    }

    /**
     * Obtener reservas por rango de fechas
     */
    public List<Reserva> obtenerReservasPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.findByFechaHoraBetween(inicio, fin);
    }

    /**
     * Crear una nueva reserva con validaciones completas
     */
    public Reserva crearReserva(Long clienteId, Long mesaId, LocalDateTime fechaHora) {
        // Validar que el cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + clienteId));

        // Validar que la mesa existe
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada con ID: " + mesaId));

        // Validar que la mesa está disponible
        if (!mesa.isDisponible()) {
            throw new IllegalStateException("La mesa número " + mesa.getNumero() + " no está disponible");
        }

        // Validar que la fecha no sea pasada
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede crear una reserva en una fecha pasada");
        }

        // Validar que la fecha no sea muy lejana (opcional: máximo 3 meses adelante)
        if (fechaHora.isAfter(LocalDateTime.now().plusMonths(3))) {
            throw new IllegalArgumentException("No se pueden hacer reservas con más de 3 meses de anticipación");
        }

        // Verificar que la mesa no esté reservada en esa fecha/hora
        if (reservaRepository.existsByMesaAndFechaHora(mesaId, fechaHora)) {
            throw new IllegalStateException("La mesa ya está reservada para esa fecha y hora");
        }

        // Verificar que no haya otra reserva muy cercana (por ejemplo, dentro de 2 horas)
        if (existeReservaCercana(mesaId, fechaHora)) {
            throw new IllegalStateException("Ya existe una reserva cercana a ese horario en esta mesa");
        }

        // Crear y guardar la reserva
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setMesa(mesa);
        reserva.setFechaHora(fechaHora);

        return reservaRepository.save(reserva);
    }

    /**
     * Actualizar una reserva existente
     */
    public Reserva actualizarReserva(Long id, Long clienteId, Long mesaId, LocalDateTime fechaHora) {
        Reserva reservaExistente = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada"));

        // Validar fecha
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("No se puede actualizar a una fecha pasada");
        }

        // Si se cambió la mesa o la fecha, verificar disponibilidad
        if (!reservaExistente.getMesa().getId().equals(mesaId) || 
            !reservaExistente.getFechaHora().equals(fechaHora)) {
            
            if (reservaRepository.existsByMesaAndFechaHora(mesaId, fechaHora)) {
                throw new IllegalStateException("La mesa ya está reservada para esa fecha y hora");
            }
        }

        reservaExistente.setCliente(cliente);
        reservaExistente.setMesa(mesa);
        reservaExistente.setFechaHora(fechaHora);

        return reservaRepository.save(reservaExistente);
    }

    /**
     * Cancelar/eliminar una reserva
     */
    public void cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + id));

        reservaRepository.delete(reserva);
    }

    /**
     * Cancelar reserva verificando que pertenece al cliente
     */
    public void cancelarReservaPorCliente(Long reservaId, Long clienteId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada con ID: " + reservaId));

        // Verificar que la reserva pertenece al cliente
        if (!reserva.getCliente().getId().equals(clienteId)) {
            throw new IllegalStateException("No tiene permiso para cancelar esta reserva");
        }

        // Opcional: Verificar que la reserva sea futura (no permitir cancelar reservas pasadas)
        if (reserva.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("No se pueden cancelar reservas pasadas");
        }

        reservaRepository.deleteById(reservaId);
    }

    /**
     * Verificar si existe una reserva cercana (dentro de 2 horas)
     */
    private boolean existeReservaCercana(Long mesaId, LocalDateTime fechaHora) {
        LocalDateTime dosHorasAntes = fechaHora.minusHours(2);
        LocalDateTime dosHorasDespues = fechaHora.plusHours(2);

        Mesa mesa = mesaRepository.findById(mesaId).orElse(null);
        if (mesa == null) return false;

        return reservaRepository.findByMesa(mesa).stream()
                .anyMatch(r -> r.getFechaHora().isAfter(dosHorasAntes) && 
                              r.getFechaHora().isBefore(dosHorasDespues));
    }

    /**
     * Verificar si una mesa está disponible en una fecha/hora específica
     */
    public boolean mesaDisponibleEnFechaHora(Long mesaId, LocalDateTime fechaHora) {
        return !reservaRepository.existsByMesaAndFechaHora(mesaId, fechaHora);
    }

    /**
     * Obtener mesas disponibles para una fecha/hora específica
     */
    public List<Mesa> obtenerMesasDisponiblesParaFechaHora(LocalDateTime fechaHora) {
        List<Mesa> todasLasMesasDisponibles = mesaRepository.findByDisponible(true);
        
        return todasLasMesasDisponibles.stream()
                .filter(mesa -> !reservaRepository.existsByMesaAndFechaHora(mesa.getId(), fechaHora))
                .collect(Collectors.toList());
    }

    /**
     * Contar reservas totales
     */
    public long contarReservas() {
        return reservaRepository.count();
    }

    /**
     * Contar reservas futuras
     */
    public long contarReservasFuturas() {
        return reservaRepository.findByFechaHoraAfter(LocalDateTime.now()).size();
    }

    /**
     * Contar reservas de un cliente
     */
    public long contarReservasPorCliente(Long clienteId) {
        return reservaRepository.findByClienteId(clienteId).size();
    }

    /**
     * Verificar si una reserva pertenece a un cliente
     */
    public boolean reservaPerteneceACliente(Long reservaId, Long clienteId) {
        Reserva reserva = reservaRepository.findById(reservaId).orElse(null);
        return reserva != null && reserva.getCliente().getId().equals(clienteId);
    }

    /**
     * Obtener reservas de hoy
     */
    public List<Reserva> obtenerReservasDeHoy() {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return reservaRepository.findByFechaHoraBetween(inicioDia, finDia);
    }

    /**
     * Obtener próximas reservas (próximas 24 horas)
     */
    public List<Reserva> obtenerProximasReservas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime en24Horas = ahora.plusHours(24);
        return reservaRepository.findByFechaHoraBetween(ahora, en24Horas);
    }
}
