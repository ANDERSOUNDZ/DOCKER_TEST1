package com.bankfy.bank_meet.application.ports.input.movimiento;

import com.bankfy.bank_meet.domain.models.Movimiento;

public interface ReverseMovimientoUseCase {
    Movimiento execute(Long movimientoId);
}