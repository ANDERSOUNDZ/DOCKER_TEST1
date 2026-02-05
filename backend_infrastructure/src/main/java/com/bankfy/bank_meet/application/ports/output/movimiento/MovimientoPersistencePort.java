package com.bankfy.bank_meet.application.ports.output.movimiento;

import com.bankfy.bank_meet.domain.models.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimientoPersistencePort {
    Movimiento save(Movimiento movimiento);

    Optional<Movimiento> findById(Long id);

    BigDecimal sumRetirosDelDia(Long clienteId, LocalDateTime inicio, LocalDateTime fin);

    List<Movimiento> findByClienteAndFechaRange(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}