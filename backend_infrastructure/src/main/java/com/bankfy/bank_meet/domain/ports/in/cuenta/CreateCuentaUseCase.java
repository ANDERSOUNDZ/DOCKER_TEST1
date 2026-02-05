package com.bankfy.bank_meet.domain.ports.in.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;

public interface CreateCuentaUseCase {
    Cuenta execute(Cuenta cuenta);
}