package com.bankfy.bank_meet.application.service.cuenta;

import com.bankfy.bank_meet.application.ports.input.cuenta.DeleteCuentaUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCuentaService implements DeleteCuentaUseCase {

    private final CuentaPersistencePort cuentaPersistencePort;

    @Override
    @Transactional
    public void execute(Long id) {
        cuentaPersistencePort.findById(id)
                .map(cuenta -> {
                    if (Boolean.FALSE.equals(cuenta.getEstado())) {
                        throw new IllegalArgumentException("La cuenta ya se encuentra inactiva.");
                    }
                    cuenta.setEstado(false);
                    return cuentaPersistencePort.save(cuenta);
                })
                .orElseThrow(() -> new IllegalArgumentException("No se puede inactivar: ID no encontrado: " + id));
    }
}