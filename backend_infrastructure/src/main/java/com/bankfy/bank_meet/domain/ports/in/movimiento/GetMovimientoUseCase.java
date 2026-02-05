package com.bankfy.bank_meet.domain.ports.in.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;
import java.util.List;

public interface GetMovimientoUseCase {
    List<Movimientos> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin);

    Movimientos obtenerPorId(Long id);
}