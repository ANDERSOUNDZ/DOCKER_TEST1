package com.bankfy.bank_meet.application.ports.output.movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

public interface MovimientoPersistencePort {
    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(Long id);

    BigDecimal sumRetirosDelDia(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

    List<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

    Page<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin,
            Pageable pageable);

    Page<Movimiento> findAllByFechaRange(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);

    Page<Movimiento> findAllWithSearch(LocalDateTime inicio, LocalDateTime fin, String search, Pageable pageable);
}