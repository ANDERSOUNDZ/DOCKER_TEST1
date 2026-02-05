package com.bankfy.bank_meet.domain.service.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import com.bankfy.bank_meet.domain.ports.in.cuenta.GetCuentaUseCase;
import com.bankfy.bank_meet.infrastructure.output.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCuentaService implements GetCuentaUseCase {

    private final CuentaRepository cuentaRepository;

    @Override
    public Cuenta getById(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));
    }

    @Override
    public Cuenta getByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
    }

    @Override
    public Page<Cuenta> getAll(Pageable pageable) {
        return cuentaRepository.findAll(pageable);
    }

    @Override
    public List<Cuenta> getByClienteId(Long clienteId) {
        // Mejoramos el uso de Stream y Lambdas
        return cuentaRepository.findAll().stream()
                .filter(c -> c.getCliente() != null && clienteId.equals(c.getCliente().getId()))
                .toList();
    }
}