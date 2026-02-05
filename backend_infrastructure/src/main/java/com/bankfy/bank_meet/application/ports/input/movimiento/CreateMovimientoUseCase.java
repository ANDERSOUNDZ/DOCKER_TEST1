package com.bankfy.bank_meet.application.ports.input.movimiento;

import com.bankfy.bank_meet.domain.models.Movimiento;

public interface CreateMovimientoUseCase {
    Movimiento execute(Movimiento movimiento);
}