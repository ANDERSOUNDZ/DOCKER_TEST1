package com.bankfy.bank_meet.application.ports.input.movimiento;

import java.util.List;

import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

public interface GetMovimientoUseCase {
    List<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin);

    Movimiento obtenerPorId(Long id);
}