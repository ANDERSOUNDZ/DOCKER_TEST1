package com.bankfy.bank_meet.infrastructure.adapters.output.persistence.cuenta;

import com.bankfy.bank_meet.application.ports.output.cuenta.CuentaPersistencePort;
import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;
import com.bankfy.bank_meet.infrastructure.adapters.output.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CuentaPersistenceAdapter implements CuentaPersistencePort {

    private final CuentaRepository repository;

    @Override
    public Cuenta save(Cuenta cuenta) {
        return repository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Cuenta> findByNumeroCuenta(String numeroCuenta) {
        return repository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public boolean existsByNumeroCuenta(String numeroCuenta) {
        return repository.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    public boolean existsByClienteId(Long clienteId) {
        return repository.existsByClienteId(clienteId);
    }

    @Override
    public long countByClienteIdAndTipoCuenta(Long clienteId, String tipoCuenta) {
        return repository.countByClienteIdAndTipoCuenta(clienteId, tipoCuenta);
    }

    @Override
    public Page<Cuenta> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}