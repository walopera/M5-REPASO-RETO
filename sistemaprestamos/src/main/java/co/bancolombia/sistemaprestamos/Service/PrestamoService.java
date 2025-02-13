package co.bancolombia.sistemaprestamos.Service;

import co.bancolombia.sistemaprestamos.dto.CreaPrestamoDTO;
import co.bancolombia.sistemaprestamos.dto.GestionClienteDTO;
import co.bancolombia.sistemaprestamos.dto.GestionPrestamoDTO;
import co.bancolombia.sistemaprestamos.model.*;
import co.bancolombia.sistemaprestamos.repository.ClienteRepository;
import co.bancolombia.sistemaprestamos.repository.PrestamoRepository;
import co.bancolombia.sistemaprestamos.repository.TransaccionRepository;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PrestamoService {
    private  final PrestamoRepository prestamoRepository;
    private  final ClienteRepository clienteRepository;
    private final TransaccionRepository transaccionRepository;
    public PrestamoService(PrestamoRepository prestamoRepository, ClienteRepository clienteRepository, TransaccionRepository transaccionRepository){
        this.prestamoRepository = prestamoRepository;
        this.clienteRepository = clienteRepository;
        this.transaccionRepository = transaccionRepository;
    }
    //Adicionar prestamos
    @Transactional
    public Prestamo save(CreaPrestamoDTO creaDTO){
        Cliente cliente = clienteRepository.findById(creaDTO.getIdcliente()).orElseThrow(() ->
                new NoSuchElementException("Cliente no existe"));
        //Crea prestamos
        Prestamo prestamo = new Prestamo();
        prestamo.setMonto(creaDTO.getMonto());
        prestamo.setPlazo(creaDTO.getPlazo());
        prestamo.setInteres(BigDecimal.valueOf(2.5));
        prestamo.setCliente(cliente);
        prestamo.setEstado(1);
        //Prestamo prestamonew = prestamoRepository.save(prestamo);
        if(prestamoRepository.save(prestamo)==null){
            throw new RuntimeException("Prestamo no creado");
        }
        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(LocalDate.now());
        transaccion.setTime(LocalTime.now());
        transaccion.setTiponovedad("Prestamo nuevo creado");
        transaccion.setValor(creaDTO.getMonto());
        transaccion.setPrestamoasociado(prestamo);
        //grabat base de datos
        transaccionRepository.save(transaccion);
        return prestamo;


    }
    //aprobar prestamos
    @Transactional
    public Prestamo aprobado(GestionPrestamoDTO gestionDTO){
        Prestamo prestamo = prestamoRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id no existe"));
        if(!prestamo.getEstado().equals(1)){
            throw new RuntimeException("Prestamo en estado incorrecto");
        }
        prestamo.setEstado(2);
        prestamoRepository.save(prestamo);

        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(LocalDate.now());
        transaccion.setTime(LocalTime.now());
        transaccion.setTiponovedad("Prestamo Aprobado");
        transaccion.setValor(prestamo.getMonto());
        transaccion.setPrestamoasociado(prestamo);
        //grabat base de datos
        transaccionRepository.save(transaccion);

        return prestamo;
    }
    //aprobar prestamos
    @Transactional
    public Prestamo rechazar(GestionPrestamoDTO gestionDTO){
        Prestamo prestamo = prestamoRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id no existe"));
        if(!prestamo.getEstado().equals(1)){
            throw new RuntimeException("Prestamo en estado incorrecto");
        }
        prestamo.setEstado(3);
        prestamoRepository.save(prestamo);
        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(LocalDate.now());
        transaccion.setTime(LocalTime.now());
        transaccion.setTiponovedad("Prestamo Rechazado");
        transaccion.setValor(prestamo.getMonto());
        transaccion.setPrestamoasociado(prestamo);
        //grabar base de datos
        transaccionRepository.save(transaccion);
        return prestamo;
    }

    //aprobar prestamos
    @Transactional
    public List<Planpagos> plan(GestionPrestamoDTO gestionDTO){
        Prestamo prestamo = prestamoRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id no existe"));
        List<Planpagos> listplanpagos = new ArrayList<>();
        BigDecimal kapital=prestamo.getMonto().divide(BigDecimal.valueOf(prestamo.getPlazo()),0, RoundingMode.HALF_UP);
        BigDecimal interes;
        BigDecimal kapitalajustado;
        BigDecimal saldo=prestamo.getMonto();


        for(int i=1;i<=prestamo.getPlazo();i++){
            interes = saldo.multiply(prestamo.getInteres().divide(BigDecimal.valueOf(100)));
            saldo = saldo.subtract(kapital);
            if(i ==prestamo.getPlazo()-1){
                kapitalajustado = saldo.subtract(kapital);
                saldo = saldo.subtract(kapitalajustado);
            }
            listplanpagos.add(new Planpagos(i,kapital, interes,kapital.add(interes),saldo ));
        }
        return listplanpagos;
    }
    //consulta estado
    @Transactional
    public String estadoprestamo(GestionPrestamoDTO gestionDTO){
        Prestamo prestamo = prestamoRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id no existe"));

        return prestamoRepository.descripcionestado(gestionDTO.getId());
    }
    //consulta datos prestamo
    @Transactional
    public String datosprestamo(GestionPrestamoDTO gestionDTO){
        Prestamo prestamo = prestamoRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id no existe"));
        Datosprestamo datopres = prestamoRepository.datosprestamo(gestionDTO.getId());
        return " \n" + datopres ;
    }

    //consulta ultimos 3 prestamos cliente y todas sus transacciones
    @Transactional
    public List<Prestamoscliente> prestamosclientes (GestionClienteDTO gestionDTO){
        Cliente cliente = clienteRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id cliente no existe"));

        List<Prestamoscliente> prestamosclienteList = new ArrayList<>();
        prestamosclienteList = clienteRepository.dacliente(gestionDTO.getId());
        if (prestamosclienteList.isEmpty()  ){
            throw new RuntimeException("Cliente no tiene Prestamo");
        }


        return prestamosclienteList;
    }
    //consulta prestamos cliente
    @Transactional
    public List<Datosprestamo> lisprestamosclientes (GestionClienteDTO gestionDTO){
        Cliente cliente = clienteRepository.findById(gestionDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Id cliente no existe"));

        List<Datosprestamo> prestamosclientelis = new ArrayList<>();
        prestamosclientelis = clienteRepository.prestamoscliente(gestionDTO.getId());
        if (prestamosclientelis.isEmpty()  ){
            throw new RuntimeException("Cliente no tiene Prestamo");
        }

        return prestamosclientelis;
    }

}
