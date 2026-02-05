package com.bankfy.bank_meet.application.service.cuenta;

import com.bankfy.bank_meet.application.ports.input.cuenta.ActivateCuentaUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.domain.models.Cuenta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateCuentaService implements ActivateCuentaUseCase {

    private final CuentaPersistencePort cuentaPersistencePort;

    @Override
    @Transactional
    public Cuenta execute(String numeroCuenta) {
        return cuentaPersistencePort.findByNumeroCuenta(numeroCuenta)
                .map(c -> {
                    if (Boolean.TRUE.equals(c.getEstado())) {
                        throw new IllegalArgumentException("La cuenta ya está activa.");
                    }
                    c.setEstado(true);
                    return cuentaPersistencePort.save(c);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con número: " + numeroCuenta));
    }
}