package eva2.eva2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import eva2.eva2.models.Cliente;
import eva2.eva2.models.Mesa;
import eva2.eva2.models.Reserva;
import eva2.eva2.repository.ClienteRepository;
import eva2.eva2.repository.MesaRepository;
import eva2.eva2.repository.ReservaRepository;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

    public DataLoader(ClienteRepository clienteRepository, MesaRepository mesaRepository, 
                     ReservaRepository reservaRepository) {
        this.clienteRepository = clienteRepository;
        this.mesaRepository = mesaRepository;
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (clienteRepository.count() == 0) {
            // Crear clientes
            Cliente cliente1 = new Cliente(null, "12345678-9", "Juan Pérez", "juan@email.com", "+56912345678");
            Cliente cliente2 = new Cliente(null, "98765432-1", "María García", "maria@email.com", "+56987654321");
            Cliente cliente3 = new Cliente(null, "11223344-5", "Carlos López", "carlos@email.com", "+56911223344");
            Cliente cliente4 = new Cliente(null, "55667788-9", "Ana Martínez", "ana@email.com", "+56955667788");
            Cliente cliente5 = new Cliente(null, "22334455-6", "Roberto Silva", "roberto@email.com", "+56922334455");

            clienteRepository.save(cliente1);
            clienteRepository.save(cliente2);
            clienteRepository.save(cliente3);
            clienteRepository.save(cliente4);
            clienteRepository.save(cliente5);

            // Crear mesas
            Mesa mesa1 = new Mesa(null, 1, 2, true);
            Mesa mesa2 = new Mesa(null, 2, 2, true);
            Mesa mesa3 = new Mesa(null, 3, 4, true);
            Mesa mesa4 = new Mesa(null, 4, 4, true);
            Mesa mesa5 = new Mesa(null, 5, 6, true);
            Mesa mesa6 = new Mesa(null, 6, 6, true);
            Mesa mesa7 = new Mesa(null, 7, 8, true);
            Mesa mesa8 = new Mesa(null, 8, 8, true);

            mesaRepository.save(mesa1);
            mesaRepository.save(mesa2);
            mesaRepository.save(mesa3);
            mesaRepository.save(mesa4);
            mesaRepository.save(mesa5);
            mesaRepository.save(mesa6);
            mesaRepository.save(mesa7);
            mesaRepository.save(mesa8);

            // Crear reservas
            Reserva reserva1 = new Reserva(null, LocalDateTime.of(2025, 11, 20, 19, 0), cliente1, mesa3);
            Reserva reserva2 = new Reserva(null, LocalDateTime.of(2025, 11, 21, 20, 0), cliente2, mesa5);
            Reserva reserva3 = new Reserva(null, LocalDateTime.of(2025, 11, 22, 19, 30), cliente3, mesa1);
            Reserva reserva4 = new Reserva(null, LocalDateTime.of(2025, 11, 23, 18, 0), cliente4, mesa7);
            Reserva reserva5 = new Reserva(null, LocalDateTime.of(2025, 11, 24, 20, 30), cliente5, mesa2);

            reservaRepository.save(reserva1);
            reservaRepository.save(reserva2);
            reservaRepository.save(reserva3);
            reservaRepository.save(reserva4);
            reservaRepository.save(reserva5);

            System.out.println("✓ Base de datos inicializada con datos de prueba");
        }
    }
}
