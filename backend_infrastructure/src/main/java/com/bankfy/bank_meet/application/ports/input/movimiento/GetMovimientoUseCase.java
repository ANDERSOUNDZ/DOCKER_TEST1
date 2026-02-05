package com.bankfy.bank_meet.application.ports.input.movimiento;

import com.bankfy.bank_meet.domain.models.Movimiento;
import java.util.List;

public interface GetMovimientoUseCase {
    List<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin);

    Movimiento obtenerPorId(Long id);
}