package com.bankfy.bank_meet.domain.ports.in.movimiento;

import com.bankfy.bank_meet.domain.models.Movimientos;

public interface ReverseMovimientoUseCase {
    Movimientos execute(Long movimientoId);
}