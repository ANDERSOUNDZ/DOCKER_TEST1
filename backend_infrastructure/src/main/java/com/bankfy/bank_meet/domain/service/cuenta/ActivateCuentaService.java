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
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .map(c -> {
                    if (Boolean.TRUE.equals(c.getEstado())) {
                        throw new IllegalArgumentException("La cuenta ya está activa.");
                    }
                    c.setEstado(true);
                    return cuentaRepository.save(c);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada."));
    }
}