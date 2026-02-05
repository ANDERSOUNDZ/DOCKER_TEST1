package com.bankfy.bank_meet.application.ports.input.cuenta;

import java.util.Map;

import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

public interface UpdateCuentaUseCase {
    Cuenta execute(Long id, Cuenta cuenta);

    Cuenta executePartial(Long id, Map<String, Object> updates);
}