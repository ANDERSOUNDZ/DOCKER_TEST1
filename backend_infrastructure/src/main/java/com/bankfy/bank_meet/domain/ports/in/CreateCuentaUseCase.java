package com.bankfy.bank_meet.domain.ports.in;

import com.bankfy.bank_meet.domain.models.Cuenta;

public interface CreateCuentaUseCase {
    Cuenta execute(Cuenta cuenta);
}