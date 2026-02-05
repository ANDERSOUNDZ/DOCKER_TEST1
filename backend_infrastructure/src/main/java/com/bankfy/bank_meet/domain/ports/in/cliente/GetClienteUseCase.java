package com.bankfy.bank_meet.domain.ports.in.cliente;

import com.bankfy.bank_meet.domain.models.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetClienteUseCase {
    Cliente getById(Long id);

    Page<Cliente> getAll(String search, Pageable pageable);
}