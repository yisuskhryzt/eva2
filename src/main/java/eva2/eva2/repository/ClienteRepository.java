package eva2.eva2.repository;
import eva2.eva2.models.Cliente;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


//los repositorios permiten la comunicacion directa con la base de datos
//mediante JPA (Java Persistence API) se pueden realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
//consiste en una interfaz que extiende JpaRepository para heredar metodos predefinidos
//se relacionan con el modelo para definir la entidad con la que se trabajara en la base de datos

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
