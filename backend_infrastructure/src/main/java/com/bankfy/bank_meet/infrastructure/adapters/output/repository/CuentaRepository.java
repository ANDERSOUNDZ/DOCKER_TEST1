package com.bankfy.bank_meet.infrastructure.adapters.output.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankfy.bank_meet.domain.models.cuenta.Cuenta;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByClienteId(Long clienteId);

    boolean existsByClienteIdAndTipoCuenta(Long clienteId, String tipoCuenta);
    long countByClienteIdAndTipoCuenta(Long clienteId, String tipoCuenta);
}