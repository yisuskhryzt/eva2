package eva2.eva2.repository;

import eva2.eva2.models.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    
    // Buscar mesas por disponibilidad
    List<Mesa> findByDisponible(boolean disponible);
    
    // Buscar mesas por capacidad mínima
    List<Mesa> findByCapacidadGreaterThanEqual(int capacidad);
}
