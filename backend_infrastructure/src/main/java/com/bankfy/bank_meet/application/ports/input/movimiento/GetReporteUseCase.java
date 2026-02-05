package com.bankfy.bank_meet.application.ports.input.movimiento;

import java.time.LocalDateTime;

import com.bankfy.bank_meet.infrastructure.adapters.input.dtos.movimiento.ReporteEstadoCuentaDTO;

public interface GetReporteUseCase {
    ReporteEstadoCuentaDTO generarReporte(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}