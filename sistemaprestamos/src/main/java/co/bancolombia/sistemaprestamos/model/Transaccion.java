package co.bancolombia.sistemaprestamos.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="transaccion")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_transaccion")
    private Long idtransaccion;

    @Column(name= "tipo_novedad")
    private String tiponovedad;
    private BigDecimal valor;
    private LocalDate fecha;
    private LocalTime hora;
    @ManyToOne
    @JoinColumn(name="prestamo_asociado", nullable = false)
    private Prestamo prestamoasociado;

    public Transaccion(Prestamo prestamoasociado, String tiponovedad, BigDecimal valor, LocalDate fecha, LocalTime hora){
        this.prestamoasociado=prestamoasociado;
        this.tiponovedad=tiponovedad;
        this.valor=valor;
        this.fecha=fecha;
        this.hora=hora;
    }
    public Transaccion(){

    }

    public Long getIdtransaccion() {
        return idtransaccion;
    }

    public void setIdtransaccion(Long idtransaccion) {
        this.idtransaccion = idtransaccion;
    }



    public void setPrestamoasociado(Prestamo prestamoasociado) {
        this.prestamoasociado = prestamoasociado;
    }



    public String getTiponovedad() {
        return tiponovedad;
    }

    public void setTiponovedad(String tiponovedad) {
        this.tiponovedad = tiponovedad;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getTime() {
        return hora;
    }

    public void setTime(LocalTime hora) {
        this.hora = hora;
    }
}
