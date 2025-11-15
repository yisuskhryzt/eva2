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

    /**
     * Obtener reservas por cliente
     */
    List<Reserva> findByClienteId(Long clienteId);

    /**
     * Obtener reservas por mesa
     */
    List<Reserva> findByMesa(Mesa mesa);

    /**
     * Obtener reservas futuras (después de una fecha)
     */
    List<Reserva> findByFechaHoraAfter(LocalDateTime fechaHora);

    /**
     * Obtener reservas en un rango de fechas
     */
    List<Reserva> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Verificar si existe una reserva para una mesa en una fecha/hora específica
     */
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reserva r WHERE r.mesa.id = :mesaId AND r.fechaHora = :fechaHora")
    boolean existsByMesaAndFechaHora(@Param("mesaId") Long mesaId, @Param("fechaHora") LocalDateTime fechaHora);

    /**
     * Contar reservas por cliente
     */
    long countByClienteId(Long clienteId);

    /**
     * Contar reservas por mesa
     */
    long countByMesa(Mesa mesa);
}
