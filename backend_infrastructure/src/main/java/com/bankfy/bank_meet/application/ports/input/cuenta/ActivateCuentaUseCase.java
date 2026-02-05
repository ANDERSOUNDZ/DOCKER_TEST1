package com.bankfy.bank_meet.application.ports.input.cuenta;

import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

public interface ActivateCuentaUseCase {
    Cuenta execute(String numeroCuenta);
}