package eva2.eva2.repository;

import eva2.eva2.models.Cliente;
import eva2.eva2.models.Mesa;
import eva2.eva2.models.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    // Buscar reservas por cliente
    List<Reserva> findByCliente(Cliente cliente);
    
    // Buscar reservas por cliente ID
    List<Reserva> findByClienteId(Long clienteId);
    
    // Buscar reservas por mesa
    List<Reserva> findByMesa(Mesa mesa);
    
    // Buscar reservas futuras
    List<Reserva> findByFechaHoraAfter(LocalDateTime fechaHora);
    
    // Buscar reservas por rango de fechas
    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
    
    // Verificar si una mesa está reservada en una fecha/hora específica
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r WHERE r.mesa.id = :mesaId AND r.fechaHora = :fechaHora")
    boolean existsByMesaAndFechaHora(@Param("mesaId") Long mesaId, @Param("fechaHora") LocalDateTime fechaHora);
}
