package com.bankfy.bank_meet.application.service.cuenta;

import com.bankfy.bank_meet.application.ports.input.cuenta.GetCuentaUseCase;
import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

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

    private final CuentaPersistencePort cuentaPersistencePort;

    @Override
    public Cuenta getById(Long id) {
        return cuentaPersistencePort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + id));
    }

    @Override
    public Cuenta getByNumeroCuenta(String numeroCuenta) {
        return cuentaPersistencePort.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));
    }

    @Override
    public Page<Cuenta> getAll(Pageable pageable, String search) {
        if (search != null && !search.isEmpty()) {
            return cuentaPersistencePort.findBySearch(search, pageable);
        }
        return cuentaPersistencePort.findAll(pageable);
    }

    @Override
    public List<Cuenta> getByClienteId(Long clienteId) {
        return cuentaPersistencePort.findAll(Pageable.unpaged()).getContent().stream()
                .filter(c -> c.getCliente() != null && clienteId.equals(c.getCliente().getId()))
                .toList();
    }
}