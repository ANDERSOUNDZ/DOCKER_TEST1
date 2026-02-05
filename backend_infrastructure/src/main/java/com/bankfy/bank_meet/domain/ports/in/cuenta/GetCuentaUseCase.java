package com.bankfy.bank_meet.domain.ports.in.cuenta;

import com.bankfy.bank_meet.domain.models.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetCuentaUseCase {
    Cuenta getById(Long id);

    Cuenta getByNumeroCuenta(String numeroCuenta);

    Page<Cuenta> getAll(Pageable pageable);

    List<Cuenta> getByClienteId(Long clienteId);
}