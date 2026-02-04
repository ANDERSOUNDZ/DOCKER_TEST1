package com.bankfy.bank_meet.domain.ports.in;

import java.util.List;

import com.bankfy.bank_meet.domain.models.Movimientos;

public interface CreateMovimientoUseCase {
    Movimientos execute(Movimientos movimiento);

    List<Movimientos> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin);
}