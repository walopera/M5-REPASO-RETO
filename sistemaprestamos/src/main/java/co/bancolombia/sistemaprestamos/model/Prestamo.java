package co.bancolombia.sistemaprestamos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name="prestamo")
public class Prestamo {
    @Id
    @Column(name="id_prestamo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprestamo;
    private BigDecimal monto;
    private BigDecimal interes;
    private Integer plazo;
    private Integer estado;

    @ManyToOne
    @JoinColumn(name = "cliente", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "prestamoasociado")
    private List<Transaccion> transacciones;

    public Prestamo(BigDecimal monto, BigDecimal interes, Integer plazo, Integer estado, Cliente cliente){
        this.monto= monto;
        this.interes =interes;
        this.plazo=plazo;
        this.estado=estado;
        this.cliente= cliente;
    }
    public Prestamo(){

    }

    public Long getIdprestamo() {
        return idprestamo;
    }

    public void setIdprestamo(Long idprestamo) {
        this.idprestamo = idprestamo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getInteres() {
        return interes;
    }

    public void setInteres(BigDecimal interes) {
        this.interes = interes;
    }

    public Integer getPlazo() {
        return plazo;
    }

    public void setPlazo(Integer plazo) {
        this.plazo = plazo;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Transaccion> getTransacciones() {
        return transacciones;
    }

    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }
}
