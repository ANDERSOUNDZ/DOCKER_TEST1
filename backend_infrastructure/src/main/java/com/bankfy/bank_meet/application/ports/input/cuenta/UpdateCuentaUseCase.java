package com.bankfy.bank_meet.application.ports.input.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import java.util.Map;

public interface UpdateCuentaUseCase {
    Cuenta execute(Long id, Cuenta cuenta);

    Cuenta executePartial(Long id, Map<String, Object> updates);
}