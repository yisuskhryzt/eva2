package eva2.eva2.config;

import eva2.eva2.models.Cliente;
import eva2.eva2.models.Mesa;
import eva2.eva2.models.Reserva;
import eva2.eva2.repository.ClienteRepository;
import eva2.eva2.repository.MesaRepository;
import eva2.eva2.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la tabla de clientes está vacía
        if (clienteRepository.count() == 0) {
            cargarDatos();
        }
    }

    private void cargarDatos() {
        // Crear clientes
        Cliente c1 = new Cliente();
        c1.setRut("12345678-9");
        c1.setNombre("Juan Pérez");
        c1.setEmail("juan@email.com");
        c1.setTelefono("+56912345678");
        clienteRepository.save(c1);

        Cliente c2 = new Cliente();
        c2.setRut("98765432-1");
        c2.setNombre("María García");
        c2.setEmail("maria@email.com");
        c2.setTelefono("+56987654321");
        clienteRepository.save(c2);

        Cliente c3 = new Cliente();
        c3.setRut("11223344-5");
        c3.setNombre("Carlos López");
        c3.setEmail("carlos@email.com");
        c3.setTelefono("+56911223344");
        clienteRepository.save(c3);

        Cliente c4 = new Cliente();
        c4.setRut("55667788-9");
        c4.setNombre("Ana Martínez");
        c4.setEmail("ana@email.com");
        c4.setTelefono("+56955667788");
        clienteRepository.save(c4);

        Cliente c5 = new Cliente();
        c5.setRut("22334455-6");
        c5.setNombre("Roberto Silva");
        c5.setEmail("roberto@email.com");
        c5.setTelefono("+56922334455");
        clienteRepository.save(c5);

        // Crear mesas
        Mesa m1 = new Mesa();
        m1.setNumero(1);
        m1.setCapacidad(2);
        m1.setDisponible(true);
        mesaRepository.save(m1);

        Mesa m2 = new Mesa();
        m2.setNumero(2);
        m2.setCapacidad(2);
        m2.setDisponible(true);
        mesaRepository.save(m2);

        Mesa m3 = new Mesa();
        m3.setNumero(3);
        m3.setCapacidad(4);
        m3.setDisponible(true);
        mesaRepository.save(m3);

        Mesa m4 = new Mesa();
        m4.setNumero(4);
        m4.setCapacidad(4);
        m4.setDisponible(true);
        mesaRepository.save(m4);

        Mesa m5 = new Mesa();
        m5.setNumero(5);
        m5.setCapacidad(6);
        m5.setDisponible(true);
        mesaRepository.save(m5);

        Mesa m6 = new Mesa();
        m6.setNumero(6);
        m6.setCapacidad(6);
        m6.setDisponible(true);
        mesaRepository.save(m6);

        Mesa m7 = new Mesa();
        m7.setNumero(7);
        m7.setCapacidad(8);
        m7.setDisponible(true);
        mesaRepository.save(m7);

        Mesa m8 = new Mesa();
        m8.setNumero(8);
        m8.setCapacidad(8);
        m8.setDisponible(true);
        mesaRepository.save(m8);

        // Crear reservas
        Reserva r1 = new Reserva();
        r1.setCliente(c1);
        r1.setMesa(m3);
        r1.setFechaHora(LocalDateTime.of(2025, 11, 20, 19, 0));
        reservaRepository.save(r1);

        Reserva r2 = new Reserva();
        r2.setCliente(c2);
        r2.setMesa(m5);
        r2.setFechaHora(LocalDateTime.of(2025, 11, 21, 20, 0));
        reservaRepository.save(r2);

        Reserva r3 = new Reserva();
        r3.setCliente(c3);
        r3.setMesa(m1);
        r3.setFechaHora(LocalDateTime.of(2025, 11, 22, 19, 30));
        reservaRepository.save(r3);

        Reserva r4 = new Reserva();
        r4.setCliente(c4);
        r4.setMesa(m7);
        r4.setFechaHora(LocalDateTime.of(2025, 11, 23, 18, 0));
        reservaRepository.save(r4);

        Reserva r5 = new Reserva();
        r5.setCliente(c5);
        r5.setMesa(m2);
        r5.setFechaHora(LocalDateTime.of(2025, 11, 24, 20, 30));
        reservaRepository.save(r5);

        System.out.println("✅ Datos iniciales cargados correctamente");
    }
}
