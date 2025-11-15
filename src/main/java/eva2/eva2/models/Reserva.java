package eva2.eva2.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime fechaHora;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    public Reserva() {}

    public Reserva(Long id, LocalDateTime fechaHora, Cliente cliente, Mesa mesa) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.mesa = mesa;
    }

    public Reserva(LocalDateTime fechaHora, Cliente cliente, Mesa mesa) {
        this.fechaHora = fechaHora;
        this.cliente = cliente;
        this.mesa = mesa;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    
    public Mesa getMesa() { return mesa; }
    public void setMesa(Mesa mesa) { this.mesa = mesa; }
}