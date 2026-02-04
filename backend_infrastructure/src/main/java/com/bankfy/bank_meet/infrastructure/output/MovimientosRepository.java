package com.bankfy.bank_meet.infrastructure.output;

import com.bankfy.bank_meet.domain.models.Movimientos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientosRepository extends JpaRepository<Movimientos, Long> {
    @Query("SELECT m FROM Movimientos m WHERE m.cuenta.cliente.id = :clienteId " +
            "AND m.fecha BETWEEN :inicio AND :fin")
    List<Movimientos> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}