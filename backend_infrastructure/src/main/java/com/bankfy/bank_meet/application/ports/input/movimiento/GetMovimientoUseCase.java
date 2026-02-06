package com.bankfy.bank_meet.application.ports.input.movimiento;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.bankfy.bank_meet.domain.models.movimiento.Movimiento;

public interface GetMovimientoUseCase {
    List<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin);

    Movimiento obtenerPorId(Long id);

    Page<Movimiento> obtenerPorFiltros(Long clienteId, String fechaInicio, String fechaFin, Pageable pageable);
}