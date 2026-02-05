package com.bankfy.bank_meet.application.ports.output.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CuentaPersistencePort {
    Cuenta save(Cuenta cuenta);

    Optional<Cuenta> findById(Long id);

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByClienteId(Long clienteId);

    long countByClienteIdAndTipoCuenta(Long clienteId, String tipoCuenta);

    Page<Cuenta> findAll(Pageable pageable);

    void deleteById(Long id);
}