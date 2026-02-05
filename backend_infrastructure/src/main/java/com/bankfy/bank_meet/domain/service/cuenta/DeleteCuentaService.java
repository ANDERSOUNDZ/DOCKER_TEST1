package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.DeleteCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCuentaService implements DeleteCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se puede inactivar: Cuenta no encontrada."));

        if (Boolean.FALSE.equals(cuenta.getEstado())) {
            throw new IllegalArgumentException("La cuenta ya se encuentra inactiva.");
        }

        cuenta.setEstado(false);

        cuentaRepository.save(cuenta);
    }
}