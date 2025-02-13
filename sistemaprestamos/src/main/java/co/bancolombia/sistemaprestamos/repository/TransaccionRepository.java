package co.bancolombia.sistemaprestamos.repository;

import co.bancolombia.sistemaprestamos.model.Prestamo;
import co.bancolombia.sistemaprestamos.model.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

}
