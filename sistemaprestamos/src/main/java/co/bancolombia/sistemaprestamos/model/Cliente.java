package co.bancolombia.sistemaprestamos.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Cliente")
public class Cliente {
    @Id
    @Column(name="id_cliente")
    private long id;

    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    @OneToMany(mappedBy = "cliente")
    private List<Prestamo> prestamos;

    public Cliente(long id, String nombre, String email, String telefono, String direccion){
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono= telefono;
        this.direccion= direccion;
    }
    public Cliente(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
