package eva2.eva2.models;

import java.util.List;
import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rut;
    
    private String nombre;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String telefono;

    @OneToMany(mappedBy = "cliente")
    private List<Reserva> reservas;

    public Cliente() {}

    public Cliente(Long id, String rut, String nombre, String email, String telefono) {
        this.id = id;
        this.rut = rut;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public List<Reserva> getReservas() { return reservas; }
    public void setReservas(List<Reserva> reservas) { this.reservas = reservas; }
}
