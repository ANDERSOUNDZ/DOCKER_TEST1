package com.bankfy.bank_meet.domain.ports.in.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;

public interface ActivateCuentaUseCase {
    Cuenta execute(String numeroCuenta);
}