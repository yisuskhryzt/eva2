package eva2.eva2.models;
import java.util.List;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Entity;

@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numero;
    private int capacidad;
    private boolean disponible;

    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas;

    //Constructores

    public Mesa() {
    }

    public Mesa(Long id, int numero, int capacidad, boolean disponible) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.disponible = disponible;
    }

    //Getters

    public Long getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    //Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

}
