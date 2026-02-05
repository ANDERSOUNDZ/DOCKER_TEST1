package com.bankfy.bank_meet.application.ports.input.movimiento;

import com.bankfy.bank_meet.domain.models.ReporteEstadoCuenta;
import java.time.LocalDateTime;

public interface GetReporteUseCase {
    ReporteEstadoCuenta generarReporte(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}