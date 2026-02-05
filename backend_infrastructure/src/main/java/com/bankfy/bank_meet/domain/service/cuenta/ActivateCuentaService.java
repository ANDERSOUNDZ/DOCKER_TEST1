package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.ActivateCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivateCuentaService implements ActivateCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public Cuenta execute(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(
                        () -> new IllegalArgumentException("No existe ninguna cuenta con el número: " + numeroCuenta));

        if (Boolean.TRUE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("La cuenta " + numeroCuenta + " ya está activa.");
        }

        cuenta.setEstado(true);

        return cuentaRepository.save(cuenta);
    }
}